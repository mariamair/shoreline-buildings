package com.buildings.service;

import com.buildings.domain.BuildingCountEntity;
import com.buildings.domain.Region;
import com.buildings.dto.*;
import com.buildings.repository.BuildingCountRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BuildingCountService {
  private final BuildingCountRepository buildingCountRepository;
  private final RegionTypeService regionTypeService;

  public BuildingCountService(BuildingCountRepository buildingCountRepository, RegionTypeService regionTypeService) {
        this.buildingCountRepository = buildingCountRepository;
        this.regionTypeService = regionTypeService;
    }

  public BuildingCountEntityDto getBuildingCountById(Long id) {
    log.info("Service: Fetching building count entity with id " + id + "...");

    BuildingCountEntity buildingCount = buildingCountRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Found no building count entity with id " + id));

    return convertToDto(buildingCount);
  }
/* 
  public List<BuildingCountDto> getBuildingCountsByRegionType(int regionTypeId) {
    log.debug("Service: Fetching building count entities by region type...");

    if (!isValidRegionTypeId(regionTypeId)) {
      throw new IllegalArgumentException("Invalid region type");
    }

    List<BuildingCount> buildingCounts = buildingCountRepository.findByRegionType(regionTypeId);

    List<BuildingCountDto> buildingCountDtos = new ArrayList<>();
    for (BuildingCount b : buildingCounts) {
      buildingCountDtos.add(convertToDto(b));
    }
    return buildingCountDtos;
  } */

  // Helper methods
  private BuildingCountEntityDto convertToDto(BuildingCountEntity buildingCount) {
      /* RegionTypeDto regionTypeDto = new RegionTypeDto(
      region.getRegionType().getId(), 
      region.getRegionType().getName());*/

    RegionDto regionDto = new RegionDto(
      buildingCount.getRegion().getCode(), 
      buildingCount.getRegion().getName(), 
      null);
    
    BuildingTypeDto buildingTypeDto = new BuildingTypeDto(
      buildingCount.getBuildingType().getId(),
      buildingCount.getBuildingType().getName()
    );
    
    ShorelineTypeDto shorelineTypeDto = new ShorelineTypeDto(
      buildingCount.getShorelineType().getId(),
      buildingCount.getShorelineType().getName()
    );
    
    AreaTypeDto areaTypeDto = new AreaTypeDto(
      buildingCount.getAreaType().getId(),
      buildingCount.getAreaType().getName()
    );

    BuildingCountEntityDto buildingCountDto = new BuildingCountEntityDto(
      buildingCount.getId(), 
      regionDto,
      buildingTypeDto,
      shorelineTypeDto,
      areaTypeDto,
      buildingCount.getYear(), 
      buildingCount.getBuildingCount());
    return buildingCountDto;
  }

  private boolean isValidRegionTypeId(int regionTypeId) {
    List<Integer> regionTypeIds = regionTypeService.getRegionTypeIds();
    return regionTypeIds.contains(regionTypeId);
  }
}
