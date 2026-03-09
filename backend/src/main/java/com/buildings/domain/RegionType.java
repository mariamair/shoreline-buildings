package com.buildings.domain;

public class RegionType {
  private int id;
  private String name;

  public RegionType(int id, String name) {
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
