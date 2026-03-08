package com.buildings.service;

import com.buildings.domain.Region;
import com.buildings.dto.RegionDto;
import com.buildings.dto.RegionTypeDto;
import com.buildings.repository.RegionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegionService {
  private final RegionRepository regionRepository;
  private final RegionTypeService regionTypeService;

  public RegionService(RegionRepository regionRepository, RegionTypeService regionTypeService) {
        this.regionRepository = regionRepository;
        this.regionTypeService = regionTypeService;
    }

  public RegionDto getRegionById(String id) {
    System.out.println("Service: Start fetching region...");
    Region region = regionRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Found no region with id " + id));
    return convertToDto(region);
  }

  public List<RegionDto> getRegionsByType(int regionTypeId) {
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
      region.getId(), 
      region.getName(), 
      regionTypeDto);
    return regionDto;
  }

  private boolean isValidRegionTypeId(int regionTypeId) {
    List<Integer> regionTypeIds = regionTypeService.getRegionTypeIds();
    return regionTypeIds.contains(regionTypeId);
  }
}
