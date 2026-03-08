package com.buildings.domain;

public class Region {
  private String code;
  private String name;
  private RegionType regionType;

  public Region(String code, String name, RegionType regionType) {
    this.code = code;
    this.name = name;
    this.regionType = regionType;
  }

  public String getCode() {
    return code;
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
