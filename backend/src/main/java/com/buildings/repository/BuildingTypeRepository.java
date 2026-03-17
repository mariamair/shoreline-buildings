package com.buildings.repository;

import com.buildings.domain.BuildingType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class BuildingTypeRepository {
  private final JdbcClient jdbcClient;

  public BuildingTypeRepository(final JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public List<Integer> findBuildingTypeIds() {
    return jdbcClient.sql("SELECT id FROM building_type")
      .query((rs, _) -> rs.getInt("id"))
      .list();
  }

  public Optional<BuildingType> findBuildingTypeById(final Integer id) {
    return jdbcClient.sql("SELECT id, name FROM building_type WHERE id = :id")
      .param("id", id)
      .query((rs, _) -> new BuildingType(
        rs.getInt("id"),
        rs.getString("name")
      ))
      .optional();
  }

  public List<BuildingType> findBuildingTypes() {
    return jdbcClient.sql("SELECT id, name FROM building_type")
      .query((rs, _) -> new BuildingType(
        rs.getInt("id"),
        rs.getString("name")
      ))
      .list();
  }

  public Map<Long, BuildingType> findBuildingTypesByBuildingCountIds(final List<Long> buildingCountIds) {
    return jdbcClient.sql("""
      SELECT b.id AS building_count_id, bt.id, bt.name
      FROM building_type bt
      JOIN building_count b ON b.building_type_id = bt.id
      WHERE b.id IN (:ids)
      """)
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
