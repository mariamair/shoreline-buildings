package com.buildings.repository;

import com.buildings.domain.BuildingCountEntity;
import com.buildings.dto.BuildingCountFilterDto;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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

  public List<Integer> getYears() {
    return jdbcClient.sql("SELECT DISTINCT year FROM building_count")
      .query((rs, _) -> rs.getInt("year"))
      .list();
  }

  public Optional<BuildingCountEntity> findBuildingCountEntityById(Long id) {
    return jdbcClient.sql(BASE_SQL + " WHERE id = :id")
      .param("id", id)
      .query(mapRowsToEntity())
      .optional();
  }

  public List<BuildingCountEntity> findBuildingCountEntities(BuildingCountFilterDto filter, int limit, int offset) {
    FilterQuery filterQuery = buildFilterQuery(filter);
    String sql = BASE_SQL 
      + filterQuery.sql() 
      + " ORDER BY id ASC LIMIT :limit OFFSET :offset";
    
    filterQuery.params()
      .addValue("limit", limit)
      .addValue("offset", offset);

    return jdbcClient.sql(sql)
      .paramSource(filterQuery.params())
      .query(mapRowsToEntity())
      .list();
  }

  public int countBuildingCountEntities(BuildingCountFilterDto filter) {
    FilterQuery filterQuery = buildFilterQuery(filter);
    String sql = "SELECT COUNT(*) FROM building_count" 
      + filterQuery.sql();

    return jdbcClient.sql(sql)
      .paramSource(filterQuery.params())
      .query(Integer.class)
      .single();
  }

  private record FilterQuery(String sql, MapSqlParameterSource params) {}

  private FilterQuery buildFilterQuery(BuildingCountFilterDto filter) {
    StringBuilder sql = new StringBuilder(" WHERE 1=1");
    MapSqlParameterSource params = new MapSqlParameterSource();

    if (filter != null && filter.getRegionCode() != null) {
      sql.append(" AND region_code = :regionCode");
      params.addValue("regionCode", filter.getRegionCode());
    }

    if (filter != null && filter.getAreaTypeId() != null) {
      sql.append(" AND area_type_id = :areaTypeId");
      params.addValue("areaTypeId", filter.getAreaTypeId());
    }

    if (filter != null && filter.getBuildingTypeId() != null) {
      sql.append(" AND building_type_id = :buildingTypeId");
      params.addValue("buildingTypeId", filter.getBuildingTypeId());
    }

    if (filter != null && filter.getShorelineTypeId() != null) {
      sql.append(" AND shoreline_type_id = :shorelineTypeId");
      params.addValue("shorelineTypeId", filter.getShorelineTypeId());
    }
    return new FilterQuery(sql.toString(), params);
  }

  private RowMapper<BuildingCountEntity> mapRowsToEntity() {
    return (rs, _) -> new BuildingCountEntity(
      rs.getLong("id"), 
      rs.getInt("year"), 
      rs.getInt("count")
    );
  }
}
