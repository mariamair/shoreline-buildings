package com.buildings.service;

import com.buildings.domain.BuildingCountEntity;
import com.buildings.dto.BuildingCountFilterDto;
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

  public List<BuildingCountEntity> getBuildingCountEntitiesByRegion(String regionCode, Integer limit, Integer offset) {
    List<BuildingCountEntity> buildingCountEntities = buildingCountRepository.findBuildingCountEntitiesByRegion(regionCode, limit, offset);

    if (buildingCountEntities.isEmpty()) {
      throw new EntityNotFoundException(String.format("Found no building count entity with region code '%s'", regionCode));
    }
     
    return buildingCountEntities;
  }

  public List<BuildingCountEntity> getAllBuildingCountEntities(BuildingCountFilterDto filter, Integer limit, Integer offset) {
    return buildingCountRepository.findAllBuildingCountEntities(filter, limit, offset);
  }

  public int getTotalCount(BuildingCountFilterDto filter) {
    return buildingCountRepository.countAllBuildingCountEntities(filter);
  }
}
