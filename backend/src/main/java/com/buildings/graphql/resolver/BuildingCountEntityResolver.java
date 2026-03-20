package com.buildings.graphql.resolver;

import com.buildings.auth.Authenticated;
import com.buildings.domain.AreaType;
import com.buildings.domain.BuildingCountEntity;
import com.buildings.domain.BuildingType;
import com.buildings.domain.Region;
import com.buildings.domain.ShorelineType;
import com.buildings.dto.BuildingCountPageDto;
import com.buildings.dto.BuildingCountContent;
import com.buildings.service.AreaTypeService;
import com.buildings.service.BuildingCountService;
import com.buildings.service.RegionService;
import com.buildings.service.BuildingTypeService;
import com.buildings.service.ShorelineTypeService;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GraphQL resolver for BuildingCount queries.
 */
@Controller
public class BuildingCountEntityResolver {
  private final BuildingCountService buildingCountService;
  private final RegionService regionService;
  private final AreaTypeService areaTypeService;
  private final BuildingTypeService buildingTypeService;
  private final ShorelineTypeService shorelineTypeService;

  public BuildingCountEntityResolver(
    final BuildingCountService buildingCountService,
    final RegionService regionService,
    final AreaTypeService areaTypeService,
    final BuildingTypeService buildingTypeService,
    final ShorelineTypeService shorelineTypeService) {
    this.buildingCountService = buildingCountService;
    this.regionService = regionService;
    this.areaTypeService = areaTypeService;
    this.buildingTypeService = buildingTypeService;
    this.shorelineTypeService = shorelineTypeService;
  }

  @QueryMapping
  public BuildingCountEntity buildingCountEntity(@Argument final Long id) {
    return buildingCountService.getBuildingCountEntityById(id);
  }

  @QueryMapping
  public BuildingCountPageDto buildingCountEntities(
      @Argument final BuildingCountContent filter,
      @Argument final Integer limit,
      @Argument final Integer offset,
      final DataFetchingEnvironment env) {

    List<BuildingCountEntity> items = buildingCountService.getBuildingCountEntities(filter, limit, offset);

    Set<String> requestedFields = env.getSelectionSet().getFields()
        .stream()
        .map(SelectedField::getName)
        .collect(Collectors.toSet());

    int totalCount = 0;
    boolean hasNextPage = false;

    if (requestedFields.contains("totalCount") || requestedFields.contains("hasNextPage")) {
      totalCount = buildingCountService.getTotalCount(filter);
      hasNextPage = offset + limit < totalCount;
    }

    return new BuildingCountPageDto(items, totalCount, limit, offset, hasNextPage);
  }

  @BatchMapping(typeName = "BuildingCountEntity", field = "region")
  public Map<BuildingCountEntity, Region> region(final List<BuildingCountEntity> buildingCountEntities) {
    List<Long> ids = buildingCountEntities.stream().map(BuildingCountEntity::getId).toList();
    Map<Long, Region> regionMap = regionService.getRegionsByBuildingCountIds(ids);

    return buildingCountEntities.stream()
        .collect(Collectors.toMap(
            b -> b,
            b -> regionMap.getOrDefault(b.getId(), null)));
  }

  @BatchMapping(typeName = "BuildingCountEntity", field = "areaType")
  public Map<BuildingCountEntity, AreaType> areaType(final List<BuildingCountEntity> buildingCountEntities) {
    List<Long> ids = buildingCountEntities.stream().map(BuildingCountEntity::getId).toList();
    Map<Long, AreaType> areaTypeMap = areaTypeService.getAreaTypesByBuildingCountIds(ids);

    return buildingCountEntities.stream()
        .collect(Collectors.toMap(
            b -> b,
            b -> areaTypeMap.getOrDefault(b.getId(), null)));
  }

  @BatchMapping(typeName = "BuildingCountEntity", field = "buildingType")
  public Map<BuildingCountEntity, BuildingType> buildingType(final List<BuildingCountEntity> buildingCountEntities) {
    List<Long> ids = buildingCountEntities.stream().map(BuildingCountEntity::getId).toList();
    Map<Long, BuildingType> buildingTypeMap = buildingTypeService.getBuildingTypesByBuildingCountIds(ids);

    return buildingCountEntities.stream()
        .collect(Collectors.toMap(
            b -> b,
            b -> buildingTypeMap.getOrDefault(b.getId(), null)));
  }

  @BatchMapping(typeName = "BuildingCountEntity", field = "shorelineType")
  public Map<BuildingCountEntity, ShorelineType> shorelineType(final List<BuildingCountEntity> buildingCountEntities) {
    List<Long> ids = buildingCountEntities.stream().map(BuildingCountEntity::getId).toList();
    Map<Long, ShorelineType> shorelineTypeMap = shorelineTypeService.getShorelineTypesByBuildingCountIds(ids);

    return buildingCountEntities.stream()
        .collect(Collectors.toMap(
            b -> b,
            b -> shorelineTypeMap.getOrDefault(b.getId(), null)));
  }

  @MutationMapping
  @Authenticated
  public BuildingCountEntity createBuildingCountEntity(
      @Argument final BuildingCountContent input) {
    return buildingCountService.createBuildingCountEntity(input);
  }

  @MutationMapping
  @Authenticated
  public BuildingCountEntity updateBuildingCountEntity(
      @Argument final Long id,
      @Argument final BuildingCountContent input) {
    return buildingCountService.updateBuildingCountEntity(id, input);
  }

  @MutationMapping
  @Authenticated
  public boolean deleteBuildingCountEntity(@Argument final Long id) {
    return buildingCountService.deleteBuildingCountEntity(id);
  }
}
