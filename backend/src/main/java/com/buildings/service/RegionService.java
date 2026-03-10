package com.buildings.service;

import com.buildings.domain.Region;
import com.buildings.repository.RegionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class RegionService {
  private final RegionRepository regionRepository;

  public RegionService(RegionRepository regionRepository) {
    this.regionRepository = regionRepository;
    }

  public Region getRegionByCode(String code) {
    return regionRepository.findRegionByCode(code)
      .orElseThrow(() -> new EntityNotFoundException(String.format("Found no region with code '%s'", code)));
  }

  public List<Region> getRegionsByType(int regionTypeId, Integer limit, Integer offset) {
    List<Region> regions = regionRepository.findRegionsByType(regionTypeId, limit, offset);

    if (regions.isEmpty()) {
      throw new EntityNotFoundException(String.format("Found no regions with type id '%d'", regionTypeId));
    }

    return regions;
  }

  public int getTotalCount(int regionTypeId) {
    return regionRepository.countAllRegions(regionTypeId);
  }

  public Map<Long, Region> getRegionsByBuildingCountIds(List<Long> buildingCountIds) {
    return regionRepository.findRegionsByBuildingCountIds(buildingCountIds);
  }
}
