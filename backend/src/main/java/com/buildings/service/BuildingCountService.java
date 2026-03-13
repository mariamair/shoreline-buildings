package com.buildings.service;

import com.buildings.domain.BuildingCountEntity;
import com.buildings.dto.BuildingCountFilterDto;
import com.buildings.repository.BuildingCountRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BuildingCountService {
  private final BuildingCountRepository buildingCountRepository;
  private final RegionService regionService;
  private final AreaTypeService areaTypeService;
  private final BuildingTypeService buildingTypeService;
  private final ShorelineTypeService shorelineTypeService;

  public BuildingCountService(BuildingCountRepository buildingCountRepository, RegionService regionService, AreaTypeService areaTypeService, BuildingTypeService buildingTypeService, ShorelineTypeService shorelineTypeService) {
        this.buildingCountRepository = buildingCountRepository;
        this.regionService = regionService;
        this.areaTypeService = areaTypeService;
        this.buildingTypeService = buildingTypeService;
        this.shorelineTypeService = shorelineTypeService;
    }

  public BuildingCountEntity getBuildingCountEntityById(Long id) {
    return buildingCountRepository.findBuildingCountEntityById(id)
      .orElseThrow(() -> new EntityNotFoundException(String.format("Found no building count entity with id '%d'", id)));
  }

  public List<BuildingCountEntity> getAllBuildingCountEntities(BuildingCountFilterDto filter, Integer limit, Integer offset) {
    if (filter != null) {
      validateFilter(filter);
    }
    return buildingCountRepository.findAllBuildingCountEntities(filter, limit, offset);
  }

  public int getTotalCount(BuildingCountFilterDto filter) {
    return buildingCountRepository.countAllBuildingCountEntities(filter);
  }

  private void validateFilter(BuildingCountFilterDto filter) {
    List<String> errors = new ArrayList<>();

    if (filter.getRegionCode() != null) {
      String regionCode = filter.getRegionCode();
      if (!regionService.getRegionCodes().contains(regionCode)) {
        errors.add(String.format("%s' is not a valid region code", regionCode));
      }
    }

    if (filter.getAreaTypeId() != null) {
      int areaTypeId = filter.getAreaTypeId();
      if (!areaTypeService.getAreaTypeIds().contains(areaTypeId)) {
        errors.add(String.format("'%d' is not a valid area type", areaTypeId));
      }
    }
    
    if (filter.getBuildingTypeId() != null) {
      int buildingTypeId = filter.getBuildingTypeId();
      if (!buildingTypeService.getBuildingTypeIds().contains(buildingTypeId)) {
        errors.add(String.format("'%d' is not a valid building type", buildingTypeId));
      }
    }

    if (filter.getShorelineTypeId() != null) {
      int shorelineTypeId = filter.getShorelineTypeId();
      if (!shorelineTypeService.getShorelineTypeIds().contains(shorelineTypeId)) {
        errors.add(String.format("'%d' is not a valid shoreline type", shorelineTypeId));
      }
    }

    if (!errors.isEmpty()) {
      throw new IllegalArgumentException("Invalid filter value(s): " +  String.join("; ", errors));
    }
  }
}
