import os
import csv
import psycopg2
from psycopg2.extras import execute_values
import sys

def get_db_connection():
    """Connect to PostgreSQL database"""
    try:
        conn = psycopg2.connect(os.getenv('DATABASE_URL'))
        return conn
    except psycopg2.Error as e:
        print(f"Error connecting to database: {e}")
        sys.exit(1)

def populate_reference_tables(conn, csv_file):
    """Extract unique reference values from CSV and insert into lookup tables"""
    cur = conn.cursor()
    
    # Using sets to only store unique values. Note: Since sets are unordered in Python, the values in the reference tables will not be in the same order as in the CSV.
    regions_set = set()
    building_types_set = set()
    
    # Extract unique values from CSV (streaming, doesn't load all rows)
    print("Extracting unique values from CSV...")
    with open(csv_file, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            region_str = row.get('region', '').strip()
            if region_str:
                regions_set.add(region_str)
            
            byggnadstyp = row.get('byggnadstyp', '').strip()
            if byggnadstyp:
                building_types_set.add(byggnadstyp)
    
    try:
        # Batch insert regions
        print(f"Inserting {len(regions_set)} regions...")
        region_data = []
        for region_str in regions_set:
            region_id, region_name = region_str.split(' ', 1)
            region_type = None
            if region_id:
                if region_id == "00":
                    region_type = 1
                elif len(region_id) == 2:
                    region_type = 2
                elif len(region_id) == 4:
                    region_type = 3
                else:
                    print(f"  Warning: Could not set region_type for region_id '{region_id}'")
                region_data.append((region_id, region_name, region_type))
            else:
                print(f"  Warning: Could not parse region '{region_str}'")
        
        if region_data:
            execute_values(
                cur,
                "INSERT INTO region (id, name, type_id) VALUES %s ON CONFLICT (id) DO NOTHING",
                region_data,
                page_size=1000
            )
        
        # Batch insert building types
        print(f"Inserting {len(building_types_set)} building types...")
        building_type_data = [(name,) for name in building_types_set]
        if building_type_data:
            execute_values(
                cur,
                "INSERT INTO building_type (name) VALUES %s ON CONFLICT (name) DO NOTHING",
                building_type_data,
                page_size=1000
            )
        
        conn.commit()
        print("✓ Reference tables populated")
        
    except psycopg2.Error as e:
        conn.rollback()
        print(f"Error populating reference tables: {e}")
        sys.exit(1)
    finally:
        cur.close()

def load_reference_data(conn):
    """Load lookup tables into memory for mapping"""
    cur = conn.cursor()
    
    # Load regions (from CSV)
    cur.execute("SELECT id, name FROM region")
    regions = {row[1]: row[0] for row in cur.fetchall()}
    
    # Load building types (from CSV)
    cur.execute("SELECT id, name FROM building_type")
    building_types = {row[1]: row[0] for row in cur.fetchall()}
    
    cur.close()
    
    return regions, building_types

def insert_shoreline_building_count_batch(conn, csv_file, regions, building_types, batch_size=5000):
    """Insert shoreline building data in batches for better performance"""
    cur = conn.cursor()

    # Load the small static reference tables once
    cur.execute("SELECT id, name FROM shoreline_type")
    shoreline_types = {row[1]: row[0] for row in cur.fetchall()}
    
    cur.execute("SELECT id, name FROM area_type")
    area_types = {row[1]: row[0] for row in cur.fetchall()}
    
    try:
        batch_data = []
        row_count = 0
        skipped_rows = 0
        
        with open(csv_file, 'r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            
            for idx, row in enumerate(reader, 1):
                try:
                    region_str = row.get('region', '').strip()
                    building_type_name = row.get('byggnadstyp', '').strip()
                    shoreline_type_name = row.get('strandtyp', '').strip()
                    area_type_name = row.get('typ av område', '').strip()
                    year = int(row.get('år', 0))
                    building_count_str = row.get('Antal byggnader inom 100 meter från strand', '').strip()

                    # Parse building count: Replace .. and missing values with None (= NULL in database)
                    if building_count_str == '..':
                        building_count = None
                    elif building_count_str:
                        building_count = int(building_count_str)
                    else:
                        building_count = None
                    
                    # Parse region: Keep only the name part for looking up IDs in the next step: "01 Stockholms län" -> "Stockholms län"
                    region_name = None
                    if region_str and ' ' in region_str:
                        region_name = region_str.split(' ', 1)[1]
                    
                    # Look up IDs
                    region_id = regions.get(region_name) if region_name else None
                    building_type_id = building_types.get(building_type_name)
                    shoreline_type_id = shoreline_types.get(shoreline_type_name)
                    area_type_id = area_types.get(area_type_name)
                    
                    batch_data.append((region_id, building_type_id, shoreline_type_id, area_type_id, year, building_count))
                    row_count += 1
                    
                    # Insert when batch is full
                    if len(batch_data) >= batch_size:
                        execute_values(
                            cur,
                            """INSERT INTO shoreline_building_count 
                               (region_id, building_type_id, shoreline_type_id, area_type_id, year, building_count) 
                               VALUES %s""",
                            batch_data,
                            page_size=1000
                        )
                        conn.commit()
                        print(f"  Inserted {row_count} rows...")
                        batch_data = []
                    
                except ValueError as e:
                    print(f"  Error parsing row {idx}: {e}")
                    skipped_rows += 1
                    continue
            
            # Insert remaining batch
            if batch_data:
                execute_values(
                    cur,
                    """INSERT INTO shoreline_building_count 
                        (region_id, building_type_id, shoreline_type_id, area_type_id, year, building_count)
                       VALUES %s""",
                    batch_data,
                    page_size=1000
                )
                conn.commit()
        
        print(f"\n✓ Successfully inserted {row_count} rows into shoreline_building_count")
        if skipped_rows > 0:
            print(f"  Skipped {skipped_rows} rows due to errors")
        
    except psycopg2.Error as e:
        conn.rollback()
        print(f"Error inserting data: {e}")
        sys.exit(1)
    finally:
        cur.close()

def load_building_data():
    """Main function to load shoreline building data"""
    csv_file = os.getenv('CSV_PATH', 'sample.csv')
    
    conn = get_db_connection()
    try:
        print(f"Reading CSV file: {csv_file}\n")
        
        print("Populating reference tables from CSV...")
        populate_reference_tables(conn, csv_file)
        
        print("\nLoading reference data into memory...")
        regions, building_types = load_reference_data(conn)
        print(f"  Loaded {len(regions)} regions, {len(building_types)} building types")
        
        print("\nInserting shoreline building data...")
        insert_shoreline_building_count_batch(conn, csv_file, regions, building_types)
        
    finally:
        conn.close()

if __name__ == '__main__':
    load_building_data()