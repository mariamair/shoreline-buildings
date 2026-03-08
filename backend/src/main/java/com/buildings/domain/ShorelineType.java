package com.buildings.domain;

import java.util.HashSet;
import java.util.Set;

public class ShorelineType {
  private int id;
  private String name;

  private Set<BuildingCountEntity> buildingCountEntities = new HashSet<>();

  public ShorelineType(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<BuildingCountEntity> getBuildingCountEntities() {
    return buildingCountEntities;
  }
}
