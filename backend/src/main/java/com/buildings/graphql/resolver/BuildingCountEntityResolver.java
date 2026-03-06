package com.buildings.graphql.resolver;

import com.buildings.dto.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

/**
 * GraphQL resolver for ShorelineBuildingCount queries.
 */
@Controller
public class BuildingCountEntityResolver {
  @QueryMapping
  public List<BuildingCountEntity> buildingCountEntities(
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
  }

  @QueryMapping
    public List<BuildingCountEntity> buildingCountEntitiesPerRegion(
      @Argument int regionType,
      @Argument Integer limit,
      @Argument Integer offset) {
        // Set defaults if null
        if (limit == null) {
            limit = 50;
        }
        if (offset == null) {
            offset = 0;
        }

        if (regionType < 0 || regionType > 2) {
          // throw new Exception("Invalid region type");
          System.out.println("*** Invalid region type ***");
        }

        MockDataGenerator mockDataGenerator = new MockDataGenerator();
        return mockDataGenerator.generateBuildingCountEntities(limit, offset);
      }
}
