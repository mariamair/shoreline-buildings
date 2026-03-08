package com.buildings.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RegionTypeRepository {
  private final JdbcTemplate jdbcTemplate;

  public RegionTypeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Integer> findAllRegionTypeIds() {
    String sql = "SELECT rt.id " +
                 "FROM region_type rt";

    try {
      List<Integer> regionTypeIds = jdbcTemplate.query(sql, (rs, rowNum) -> {
        int typeId = rs.getInt("id");
        return typeId;
        });
      return regionTypeIds;
    } catch (Exception e) {
      return new ArrayList<Integer>();
    }
  }
}
