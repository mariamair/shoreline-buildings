package com.buildings.repository;

import com.buildings.domain.AreaType;
import com.buildings.domain.BuildingCountEntity;
import com.buildings.domain.BuildingType;
import com.buildings.domain.Region;
import com.buildings.domain.RegionType;
import com.buildings.domain.ShorelineType;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class BuildingCountRepository {
  private final JdbcTemplate jdbcTemplate;

    public BuildingCountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

  // Fetch a building count entity. Use LEFT JOIN to fetch a building count entity even if it doesn't have a region or types'.
  public Optional<BuildingCountEntity> findById(Long id) {
    log.info("Repository: Fetching building count entitity with id " + id + "...");

    String sql = "SELECT b.id, b.year, b.building_count " + 
        ", r.id AS region_id, r.name AS region_name" +
        ", bt.id AS building_type_id, bt.name AS building_type_name" +
        ", st.id AS shoreline_type_id, st.name AS shoreline_type_name" +
        ", at.id AS area_type_id, at.name AS area_type_name " + 
        "FROM building_count b " +
        "LEFT JOIN region r ON b.region_id = r.id " +
        "LEFT JOIN building_type bt ON b.building_type_id = bt.id " + 
        "LEFT JOIN shoreline_type st ON b.shoreline_type_id = st.id " + 
        "LEFT JOIN area_type at ON b.area_type_id = at.id " + 
        "WHERE b.id = ?";

        log.info(sql);

    try {
      BuildingCountEntity buildingCount = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
        BuildingCountEntity b = new BuildingCountEntity(
          rs.getLong("id"), 
          null,
          null,
          null,
          null,
          rs.getInt("year"), 
          null);

         // Create Region if it exists in database
          if (rs.getObject("region_id") != null) {
            Region region = new Region(
              rs.getString("region_id"), 
              rs.getString("region_name"), 
              null);
            
            b.setRegion(region);
          }
 
          // Create BuildingType if it exists in database
          if (rs.getObject("building_type_id") != null) {
            BuildingType buildingType = new BuildingType(
              rs.getInt("building_type_id"), 
              rs.getString("building_type_name"));
            
            b.setBuildingType(buildingType);
          }

          // Create ShorelineType if it exists in database
          if (rs.getObject("shoreline_type_id") != null) {
            ShorelineType shorelineType = new ShorelineType(
              rs.getInt("shoreline_type_id"), 
              rs.getString("shoreline_type_name"));
            
            b.setShorelineType(shorelineType);
          }

          // Create AreaType if it exists in database
          if (rs.getObject("area_type_id") != null) {
            AreaType areaType = new AreaType(
              rs.getInt("area_type_id"), 
              rs.getString("area_type_name"));
            
            b.setAreaType(areaType);
          } 

          // Add building count if it exists in database
          if (rs.getObject("building_count") != null) {
            b.setBuildingCount(rs.getInt("building_count"));
          }

        return b;
      }, id);
      return Optional.of(buildingCount);

    } catch (Exception e) {
      return Optional.empty();
    }
  }

 /*  public List<BuildingCountEntity> findByType(int type_id) {
    System.out.println("Repository: Start fetching regions by type...");

    String sql = "SELECT r.id, r.name, rt.id as type_id, rt.name as type_name " +
                 "FROM region r " +
                 "LEFT JOIN region_type rt ON r.type_id = rt.id " +
                 "WHERE r.type_id = ?";

    try {
            List<BuildingCountEntity> regions = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Region r = new Region(
                  rs.getString("id"), 
                  rs.getString("name"), 
                  null);
                
                // Create RegionType if it exists
                if (rs.getObject("type_id") != null) {
                    RegionType regionType = new RegionType(
                      rs.getInt("type_id"), 
                      rs.getString("type_name"));

                    r.setRegionType(regionType);
                }
                
                return r;
            }, type_id);
            return regions;
        } catch (Exception e) {
            return new ArrayList<Region>();
        }

  } */
}
