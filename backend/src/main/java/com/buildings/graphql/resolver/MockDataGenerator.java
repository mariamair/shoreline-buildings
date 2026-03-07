package com.buildings.graphql.resolver;

import com.buildings.dto.*;
import java.util.ArrayList;
import java.util.List;

public class MockDataGenerator {
  public RegionDto generateRegion(String id) {
    return new RegionDto(id, "Stratford-upon-Avon", new RegionTypeDto(3, "kommun"));
  }

  public List<RegionDto> generateRegions(Integer limit, Integer offset) {
    List<RegionDto> regions = new ArrayList<>();
    regions.add(new RegionDto("01", "Stockholms län", new RegionTypeDto(2, "län")));
    regions.add(new RegionDto("22", "Västernorrlands län", new RegionTypeDto(2, "län")));
    regions.add(new RegionDto("1980", "Västerås", new RegionTypeDto(3, "kommun")));
    regions.add(new RegionDto("2518", "Övertorneå", new RegionTypeDto(3, "kommun")));
    regions.add(new RegionDto("00", "Riket", new RegionTypeDto(1, "sverige")));

    while(regions.size() > limit) {
      regions.removeLast();
    }

    return regions;
  }

  public List<BuildingCountEntityDto> generateBuildingCountEntities(Integer limit, Integer offset) {
    List<BuildingCountEntityDto> results = new ArrayList<>();
    
    List<RegionDto> regions = new ArrayList<>();
    regions.add(new RegionDto("01", "Stockholms län", new RegionTypeDto(2, "län")));
    regions.add(new RegionDto("22", "Västernorrlands län", new RegionTypeDto(2, "län")));
    regions.add(new RegionDto("1980", "Västerås", new RegionTypeDto(3, "kommun")));
    regions.add(new RegionDto("2518", "Övertorneå", new RegionTypeDto(3, "kommun")));
    regions.add(new RegionDto("00", "Riket", new RegionTypeDto(1, "sverige")));
    
    String[] buildingTypes = {"industri", "samhällsfunktion", "bostad", "komplementbyggnad"};
    String[] shorelineTypes = {"totalt", "hav", "inlandsvatten"};
    String[] areaTypes = {"totalt", "inom tätort", "inom formellt skyddad natur"};
    
    int totalCount = 200;
    int startIndex = Math.min(offset, totalCount);
    int endIndex = Math.min(offset + limit, totalCount);
        
    for (int i = startIndex; i < endIndex; i++) {
      BuildingCountEntityDto record = new BuildingCountEntityDto();
      
      record.setId(i);
      record.setRegion(regions.get(i % regions.size()));
      record.setBuildingType(new BuildingTypeDto(i, buildingTypes[i % buildingTypes.length]));
      record.setShorelineType(new ShorelineTypeDto(i, shorelineTypes[i % shorelineTypes.length]));
      record.setAreaType(new AreaTypeDto(i, areaTypes[i % areaTypes.length]));
      record.setYear(2020 + (i % 4));
      record.setBuildingCount((i * 10) + 5);
      
      results.add(record);
    }
    
    return results;
  }
}
