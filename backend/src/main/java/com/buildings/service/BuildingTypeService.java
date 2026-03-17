package com.buildings.service;

import com.buildings.domain.BuildingType;
import com.buildings.repository.BuildingTypeRepository;
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

  public Map<Long, BuildingType> getBuildingTypesByBuildingCountIds(final List<Long> buildingCountIds) {
    return buildingTypeRepository.findBuildingTypesByBuildingCountIds(buildingCountIds);
  }
}
