package com.buildings.service;

import com.buildings.domain.Region;
import com.buildings.dto.RegionDto;
import com.buildings.dto.RegionTypeDto;
import com.buildings.repository.RegionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RegionService {
  private final RegionRepository regionRepository;
  private final RegionTypeService regionTypeService;

  public RegionService(RegionRepository regionRepository, RegionTypeService regionTypeService) {
        this.regionRepository = regionRepository;
        this.regionTypeService = regionTypeService;
    }

  public RegionDto getRegionById(String code) {
    log.info("Service: Fetching region with code '" + code + "'...");

    Region region = regionRepository.findById(code)
      .orElseThrow(() -> new EntityNotFoundException("Found no region with code '" + code + "'"));

    return convertToDto(region);
  }

  public List<RegionDto> getRegionsByType(int regionTypeId) {
    log.info("Service: Fetching regions by region type...");

    if (!isValidRegionTypeId(regionTypeId)) {
      throw new IllegalArgumentException("Invalid region type");
    }

    List<Region> regions = regionRepository.findByType(regionTypeId);

    List<RegionDto> regionDtos = new ArrayList<>();
    for (Region r : regions) {
      regionDtos.add(convertToDto(r));
    }
    return regionDtos;
  }

  // Helper methods
  private RegionDto convertToDto(Region region) {
    RegionTypeDto regionTypeDto = new RegionTypeDto(
      region.getRegionType().getId(), 
      region.getRegionType().getName());

    RegionDto regionDto = new RegionDto(
      region.getCode(), 
      region.getName(), 
      regionTypeDto);
    return regionDto;
  }

  private boolean isValidRegionTypeId(int regionTypeId) {
    List<Integer> regionTypeIds = regionTypeService.getRegionTypeIds();
    return regionTypeIds.contains(regionTypeId);
  }
}
