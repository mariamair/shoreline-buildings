package com.buildings.service;

import com.buildings.domain.BuildingCountEntity;
import com.buildings.repository.BuildingCountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BuildingCountService {
  private final BuildingCountRepository buildingCountRepository;

  public BuildingCountService(BuildingCountRepository buildingCountRepository) {
        this.buildingCountRepository = buildingCountRepository;
    }

  public BuildingCountEntity getBuildingCountEntityById(Long id) {
    return buildingCountRepository.findBuildingCountEntityById(id)
      .orElseThrow(() -> new EntityNotFoundException(String.format("Found no building count entity with id '%d'", id)));
  }

  public List<BuildingCountEntity> getBuildingCountEntitiesByAreaType(int areaTypeId, Integer limit, Integer offset) {
    List<BuildingCountEntity> buildingCountEntities = buildingCountRepository.findBuildingCountEntitiesByAreaType(areaTypeId, limit, offset);

    if (buildingCountEntities.isEmpty()) {
      throw new EntityNotFoundException(String.format("Found no building count entity with area type id '%d'", areaTypeId));
    }

    return buildingCountEntities;
  }

  public List<BuildingCountEntity> getBuildingCountEntitiesByBuildingType(int buildingTypeId, Integer limit, Integer offset) {
    List<BuildingCountEntity> buildingCountEntities = buildingCountRepository.findBuildingCountEntitiesByBuildingType(buildingTypeId, limit, offset);

    if (buildingCountEntities.isEmpty()) {
      throw new EntityNotFoundException(String.format("Found no building count entity with building type id '%d'", buildingTypeId));
    }
     
    return buildingCountEntities;
  }

  public List<BuildingCountEntity> getBuildingCountEntitiesByShorelineType(int shorelineTypeId, Integer limit, Integer offset) {
    List<BuildingCountEntity> buildingCountEntities = buildingCountRepository.findBuildingCountEntitiesByShorelineType(shorelineTypeId, limit, offset);

    if (buildingCountEntities.isEmpty()) {
      throw new EntityNotFoundException(String.format("Found no building count entity with shoreline type id '%d'", shorelineTypeId));
    }
     
    return buildingCountEntities;
  }

  public List<BuildingCountEntity> getBuildingCountEntitiesByRegion(String regionCode, Integer limit, Integer offset) {
    List<BuildingCountEntity> buildingCountEntities = buildingCountRepository.findBuildingCountEntitiesByRegion(regionCode, limit, offset);

    if (buildingCountEntities.isEmpty()) {
      throw new EntityNotFoundException(String.format("Found no building count entity with region code '%s'", regionCode));
    }
     
    return buildingCountEntities;
  }

  public List<BuildingCountEntity> getAllBuildingCountEntities(Integer limit, Integer offset) {
    return buildingCountRepository.findAllBuildingCountEntities(limit, offset);
  }
}
