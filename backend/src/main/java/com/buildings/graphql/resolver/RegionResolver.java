package com.buildings.graphql.resolver;

import com.buildings.domain.Region;
import com.buildings.domain.RegionType;
import com.buildings.dto.RegionPageDto;
import com.buildings.service.RegionService;
import com.buildings.service.RegionTypeService;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * GraphQL resolver for Region queries.
 */
@Controller
public class RegionResolver {
  private final RegionService regionService;
  private final RegionTypeService regionTypeService;

  public RegionResolver(RegionService regionService, RegionTypeService regionTypeService) {
        this.regionService = regionService;
        this.regionTypeService = regionTypeService;
    }

  @QueryMapping
  public Region region(@Argument String code) {
    return regionService.getRegionByCode(code);
  }

  @QueryMapping
  public RegionPageDto regionsByType(
    @Argument int regionTypeId, 
    @Argument Integer limit, 
    @Argument Integer offset,
    DataFetchingEnvironment env) {

    List<Region> items = regionService.getRegionsByType(regionTypeId, limit, offset);

    Set<String> requestedFields = env.getSelectionSet().getFields()
      .stream()
      .map(SelectedField::getName)
      .collect(Collectors.toSet());

    int totalCount = 0;
    boolean hasNextPage = false;    

    if (requestedFields.contains("totalCount") || requestedFields.contains("hasNextPage")) {
      totalCount = regionService.getTotalCount(regionTypeId);
      hasNextPage = offset + limit < totalCount;
    }

      return new RegionPageDto(items, totalCount, limit, offset, hasNextPage);
  }

  @BatchMapping(typeName = "Region", field = "regionType")
  public Map<Region, RegionType> regionType(List<Region> regions) {
    List<String> codes = regions.stream().map(Region::getCode).toList();
    Map<String, RegionType> regionTypeMap = regionTypeService.getRegionTypesByRegionCodes(codes);

    return regions.stream()
      .collect(Collectors.toMap(
          r -> r,
          r -> regionTypeMap.getOrDefault(r.getCode(), null)
      ));
    }
}
