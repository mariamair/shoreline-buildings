package com.buildings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateBuildingCountEntityDto {
  private String regionCode;
  private Integer areaTypeId;
  private Integer buildingTypeId;
  private Integer shorelineTypeId;
  private Integer year;
  private Integer buildingCount;
}
