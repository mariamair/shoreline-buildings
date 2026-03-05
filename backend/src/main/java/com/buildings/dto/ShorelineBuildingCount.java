package com.buildings.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShorelineBuildingCount {
    private int id;
    private Region region;
    private BuildingType buildingType;
    private ShorelineType shorelineType;
    private AreaType areaType;
    private int year;
    private Integer buildingCount;
}
