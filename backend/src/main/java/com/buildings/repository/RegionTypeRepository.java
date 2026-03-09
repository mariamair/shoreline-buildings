package com.buildings.repository;

import com.buildings.domain.RegionType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class RegionTypeRepository {
    private final JdbcClient jdbcClient;

  public RegionTypeRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public List<Integer> findAllRegionTypeIds() {
    return jdbcClient.sql("SELECT id FROM region_type")
      .query((rs, _) -> rs.getInt("id"))
      .list();
  }

  public Map<String, RegionType> findRegionTypeByRegionIds(List<String> regionIds) {
    return jdbcClient.sql(
      "SELECT r.id AS region_id, rt.id, rt.name " +
      "FROM region_type rt " +
      "JOIN region r ON r.type_id = rt.id " +
      "WHERE r.id IN (:ids)")
        .param("ids", regionIds)
        .query((rs, _) -> Map.entry(
            rs.getString("region_id"),
            new RegionType(
              rs.getInt("id"), 
              rs.getString("name"))
        ))
        .list()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
