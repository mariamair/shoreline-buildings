package com.buildings.domain;

public class BuildingCountEntity {
  private Long id;
  private int year;
  private Integer buildingCount;

  public BuildingCountEntity (Long id, int year, Integer buildingCount) {
    this.id = id;
    this.year = year;
    this.buildingCount = buildingCount;
  }

  public Long getId() {
    return id;
  }

  public int getYear() {
    return year;
  }

  public Integer getBuildingCount() {
    return buildingCount;
  }
}
