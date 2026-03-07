package com.buildings.graphql.resolver;

import com.buildings.dto.*;
import com.buildings.service.RegionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

/**
 * GraphQL resolver for Region queries.
 */
@Controller
public class RegionResolver {
  private final RegionService regionService;

  public RegionResolver(RegionService regionService) {
        this.regionService = regionService;
    }

  @QueryMapping
  public RegionDto region(@Argument String id) {
    // MockDataGenerator mockDataGenerator = new MockDataGenerator();
    // return mockDataGenerator.generateRegion(id);
    return regionService.getRegionById(id);
  }

  @QueryMapping
  public List<RegionDto> regions(
    @Argument Integer regionType, 
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
    return mockDataGenerator.generateRegions(limit, offset);
  }
}
