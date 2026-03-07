package com.buildings.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingCountEntityDto {
  private int id;
  private RegionDto region;
  private BuildingTypeDto buildingType;
  private ShorelineTypeDto shorelineType;
  private AreaTypeDto areaType;
  private int year;
  private Integer buildingCount;
}
