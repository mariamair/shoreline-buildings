package com.buildings.graphql.resolver;

import com.buildings.domain.*;
import com.buildings.dto.BuildingCountPageDto;
import com.buildings.dto.CreateBuildingCountEntityDto;
import com.buildings.dto.BuildingCountFilterDto;
import com.buildings.service.*;

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
    BuildingCountService buildingCountService, 
    RegionService regionService, 
    AreaTypeService areaTypeService, 
    BuildingTypeService buildingTypeService, 
    ShorelineTypeService shorelineTypeService) {
    this.buildingCountService = buildingCountService;
    this.regionService = regionService;
    this.areaTypeService = areaTypeService;
    this.buildingTypeService = buildingTypeService;
    this.shorelineTypeService = shorelineTypeService;
  }

  @QueryMapping
  public BuildingCountEntity buildingCountEntity(@Argument Long id) {
    return buildingCountService.getBuildingCountEntityById(id);
  }

  @QueryMapping
  public BuildingCountPageDto buildingCountEntities(
    @Argument BuildingCountFilterDto filter,
    @Argument Integer limit, 
    @Argument Integer offset,
    DataFetchingEnvironment env) {

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
  public Map<BuildingCountEntity, Region> region(List<BuildingCountEntity> buildingCountEntities) {
    List<Long> ids = buildingCountEntities.stream().map(BuildingCountEntity::getId).toList();
    Map<Long, Region> regionMap = regionService.getRegionsByBuildingCountIds(ids);

    return buildingCountEntities.stream()
      .collect(Collectors.toMap(
        b -> b,
        b -> regionMap.getOrDefault(b.getId(), null)
      ));
  }

  @BatchMapping(typeName = "BuildingCountEntity", field = "areaType")
  public Map<BuildingCountEntity, AreaType> areaType(List<BuildingCountEntity> buildingCountEntities) {
    List<Long> ids = buildingCountEntities.stream().map(BuildingCountEntity::getId).toList();
    Map<Long, AreaType> areaTypeMap = areaTypeService.getAreaTypesByBuildingCountIds(ids);

    return buildingCountEntities.stream()
      .collect(Collectors.toMap(
        b -> b,
        b -> areaTypeMap.getOrDefault(b.getId(), null)
      ));
  }

  @BatchMapping(typeName = "BuildingCountEntity", field = "buildingType")
  public Map<BuildingCountEntity, BuildingType> buildingType(List<BuildingCountEntity> buildingCountEntities) {
    List<Long> ids = buildingCountEntities.stream().map(BuildingCountEntity::getId).toList();
    Map<Long, BuildingType> buildingTypeMap = buildingTypeService.getBuildingTypesByBuildingCountIds(ids);

    return buildingCountEntities.stream()
      .collect(Collectors.toMap(
        b -> b,
        b -> buildingTypeMap.getOrDefault(b.getId(), null)
      ));
  }

  @BatchMapping(typeName = "BuildingCountEntity", field = "shorelineType")
  public Map<BuildingCountEntity, ShorelineType> shorelineType(List<BuildingCountEntity> buildingCountEntities) {
    List<Long> ids = buildingCountEntities.stream().map(BuildingCountEntity::getId).toList();
    Map<Long, ShorelineType> shorelineTypeMap = shorelineTypeService.getShorelineTypesByBuildingCountIds(ids);

    return buildingCountEntities.stream()
      .collect(Collectors.toMap(
        b -> b,
        b -> shorelineTypeMap.getOrDefault(b.getId(), null)
      ));
  }

  @MutationMapping
  public BuildingCountEntity createBuildingCountEntity(
    @Argument CreateBuildingCountEntityDto input) {
    return buildingCountService.createBuildingCountEntity(input);
  }
}
