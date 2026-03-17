package com.buildings.dto;

public record BuildingCountContent(
  String regionCode,
  Integer areaTypeId, 
  Integer buildingTypeId, 
  Integer shorelineTypeId, 
  Integer year, 
  Integer buildingCount) {
}
