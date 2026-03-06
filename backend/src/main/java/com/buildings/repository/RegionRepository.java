package com.buildings.repository;

import com.buildings.domain.Region;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class RegionRepository {
  private final JdbcTemplate jdbcTemplate;

  public RegionRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Region findById(String id) {
    String sql = "SELECT id, name, type_id FROM region WHERE id = ?";
    // Region region = new Region();
    Region region = jdbcTemplate.queryForObject(sql, Region.class, id);
    System.out.println("**** Fetched region: " + region.getName() + "****");
    return region;
  }

  /* 
  public List<Region> findByType(int regionType) {
    List<Region> regions = new ArrayList<>();

    return regions;
  }
    */
}
