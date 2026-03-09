package com.buildings.graphql.resolver;

import com.buildings.domain.*;
import com.buildings.service.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Map;
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
  public List<BuildingCountEntity> buildingCountEntities(@Argument Integer limit, @Argument Integer offset) {
    return buildingCountService.getAllBuildingCountEntities(limit, offset);
  }

  @QueryMapping
  public List<BuildingCountEntity> buildingCountEntitiesPerAreaType(
    @Argument Integer areaType,
    @Argument Integer limit, 
    @Argument Integer offset) {
    return buildingCountService.getBuildingCountEntitiesByAreaType(areaType, limit, offset);
  }

  @QueryMapping
  public List<BuildingCountEntity> buildingCountEntitiesPerBuildingType(
    @Argument Integer buildingType,
    @Argument Integer limit, 
    @Argument Integer offset) {
    return buildingCountService.getBuildingCountEntitiesByBuildingType(buildingType, limit, offset);
  }

  @QueryMapping
  public List<BuildingCountEntity> buildingCountEntitiesPerShorelineType(
    @Argument Integer shorelineType,
    @Argument Integer limit, 
    @Argument Integer offset) {
    return buildingCountService.getBuildingCountEntitiesByShorelineType(shorelineType, limit, offset);
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
}
