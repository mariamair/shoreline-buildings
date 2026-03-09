package com.buildings.service;

import com.buildings.domain.AreaType;
import com.buildings.repository.AreaTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AreaTypeService {
  public final AreaTypeRepository areaTypeRepository;

  public AreaTypeService(AreaTypeRepository areaTypeRepository) {
    this.areaTypeRepository = areaTypeRepository;
  }

  public List<Integer> getAreaTypeIds() {
    List<Integer> areaTypeIds = areaTypeRepository.findAllAreaTypeIds();
    if (areaTypeIds.isEmpty()) {
      log.warn("No area types available");
      throw new EntityNotFoundException("Found no area type ids");
    }
    return areaTypeIds;
  }

  public Map<Long, AreaType> getAreaTypesByBuildingCountIds(List<Long> buildingCountIds) {
    return areaTypeRepository.findAreaTypesByBuildingCountIds(buildingCountIds);
  }
}
