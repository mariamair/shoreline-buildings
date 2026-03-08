package com.buildings.graphql.resolver;

import com.buildings.dto.*;
import com.buildings.service.BuildingCountService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

/**
 * GraphQL resolver for ShorelineBuildingCount queries.
 */
@Controller
public class BuildingCountEntityResolver {
  private final BuildingCountService buildingCountService;

  public BuildingCountEntityResolver(BuildingCountService buildingCountService) {
    this.buildingCountService = buildingCountService;
  }

  @QueryMapping
  public BuildingCountEntityDto buildingCountEntity(@Argument Long id) {
    return buildingCountService.getBuildingCountById(id);
  }

  @QueryMapping
  public List<BuildingCountEntityDto> buildingCountEntities(
    @Argument Integer limit,
    @Argument Integer offset) {
    // Set defaults if null
    if (limit == null) {
        limit = 50;
    }
    if (offset == null) {
        offset = 0;
    }

    MockDataGenerator mockDataGenerator = new MockDataGenerator();
    return mockDataGenerator.generateBuildingCountEntities(limit, offset);
    // return buildingCountService.getBuildingCountEntities(limit, offset);
  }

  @QueryMapping
    public List<BuildingCountEntityDto> buildingCountEntitiesPerRegion(
      @Argument int regionTypeId,
      @Argument Integer limit,
      @Argument Integer offset) {
        // Set defaults if null
        if (limit == null) {
            limit = 50;
        }
        if (offset == null) {
            offset = 0;
        }

        if (regionTypeId < 0 || regionTypeId > 2) {
          // throw new Exception("Invalid region type");
          System.out.println("*** Invalid region type ***");
        }

        MockDataGenerator mockDataGenerator = new MockDataGenerator();
        return mockDataGenerator.generateBuildingCountEntities(limit, offset);
      }
}
