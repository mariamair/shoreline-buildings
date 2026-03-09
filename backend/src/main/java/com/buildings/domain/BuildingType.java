package com.buildings.domain;

public class BuildingType {
  private int id;
  private String name;

  public BuildingType(int id, String name) {
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
