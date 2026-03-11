package com.buildings.domain;

public class BuildingType {
  private final int id;
  private final String name;

  public BuildingType(final int id, final String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
