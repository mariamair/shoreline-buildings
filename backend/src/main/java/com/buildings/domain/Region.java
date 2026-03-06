package com.buildings.domain;

public class Region {
  private String id;
  private String name;
  private int regionTypeId;

  public Region(String id, String name, int regionTypeId) {
    this.id = id;
    this.name = name;
    this.regionTypeId = regionTypeId;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getRegionTypeId() {
    return regionTypeId;
  }
}
