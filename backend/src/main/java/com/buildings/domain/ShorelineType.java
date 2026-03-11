package com.buildings.domain;

public class ShorelineType {
  private final int id;
  private final String name;

  public ShorelineType(final int id, final String name) {
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
