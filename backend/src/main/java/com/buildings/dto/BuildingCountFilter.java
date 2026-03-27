package com.buildings.dto;

import com.buildings.service.Validatable;

public record BuildingCountFilter(
  String regionCode,
  Integer areaTypeId,
  Integer buildingTypeId,
  Integer shorelineTypeId,
  Integer year,
  Integer buildingCount) implements Validatable {
} 
