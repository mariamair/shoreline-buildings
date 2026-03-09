package com.buildings.domain;

public class AreaType {
  private int id;
  private String name;  

  public AreaType(int id, String name) {
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
