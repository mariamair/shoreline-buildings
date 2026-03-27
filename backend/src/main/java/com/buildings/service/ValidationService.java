package com.buildings.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
  private final RegionService regionService;
  private final AreaTypeService areaTypeService;
  private final BuildingTypeService buildingTypeService;
  private final ShorelineTypeService shorelineTypeService;

  public ValidationService(
    final RegionService regionService,
    final AreaTypeService areaTypeService,
    final BuildingTypeService buildingTypeService,
    final ShorelineTypeService shorelineTypeService) {
        this.regionService = regionService;
        this.areaTypeService = areaTypeService;
        this.buildingTypeService = buildingTypeService;
        this.shorelineTypeService = shorelineTypeService;
    }

  public List<String> validateContent(final Validatable content) {
    List<String> errors = new ArrayList<>();

    if (content.regionCode() != null && !regionService.getRegionCodes().contains(content.regionCode())) {
      errors.add(String.format("'%s' is not a valid region code", content.regionCode()));
    }

    if(content.areaTypeId() != null && !areaTypeService.getAreaTypeIds().contains(content.areaTypeId())) {
      errors.add(String.format("'%d' is not a valid area type", content.areaTypeId()));
    }

    if(content.buildingTypeId() != null && !buildingTypeService.getBuildingTypeIds().contains(content.buildingTypeId())) {
      errors.add(String.format("'%d' is not a valid building type", content.buildingTypeId()));
    }

    if(content.shorelineTypeId() != null && !shorelineTypeService.getShorelineTypeIds().contains(content.shorelineTypeId())) {
      errors.add(String.format("'%d' is not a valid shoreline type", content.shorelineTypeId()));
    }

    return errors;
  }

/*   public String validateContent(final Validatable content) {
    String error = "";

    if (content.regionCode() != null && !regionService.getRegionCodes().contains(content.regionCode())) {
      error += String.format("Region with code '%s' does not exist", content.regionCode());
    }

    return error;
  } */
}
