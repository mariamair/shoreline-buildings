package com.buildings.repository;

import com.buildings.domain.Region;
import com.buildings.domain.RegionType;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RegionRepository {
  private final JdbcTemplate jdbcTemplate;

    public RegionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

  // Fetch a region with its region type. Use LEFT JOIN to fetch a region even if it doesn't have a region type.
  public Optional<Region> findById(String id) {
    System.out.println("Repository: Start fetching region...");

    String sql = "SELECT r.id, r.name, rt.id as type_id, rt.name as type_name " +
                 "FROM region r " +
                 "LEFT JOIN region_type rt ON r.type_id = rt.id " +
                 "WHERE r.id = ?";

    try {
            Region region = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
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
            }, id);
            return Optional.of(region);
        } catch (Exception e) {
            return Optional.empty();
        }

  }

  public List<Region> findByType(int type_id) {
    System.out.println("Repository: Start fetching regions by type...");

    String sql = "SELECT r.id, r.name, rt.id as type_id, rt.name as type_name " +
                 "FROM region r " +
                 "LEFT JOIN region_type rt ON r.type_id = rt.id " +
                 "WHERE r.type_id = ?";

    try {
            List<Region> regions = jdbcTemplate.query(sql, (rs, rowNum) -> {
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

  }
}
