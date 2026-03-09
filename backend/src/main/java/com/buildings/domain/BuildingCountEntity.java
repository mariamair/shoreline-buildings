package com.buildings.domain;

public class BuildingCountEntity {
  private Long id;
  private String regionCode;
  private Integer buildingTypeId;
  private Integer shorelineTypeId;
  private Integer areaTypeId;
  private int year;
  private Integer buildingCount;

  public BuildingCountEntity (Long id, String regionCode, Integer buildingTypeId, Integer shorelineTypeId, Integer areaTypeId, int year, Integer buildingCount) {
    this.id = id;
    this.regionCode = regionCode;
    this.buildingTypeId = buildingTypeId;
    this.shorelineTypeId = shorelineTypeId;
    this.areaTypeId = areaTypeId;
    this.year = year;
    this.buildingCount = buildingCount;
  }

  public Long getId() {
    return id;
  }

  public String getRegionCode() {
    return regionCode;
  }

  public void setRegionCode(String regionCode) {
    this.regionCode = regionCode;
  }

  public Integer getBuildingTypeId() {
    return buildingTypeId;
  }

  public void setBuildingType(Integer buildingTypeId) {
    this.buildingTypeId = buildingTypeId;
  }

  public Integer getShorelineTypeId() {
    return shorelineTypeId;
  }

  public void setShorelineType(Integer shorelineTypeId) {
    this.shorelineTypeId = shorelineTypeId;
  }
  
  public Integer getAreaTypeId() {
    return areaTypeId;
  }
  
  public void setAreaType(Integer areaTypeId) {
    this.areaTypeId = areaTypeId;
  }

  public int getYear() {
    return year;
  }

  public Integer getBuildingCount() {
    return buildingCount;
  }

  public void setBuildingCount(Integer buildingCount) {
    this.buildingCount = buildingCount;
  }
}
