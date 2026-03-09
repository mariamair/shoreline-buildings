package com.buildings.repository;

import com.buildings.domain.ShorelineType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class ShorelineTypeRepository {
  private final JdbcClient jdbcClient;

  public ShorelineTypeRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public List<Integer> findAllShorelineTypeIds() {
    return jdbcClient.sql("SELECT id FROM shoreline_type")
      .query((rs, _) -> rs.getInt("id"))
      .list();
  }

  public Map<Long, ShorelineType> findShorelineTypesByBuildingCountIds(List<Long> buildingCountIds) {
    return jdbcClient.sql("""
      SELECT b.id AS building_count_id, st.id, st.name 
      FROM shoreline_type st 
      JOIN building_count b ON b.shoreline_type_id = st.id 
      WHERE b.id IN (:ids)
      """)
      .param("ids", buildingCountIds)
      .query((rs, _) -> Map.entry(
        rs.getLong("building_count_id"),
        new ShorelineType(
          rs.getInt("id"), 
          rs.getString("name"))
      ))
      .list()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
