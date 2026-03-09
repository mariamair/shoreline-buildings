package com.buildings.service;

import com.buildings.domain.ShorelineType;
import com.buildings.repository.ShorelineTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShorelineTypeService {
  public final ShorelineTypeRepository shorelineTypeRepository;

  public ShorelineTypeService(ShorelineTypeRepository shorelineTypeRepository) {
    this.shorelineTypeRepository = shorelineTypeRepository;
  }

  public List<Integer> getShorelineTypeIds() {
    List<Integer> shorelineTypeIds = shorelineTypeRepository.findAllShorelineTypeIds();
    if (shorelineTypeIds.isEmpty()) {
      log.warn("No shoreline types available");
      throw new EntityNotFoundException("Found no shoreline type ids");
    }
    return shorelineTypeIds;
  }

  public Map<Long, ShorelineType> getShorelineTypesByBuildingCountIds(List<Long> buildingCountIds) {
    return shorelineTypeRepository.findShorelineTypesByBuildingCountIds(buildingCountIds);
  }
}
