package com.buildings.service;

import com.buildings.domain.RegionType;
import com.buildings.repository.RegionTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegionTypeService {
  public final RegionTypeRepository regionTypeRepository;

  public RegionTypeService(RegionTypeRepository regionTypeRepository) {
    this.regionTypeRepository = regionTypeRepository;
  }

  public List<Integer> getRegionTypeIds() {
    List<Integer> regionTypeIds = regionTypeRepository.findAllRegionTypeIds();
    if (regionTypeIds.isEmpty()) {
      log.warn("No region types available");
      throw new EntityNotFoundException("Found no region type ids");
    }
    return regionTypeIds;
  }

  public Map<String, RegionType> getRegionTypesByRegionCodes(List<String> regionCodes) {
    return regionTypeRepository.findRegionTypesByRegionCodes(regionCodes);
  }
}
