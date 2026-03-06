package com.buildings.domain;

import java.util.HashSet;
import java.util.Set;

public class RegionType {
  private int id;
  private String name;

  private Set<Region> regions = new HashSet<>();

  public RegionType(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<Region> getRegions() {
    return regions;
  }
}
