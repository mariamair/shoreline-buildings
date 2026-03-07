package com.buildings.domain;

public class Region {
  private String id;
  private String name;
  private RegionType regionType;

  public Region(String id, String name, RegionType regionType) {
    this.id = id;
    this.name = name;
    this.regionType = regionType;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public RegionType getRegionType() {
    return regionType;
  }

  public void setRegionType(RegionType regionType) {
    this.regionType = regionType;
  }
}
