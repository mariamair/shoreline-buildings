-- Create tables
CREATE TABLE IF NOT EXISTS region_type (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS region (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    type_id INT,
    FOREIGN KEY (type_id)
        REFERENCES region_type (id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS building_type (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS shoreline_type (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS area_type (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS building_count (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    region_code VARCHAR(10),
    FOREIGN KEY (region_id)
        REFERENCES region (code)
        ON DELETE SET NULL ON UPDATE CASCADE,
    building_type_id INT,
    FOREIGN KEY (building_type_id)
        REFERENCES building_type (id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    shoreline_type_id INT,
    FOREIGN KEY (shoreline_type_id)
        REFERENCES shoreline_type (id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    area_type_id INT,
    FOREIGN KEY (area_type_id)
        REFERENCES area_type (id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    year INT NOT NULL,
    count INT
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_building_count_id ON building_count(id);
CREATE INDEX IF NOT EXISTS idx_building_count_region_code ON building_count(region_code);
CREATE INDEX IF NOT EXISTS idx_building_count_building_type_id ON building_count(building_type_id);
CREATE INDEX IF NOT EXISTS idx_building_count_shoreline_type_id ON building_count(shoreline_type_id);
CREATE INDEX IF NOT EXISTS idx_building_count_area_type_id ON building_count(area_type_id);

CREATE INDEX IF NOT EXISTS idx_region_code ON region(code ASC);
CREATE INDEX IF NOT EXISTS idx_region_type_id ON region(type_id);

-- Insert category data
INSERT INTO region_type (name) VALUES
    ('sverige'),
    ('län'),
    ('kommun')
ON CONFLICT DO NOTHING;

INSERT INTO shoreline_type (name) VALUES
    ('totalt'),
    ('hav'),
    ('inlandsvatten')
ON CONFLICT DO NOTHING;

INSERT INTO area_type (name) VALUES
    ('totalt'),
    ('inom tätort'),
    ('inom formellt skyddad natur')
ON CONFLICT DO NOTHING;
