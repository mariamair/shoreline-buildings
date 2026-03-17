package com.buildings.service;

import com.buildings.domain.BuildingType;
import com.buildings.repository.BuildingTypeRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class BuildingTypeService {
  private final BuildingTypeRepository buildingTypeRepository;

  public BuildingTypeService(final BuildingTypeRepository buildingTypeRepository) {
    this.buildingTypeRepository = buildingTypeRepository;
  }

  public List<Integer> getBuildingTypeIds() {
    List<Integer> buildingTypeIds = buildingTypeRepository.findBuildingTypeIds();
    return buildingTypeIds;
  }

  public BuildingType getBuildingTypeById(final Integer id) {
    return buildingTypeRepository.findBuildingTypeById(id)
      .orElseThrow(() -> new EntityNotFoundException(String.format("Found no building type with id '%d'", id)));
  }

  public List<BuildingType> getBuildingTypes() {
    return buildingTypeRepository.findBuildingTypes();
  }

  public Map<Long, BuildingType> getBuildingTypesByBuildingCountIds(final List<Long> buildingCountIds) {
    return buildingTypeRepository.findBuildingTypesByBuildingCountIds(buildingCountIds);
  }
}
