package com.buildings.service;

import com.buildings.domain.BuildingType;
import com.buildings.repository.BuildingTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuildingTypeService {
  public final BuildingTypeRepository buildingTypeRepository;

  public BuildingTypeService(BuildingTypeRepository buildingTypeRepository) {
    this.buildingTypeRepository = buildingTypeRepository;
  }

  public List<Integer> getBuildingTypeIds() {
    List<Integer> buildingTypeIds = buildingTypeRepository.findAllBuildingTypeIds();
    if (buildingTypeIds.isEmpty()) {
      log.warn("No building types available");
      throw new EntityNotFoundException("Found no building type ids");
    }
    return buildingTypeIds;
  }

  public Map<Long, BuildingType> getBuildingTypesByBuildingCountIds(List<Long> buildingCountIds) {
    return buildingTypeRepository.findBuildingTypesByBuildingCountIds(buildingCountIds);
  }
}
