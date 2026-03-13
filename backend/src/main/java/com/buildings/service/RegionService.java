package com.buildings.service;

import com.buildings.domain.Region;
import com.buildings.repository.RegionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegionService {
  private final RegionRepository regionRepository;

  public RegionService(RegionRepository regionRepository) {
    this.regionRepository = regionRepository;
    }
  
  public List<String> getRegionCodes() {
    List<String> regionCodes = regionRepository.findRegionCodes();
    if (regionCodes.isEmpty()) {
      log.warn("No region codes available");
      throw new EntityNotFoundException("Found no region codes");
    }
    return regionCodes;
  }

  public Region getRegionByCode(String code) {
    return regionRepository.findRegionByCode(code)
      .orElseThrow(() -> new EntityNotFoundException(String.format("Found no region with code '%s'", code)));
  }

  public List<Region> getRegions(Integer regionTypeId, Integer limit, Integer offset) {
    List<Region> regions = regionRepository.findRegions(regionTypeId, limit, offset);

    if (regions.isEmpty()) {
      throw new EntityNotFoundException(String.format("Found no regions with type id '%d'", regionTypeId));
    }

    return regions;
  }

  public int getTotalCount(Integer regionTypeId) {
    return regionRepository.countRegions(regionTypeId);
  }

  public Map<Long, Region> getRegionsByBuildingCountIds(List<Long> buildingCountIds) {
    return regionRepository.findRegionsByBuildingCountIds(buildingCountIds);
  }
}
