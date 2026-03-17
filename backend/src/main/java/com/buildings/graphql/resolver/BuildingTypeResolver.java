package com.buildings.graphql.resolver;

import com.buildings.domain.BuildingType;
import com.buildings.service.BuildingTypeService;

import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * GraphQL resolver for Building Type queries.
 */
@Controller
public class BuildingTypeResolver {
  private final BuildingTypeService buildingTypeService;

  public BuildingTypeResolver(final BuildingTypeService buildingTypeService) {
    this.buildingTypeService = buildingTypeService;
  }

  @QueryMapping
  public BuildingType buildingType(@Argument final Integer id) {
    return buildingTypeService.getBuildingTypeById(id);
  }

  @QueryMapping
  public List<BuildingType> buildingTypes() {
    return buildingTypeService.getBuildingTypes();
  }
}
