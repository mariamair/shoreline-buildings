package com.buildings.service;

import com.buildings.domain.Region;
import com.buildings.repository.RegionRepository;
import java.util.List;

public class RegionService {
  private RegionRepository regionRepository;

  public Region getRegionById(String id) {
    Region region = regionRepository.findById(id);
    if (region == null) {
      // throw new Exception("Region with id " + id + "not found");
    }
    return region;
  }

  /* 
  public List<Region> getRegionsByType(int regionType) {
    List<Region> regions = regionRepository.findByType(regionType);
    if (regions == null) {
      // throw new Exception("No regions of type " + regionType);
    }
    return regions;
  } */
}
