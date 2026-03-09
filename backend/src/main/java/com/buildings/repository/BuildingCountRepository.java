package com.buildings.repository;

import com.buildings.domain.BuildingCountEntity;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class BuildingCountRepository {
  private final JdbcClient jdbcClient;
  private final String BASE_SQL = """
      SELECT 
        id, 
        year, 
        count 
      FROM building_count 
      """;

  public BuildingCountRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public Optional<BuildingCountEntity> findBuildingCountEntityById(Long id) {
    return jdbcClient.sql(BASE_SQL + "WHERE id = :id")
      .param("id", id)
      .query((rs, _) -> new BuildingCountEntity(
        rs.getLong("id"), 
        rs.getInt("year"),
        rs.getInt("count")
      ))
      .optional();
  }

  public List<BuildingCountEntity> findBuildingCountEntitiesByAreaType(int areaTypeId, int limit, int offset) {
    return jdbcClient.sql(BASE_SQL + "WHERE area_type_id = :areaTypeId LIMIT :limit OFFSET :offset")
      .param("areaTypeId", areaTypeId)
      .param("limit", limit)
      .param("offset", offset)
      .query(mapRowsToEntity())
      .list();
  }

  public List<BuildingCountEntity> findBuildingCountEntitiesByBuildingType(int buildingTypeId, int limit, int offset) {
    return jdbcClient.sql(BASE_SQL + "WHERE building_type_id = :buildingTypeId LIMIT :limit OFFSET :offset")
      .param("buildingTypeId", buildingTypeId)
      .param("limit", limit)
      .param("offset", offset)
      .query(mapRowsToEntity())
      .list();
  }

  public List<BuildingCountEntity> findBuildingCountEntitiesByShorelineType(int shorelineTypeId, int limit, int offset) {
    return jdbcClient.sql(BASE_SQL + "WHERE shoreline_type_id = :shorelineTypeId LIMIT :limit OFFSET :offset")
      .param("shorelineTypeId", shorelineTypeId)
      .param("limit", limit)
      .param("offset", offset)
      .query(mapRowsToEntity())
      .list();
  }

  public List<BuildingCountEntity> findBuildingCountEntitiesByRegion(String regionCode, int limit, int offset) {
    return jdbcClient.sql(BASE_SQL + "WHERE region_code = :regionCode LIMIT :limit OFFSET :offset")
      .param("regionCode", regionCode)
      .param("limit", limit)
      .param("offset", offset)
      .query(mapRowsToEntity())
      .list();
  }

  public List<BuildingCountEntity> findAllBuildingCountEntities(
    int limit, 
    int offset) {
      System.out.println("Repository: limit " + limit + " och offset " + offset);
    return jdbcClient.sql(BASE_SQL + "LIMIT :limit OFFSET :offset")
      .param("limit", limit)
      .param("offset", offset)
      .query(mapRowsToEntity())
      .list();
  }

  private RowMapper<BuildingCountEntity> mapRowsToEntity() {
    return (rs, _) -> new BuildingCountEntity(
        rs.getLong("id"), 
        rs.getInt("year"), 
        rs.getInt("count")
      );
  }
}
