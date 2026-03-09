package com.buildings.repository;

import com.buildings.domain.BuildingType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class BuildingTypeRepository {
  private final JdbcClient jdbcClient;

  public BuildingTypeRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public List<Integer> findAllBuildingTypeIds() {
    return jdbcClient.sql("SELECT id FROM building type")
      .query((rs, _) -> rs.getInt("id"))
      .list();
  }

  public Map<Long, BuildingType> findBuildingTypeByBuildingCountIds(List<Long> buildingCountIds) {
    return jdbcClient.sql(
      "SELECT b.id AS building_count_id, bt.id, bt.name " +
      "FROM building_type bt " +
      "JOIN building_count b ON b.building_type_id = bt.id " +
      "WHERE b.id IN (:ids)")
        .param("ids", buildingCountIds)
        .query((rs, _) -> Map.entry(
            rs.getLong("building_count_id"),
            new BuildingType(
              rs.getInt("id"), 
              rs.getString("name"))
        ))
        .list()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
