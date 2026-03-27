package com.buildings.service;

import com.buildings.dto.BuildingCountContent;
import com.buildings.dto.BuildingCountFilter;
import com.buildings.repository.BuildingCountRepository;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ValidationService {
  private final BuildingCountRepository buildingCountRepository;
  private final RegionService regionService;
  private final AreaTypeService areaTypeService;
  private final BuildingTypeService buildingTypeService;
  private final ShorelineTypeService shorelineTypeService;

  public ValidationService(
    final BuildingCountRepository buildingCountRepository,
    final RegionService regionService,
    final AreaTypeService areaTypeService,
    final BuildingTypeService buildingTypeService,
    final ShorelineTypeService shorelineTypeService) {
      this.buildingCountRepository = buildingCountRepository;
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

    if (content.areaTypeId() != null && !areaTypeService.getAreaTypeIds().contains(content.areaTypeId())) {
      errors.add(String.format("'%d' is not a valid area type", content.areaTypeId()));
    }

    if (content.buildingTypeId() != null && !buildingTypeService.getBuildingTypeIds().contains(content.buildingTypeId())) {
      errors.add(String.format("'%d' is not a valid building type", content.buildingTypeId()));
    }

    if (content.shorelineTypeId() != null && !shorelineTypeService.getShorelineTypeIds().contains(content.shorelineTypeId())) {
      errors.add(String.format("'%d' is not a valid shoreline type", content.shorelineTypeId()));
    }

    if (content.year() != null && content instanceof BuildingCountContent) {
      validateYearAsInput(content.year()).ifPresent(errors::add);
    }

    if (content.year() != null && content instanceof BuildingCountFilter) {
      validateYearAsSearchFilter(content.year()).ifPresent(errors::add);
    }

    return errors;
  }

  private Optional<String> validateYearAsInput(final Integer year) {
    final int earliestYear = 1900;
    int currentYear = java.time.Year.now().getValue();

    if (year < earliestYear || year > currentYear) {
      return Optional.of(
        String.format("'%d' is not a valid year. Year must be between %d and %d", year, earliestYear, currentYear));
    }
    return Optional.empty();
  }


  private Optional<String> validateYearAsSearchFilter(final Integer year) {
    List<Integer> yearsInDatabase = buildingCountRepository.getYears();

    if (!yearsInDatabase.isEmpty() && !yearsInDatabase.contains(year)) {
      return Optional.of(String.format("'%d' is not a valid year", year));
    }
    return Optional.empty();
  }
}
