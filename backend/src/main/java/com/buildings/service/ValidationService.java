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
  private final RegionTypeService regionTypeService;
  private final AreaTypeService areaTypeService;
  private final BuildingTypeService buildingTypeService;
  private final ShorelineTypeService shorelineTypeService;

  public ValidationService(
    final BuildingCountRepository buildingCountRepository,
    final RegionService regionService,
    final RegionTypeService regionTypeService,
    final AreaTypeService areaTypeService,
    final BuildingTypeService buildingTypeService,
    final ShorelineTypeService shorelineTypeService) {
      this.buildingCountRepository = buildingCountRepository;
      this.regionService = regionService;
      this.regionTypeService = regionTypeService;
      this.areaTypeService = areaTypeService;
      this.buildingTypeService = buildingTypeService;
      this.shorelineTypeService = shorelineTypeService;
    }

  public List<String> validate(final Validatable record) {
    List<String> errors = new ArrayList<>();

    if (record.regionCode() != null && !regionService.getRegionCodes().contains(record.regionCode())) {
      errors.add(String.format("'%s' is not a valid region code", record.regionCode()));
    }

    if (record instanceof BuildingCountFilter filter) {
      if (filter.regionTypeId() != null && !regionTypeService.getRegionTypeIds().contains(filter.regionTypeId())) {
        errors.add(String.format("'%s' is not a valid region type id", filter.regionTypeId()));
      }
    }

    if (record.areaTypeId() != null && !areaTypeService.getAreaTypeIds().contains(record.areaTypeId())) {
      errors.add(String.format("'%d' is not a valid area type", record.areaTypeId()));
    }

    if (record.buildingTypeId() != null
      && !buildingTypeService.getBuildingTypeIds().contains(record.buildingTypeId())) {
      errors.add(String.format("'%d' is not a valid building type", record.buildingTypeId()));
    }

    if (record.shorelineTypeId() != null
      && !shorelineTypeService.getShorelineTypeIds().contains(record.shorelineTypeId())) {
      errors.add(String.format("'%d' is not a valid shoreline type", record.shorelineTypeId()));
    }

    if (record.year() != null && record instanceof BuildingCountContent) {
      validateYearAsInput(record.year()).ifPresent(errors::add);
    }

    if (record.year() != null && record instanceof BuildingCountFilter) {
      validateYearAsSearchFilter(record.year()).ifPresent(errors::add);
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
