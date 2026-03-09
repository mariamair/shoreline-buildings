package com.buildings.service;

import com.buildings.domain.Region;
import com.buildings.repository.RegionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RegionService {
  private final RegionRepository regionRepository;
  private final RegionTypeService regionTypeService;

  public RegionService(RegionRepository regionRepository, RegionTypeService regionTypeService) {
        this.regionRepository = regionRepository;
        this.regionTypeService = regionTypeService;
    }

  public List<String> getAllRegionCodes() {
    List<String> regionCodes = regionRepository.findAllRegionCodes();
    if (regionCodes.isEmpty()) {
      log.warn("No region codes available");
      throw new EntityNotFoundException("Found no region codes");
    }
    return regionCodes;
  }

  public Region getRegionByCode(String code) {
    if (!isValidRegionCode(code)) {
      log.warn(String.format("Client requested invalid region code '%s'", code));
      throw new IllegalArgumentException("Invalid region code");
    }

    return regionRepository.findRegionByCode(code)
      .orElseThrow(() -> new EntityNotFoundException("Found no region with code '" + code + "'"));
  }

  public List<Region> getRegionsByType(int regionTypeId, Integer limit, Integer offset) {
    if (!isValidRegionTypeId(regionTypeId)) {
      log.warn(String.format("Client requested invalid region type '%s'", regionTypeId));
      throw new IllegalArgumentException("Invalid region type");
    }

    return regionRepository.findRegionsByType(regionTypeId, limit, offset);
  }

  public Map<Long, Region> getRegionsByBuildingCountIds(List<Long> buildingCountIds) {
    return regionRepository.findRegionsByBuildingCountIds(buildingCountIds);
  }

  private boolean isValidRegionCode(String regionCode) {
    return getAllRegionCodes().contains(regionCode);
  }
  private boolean isValidRegionTypeId(int regionTypeId) {
    return regionTypeService.getRegionTypeIds().contains(regionTypeId);
  }

}
