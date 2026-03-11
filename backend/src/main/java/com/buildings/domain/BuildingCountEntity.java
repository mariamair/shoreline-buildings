package com.buildings.domain;

public class BuildingCountEntity {
  private final Long id;
  private final int year;
  private final Integer buildingCount;

  public BuildingCountEntity(final Long id, final int year, final Integer buildingCount) {
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
