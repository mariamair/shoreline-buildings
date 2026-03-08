package com.buildings.domain;

public class BuildingCountEntity {
  private Long id;
  private Region region;
  private BuildingType buildingType;
  private ShorelineType shorelineType;
  private AreaType areaType;
  private int year;
  private Integer buildingCount;

  public BuildingCountEntity (Long id, Region region, BuildingType buildingType, ShorelineType shorelineType, AreaType areaType, int year, Integer buildingCount) {
    this.id = id;
    this.region = region;
    this.buildingType = buildingType;
    this.shorelineType = shorelineType;
    this.areaType = areaType;
    this.year = year;
    this.buildingCount = buildingCount;
  }

  public Long getId() {
    return id;
  }

  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }

  public BuildingType getBuildingType() {
    return buildingType;
  }

  public void setBuildingType(BuildingType buildingType) {
    this.buildingType = buildingType;
  }

  public ShorelineType getShorelineType() {
    return shorelineType;
  }

  public void setShorelineType(ShorelineType shorelineType) {
    this.shorelineType = shorelineType;
  }
  
  public AreaType getAreaType() {
    return areaType;
  }
  
  public void setAreaType(AreaType areaType) {
    this.areaType = areaType;
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
