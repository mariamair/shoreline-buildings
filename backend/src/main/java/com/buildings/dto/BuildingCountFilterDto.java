package com.buildings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class BuildingCountFilterDto {
  private Integer areaTypeId;
  private Integer buildingTypeId;
  private Integer shorelineTypeId;
}
