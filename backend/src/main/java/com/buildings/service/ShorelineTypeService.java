package com.buildings.service;

import com.buildings.domain.ShorelineType;
import com.buildings.repository.ShorelineTypeRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ShorelineTypeService {
  private final ShorelineTypeRepository shorelineTypeRepository;

  public ShorelineTypeService(final ShorelineTypeRepository shorelineTypeRepository) {
    this.shorelineTypeRepository = shorelineTypeRepository;
  }

  public List<Integer> getShorelineTypeIds() {
    List<Integer> shorelineTypeIds = shorelineTypeRepository.findShorelineTypeIds();
    return shorelineTypeIds;
  }

  public Map<Long, ShorelineType> getShorelineTypesByBuildingCountIds(final List<Long> buildingCountIds) {
    return shorelineTypeRepository.findShorelineTypesByBuildingCountIds(buildingCountIds);
  }
}
