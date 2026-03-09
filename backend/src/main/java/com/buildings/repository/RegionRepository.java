package com.buildings.repository;

import com.buildings.domain.Region;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RegionRepository {
  private final JdbcClient jdbcClient;

  public RegionRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

  public List<String> findAllRegionCodes() {
    return jdbcClient.sql("SELECT code FROM region")
      .query((rs, _) -> rs.getString("code"))
      .list();
  }

    public Optional<Region> findRegionByCode(String code) {
    return jdbcClient.sql("""
      SELECT code, name, type_id 
      FROM region 
      WHERE code = :code
      """)
      .param("code", code)
      .query((rs, _) -> new Region(
        rs.getString("code"), 
        rs.getString("name"), 
        rs.getInt("type_id")
      ))
      .optional();
  }

  public List<Region> findRegionsByType(int regionTypeId, int limit, int offset) {
    return jdbcClient.sql("""
      SELECT code, name, type_id 
      FROM region 
      WHERE type_id = :regionTypeId
      LIMIT :limit
      OFFSET :offset
      """)
      .param("regionTypeId", regionTypeId)
      .param("limit", limit)
      .param("offset", offset)
      .query((rs, _) -> new Region(
        rs.getString("code"), 
        rs.getString("name"), 
        rs.getInt("type_id")
      ))
      .list();
  }

  public Map<Long, Region> findRegionsByBuildingCountIds(List<Long> buildingCountIds) {
    return jdbcClient.sql("""
      SELECT b.id AS building_count_id, r.code, r.name, r.type_id 
      FROM region r 
      JOIN building_count b ON b.region_code = r.code 
      WHERE b.id IN (:ids)
      """)
      .param("ids", buildingCountIds)
      .query((rs, _) -> Map.entry(
        rs.getLong("building_count_id"),
        new Region(
          rs.getString("code"), 
          rs.getString("name"),
          rs.getInt("type_id"))
      ))
      .list()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
