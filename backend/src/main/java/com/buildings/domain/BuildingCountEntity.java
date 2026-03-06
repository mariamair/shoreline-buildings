package com.buildings.domain;

public class BuildingCountEntity {
  private int id;
  private String regionId;
  private Integer buildingTypeId;
  private Integer shorelineTypeId;
  private Integer areaTypeId;
  private int year;
  private Integer buildingCountId;

  public BuildingCountEntity (int id, String regionId, Integer buildingTypeId, Integer shorelineTypeId, Integer areaTypeId, int year, Integer buildingCountId) {
    this.id = id;
    this.regionId = regionId;
    this.buildingTypeId = buildingTypeId;
    this.shorelineTypeId = shorelineTypeId;
    this.areaTypeId = areaTypeId;
    this.year = year;
    this.buildingCountId = buildingCountId;
  }

  public int getId() {
    return id;
  }

  public String getRegionId() {
    return regionId;
  }

  public Integer getBuildingTypeId() {
    return buildingTypeId;
  }

  public Integer getShorelineTypeId() {
    return shorelineTypeId;
  }
  
  public Integer getAreaTypeId() {
    return areaTypeId;
  }

  public int getYear() {
    return year;
  }

  public Integer getBuildingCountId() {
    return buildingCountId;
  }
}
