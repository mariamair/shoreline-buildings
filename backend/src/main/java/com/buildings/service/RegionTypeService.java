package com.buildings.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.buildings.repository.RegionTypeRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RegionTypeService {
  public final RegionTypeRepository regionTypeRepository;

  public RegionTypeService(RegionTypeRepository regionTypeRepository) {
    this.regionTypeRepository = regionTypeRepository;
  }

  public List<Integer> getRegionTypeIds() {
    List<Integer> regionTypeIds = regionTypeRepository.findAllRegionTypeIds();
    if (regionTypeIds.isEmpty()) {
      throw new EntityNotFoundException("Found no region type ids");
    }
    return regionTypeIds;
  }
}
