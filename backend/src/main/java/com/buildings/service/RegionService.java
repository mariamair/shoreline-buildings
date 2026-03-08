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

  public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

  public RegionDto getRegionById(String id) {
    System.out.println("Service: Start fetching region...");
    Region region = regionRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("Found no region with id " + id));
    return convertToDto(region);
  }

  public List<RegionDto> getRegionsByType(int regionType) {
    List<Region> regions = regionRepository.findByType(regionType);
    if (regions.isEmpty()) {
      throw new EntityNotFoundException("Found no regions of type " + regionType);
    }
    List<RegionDto> regionDtos = new ArrayList<>();
    for (Region r : regions) {
      regionDtos.add(convertToDto(r));
    }
    return regionDtos;
  }

  // Helper method for data conversion
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
}
