package com.buildings.repository;

import com.buildings.domain.Region;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RegionRepository {
  private final JdbcClient jdbcClient;
  private final String BASE_SQL = """
      SELECT 
        code, 
        name, 
        type_id 
      FROM region
      """;


  public RegionRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

  public Optional<Region> findRegionByCode(String code) {
  return jdbcClient.sql(BASE_SQL + " WHERE code = :code")
    .param("code", code)
    .query(mapRowsToEntity())
    .optional();
  }


  public List<Region> findAllRegions(Integer regionTypeId, int limit, int offset) {
    FilterQuery filterQuery = buildFilterQuery(regionTypeId);
    String sql = BASE_SQL 
      + filterQuery.sql() 
      + " ORDER BY code ASC LIMIT :limit OFFSET :offset";
    
    filterQuery.params()
      .addValue("limit", limit)
      .addValue("offset", offset);

    return jdbcClient.sql(sql)
      .paramSource(filterQuery.params())
      .query(mapRowsToEntity())
      .list();
  }

  public int countAllRegions(Integer regionTypeId) {
    FilterQuery filterQuery = buildFilterQuery(regionTypeId);
    String sql = "SELECT COUNT(*) FROM region" 
      + filterQuery.sql();

    return jdbcClient.sql(sql)
    .param("regionTypeId", regionTypeId)
    .query(Integer.class)
    .single();
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

  private record FilterQuery(String sql, MapSqlParameterSource params) {}

  private FilterQuery buildFilterQuery(Integer regionTypeId) {
    StringBuilder sql = new StringBuilder(" ");
    MapSqlParameterSource params = new MapSqlParameterSource();

    if (regionTypeId != null) {
      sql.append("WHERE type_id = :regionTypeId");
      params.addValue("regionTypeId", regionTypeId);
    }

    return new FilterQuery(sql.toString(), params);
  }

  private RowMapper<Region> mapRowsToEntity() {
    return (rs, _) -> new Region(
      rs.getString("code"), 
      rs.getString("name"),
      rs.getInt("type_id")
    );
  }
}
