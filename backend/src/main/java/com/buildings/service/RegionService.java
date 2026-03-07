package com.buildings.service;

import com.buildings.domain.Region;
import com.buildings.dto.RegionDto;
import com.buildings.dto.RegionTypeDto;
import com.buildings.repository.RegionRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
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
      .orElseThrow(() -> new EntityNotFoundException("Region with id " + id + " not found"));;
    return convertToDto(region);
  }

  /* 
  public List<Region> getRegionsByType(int regionType) {
    List<Region> regions = regionRepository.findByType(regionType);
    if (regions == null) {
      // throw new Exception("No regions of type " + regionType);
    }
    return regions;
  } */

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
