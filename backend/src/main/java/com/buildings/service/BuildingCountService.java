package com.buildings.service;

import com.buildings.domain.BuildingCountEntity;
import com.buildings.dto.BuildingCountContent;
import com.buildings.repository.BuildingCountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuildingCountService {
  private final BuildingCountRepository buildingCountRepository;
  private final ValidationService validationService;
  private final RegionService regionService;
  private final AreaTypeService areaTypeService;
  private final BuildingTypeService buildingTypeService;
  private final ShorelineTypeService shorelineTypeService;

  public BuildingCountService(
    final BuildingCountRepository buildingCountRepository, final ValidationService validationService,
    final RegionService regionService,
    final AreaTypeService areaTypeService,
    final BuildingTypeService buildingTypeService,
    final ShorelineTypeService shorelineTypeService) {
        this.buildingCountRepository = buildingCountRepository;
        this.validationService = validationService;
        this.regionService = regionService;
        this.areaTypeService = areaTypeService;
        this.buildingTypeService = buildingTypeService;
        this.shorelineTypeService = shorelineTypeService;
    }

  public BuildingCountEntity getBuildingCountEntityById(final Long id) {
    return buildingCountRepository.findBuildingCountEntityById(id)
      .orElseThrow(() -> new EntityNotFoundException(String.format("Found no building count entity with id '%d'", id)));
  }

  public List<BuildingCountEntity> getBuildingCountEntities(
      final BuildingCountContent filter,
      final Integer limit,
      final Integer offset) {

    List<String> errors = new ArrayList<>();

    if (filter != null) {
      errors = validationService.validateContent(filter);
    }

    if (filter != null && filter.year() != null) {
      validateYearAsSearchFilter(filter.year()).ifPresent(errors::add);
    }

    if (!errors.isEmpty()) {
      throw new IllegalArgumentException("Invalid filter value(s): " +  String.join("; ", errors));
    }
    return buildingCountRepository.findBuildingCountEntities(filter, limit, offset);
  }

  public int getTotalCount(final BuildingCountContent filter) {
    return buildingCountRepository.countBuildingCountEntities(filter);
  }

  public BuildingCountEntity createBuildingCountEntity(final BuildingCountContent buildingCountEntity) {
    List<String> errors = new ArrayList<>();
    errors = validationService.validateContent(buildingCountEntity);

    if (buildingCountEntity.year() != null) {
        validateYearAsInput(buildingCountEntity.year()).ifPresent(errors::add);
    }

    if (!errors.isEmpty()) {
      throw new IllegalArgumentException("Invalid input value(s): " +  String.join("; ", errors));
    }
    return buildingCountRepository.saveBuildingCountEntity(buildingCountEntity);
  }

  public BuildingCountEntity updateBuildingCountEntity(final Long id, final BuildingCountContent buildingCountEntity) {
    List<String> errors = new ArrayList<>();
    errors = validationService.validateContent(buildingCountEntity);

    if (buildingCountEntity.year() != null) {
        validateYearAsInput(buildingCountEntity.year()).ifPresent(errors::add);
    }

    if (!errors.isEmpty()) {
      throw new IllegalArgumentException("Invalid input value(s): " +  String.join("; ", errors));
    }
    return buildingCountRepository.updateBuildingCountEntity(id, buildingCountEntity);
  }

  public boolean deleteBuildingCountEntity(final Long id) {
    int rowsAffected = buildingCountRepository.deleteBuildingCountEntity(id);

    if (rowsAffected > 1) {
      throw new IllegalStateException(
        String.format("Expected to delete 1 row, but deleted %d rows for id %d", rowsAffected, id));
    }
    return rowsAffected == 1;
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
    if (!buildingCountRepository.getYears().contains(year)) {
      return Optional.of(String.format("'%d' is not a valid year", year));
    }
    return Optional.empty();
  }
}
