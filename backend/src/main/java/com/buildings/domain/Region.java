package com.buildings.domain;

public class Region {
  private String code;
  private String name;
  private int regionTypeId;

  public Region(String code, String name, int regionTypeId) {
    this.code = code;
    this.name = name;
    this.regionTypeId = regionTypeId;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public int getRegionTypeId() {
    return regionTypeId;
  }
}
