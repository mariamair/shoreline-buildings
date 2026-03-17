package com.buildings.service;

import com.buildings.domain.BuildingCountEntity;
import com.buildings.dto.BuildingCountFilterDto;
import com.buildings.dto.BuildingCountContent;
import com.buildings.repository.BuildingCountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuildingCountService {
  private final BuildingCountRepository buildingCountRepository;
  private final RegionService regionService;
  private final AreaTypeService areaTypeService;
  private final BuildingTypeService buildingTypeService;
  private final ShorelineTypeService shorelineTypeService;

  public BuildingCountService(
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

  public BuildingCountEntity getBuildingCountEntityById(final Long id) {
    return buildingCountRepository.findBuildingCountEntityById(id)
      .orElseThrow(() -> new EntityNotFoundException(String.format("Found no building count entity with id '%d'", id)));
  }

  public List<BuildingCountEntity> getBuildingCountEntities(
      final BuildingCountFilterDto filter,
      final Integer limit,
      final Integer offset) {
    if (filter != null) {
      validateFilter(filter);
    }
    return buildingCountRepository.findBuildingCountEntities(filter, limit, offset);
  }

  public int getTotalCount(final BuildingCountFilterDto filter) {
    return buildingCountRepository.countBuildingCountEntities(filter);
  }

  public BuildingCountEntity createBuildingCountEntity(final BuildingCountContent buildingCountEntity) {
    validateContent(buildingCountEntity);
    return buildingCountRepository.saveBuildingCountEntity(buildingCountEntity);
  }

  public BuildingCountEntity updateBuildingCountEntity(final Long id, final BuildingCountContent buildingCountEntity) {
    validateContent(buildingCountEntity);
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

  private void validateFilter(final BuildingCountFilterDto filter) {
    List<String> errors = new ArrayList<>();

    if (filter.getRegionCode() != null) {
      String regionCode = filter.getRegionCode();
      if (!regionService.getRegionCodes().contains(regionCode)) {
        errors.add(String.format("'%s' is not a valid region code", regionCode));
      }
    }

    if (filter.getAreaTypeId() != null) {
      int areaTypeId = filter.getAreaTypeId();
      if (!areaTypeService.getAreaTypeIds().contains(areaTypeId)) {
        errors.add(String.format("'%d' is not a valid area type", areaTypeId));
      }
    }

    if (filter.getBuildingTypeId() != null) {
      int buildingTypeId = filter.getBuildingTypeId();
      if (!buildingTypeService.getBuildingTypeIds().contains(buildingTypeId)) {
        errors.add(String.format("'%d' is not a valid building type", buildingTypeId));
      }
    }

    if (filter.getShorelineTypeId() != null) {
      int shorelineTypeId = filter.getShorelineTypeId();
      if (!shorelineTypeService.getShorelineTypeIds().contains(shorelineTypeId)) {
        errors.add(String.format("'%d' is not a valid shoreline type", shorelineTypeId));
      }
    }

    if (filter.getYear() != null) {
      int year = filter.getYear();
      if (!buildingCountRepository.getYears().contains(year)) {
        errors.add(String.format("'%d' is not a valid year", year));
      }
    }

    if (!errors.isEmpty()) {
      throw new IllegalArgumentException("Invalid filter value(s): " +  String.join("; ", errors));
    }
  }

  private void validateContent(final BuildingCountContent content) {
    List<String> errors = new ArrayList<>();

    if (content.regionCode() != null) {
      String regionCode = content.regionCode();
      if (!regionService.getRegionCodes().contains(regionCode)) {
        errors.add(String.format("'%s' is not a valid region code", regionCode));
      }
    }

    if (content.areaTypeId() != null) {
      int areaTypeId = content.areaTypeId();
      if (!areaTypeService.getAreaTypeIds().contains(areaTypeId)) {
        errors.add(String.format("'%d' is not a valid area type", areaTypeId));
      }
    }

    if (content.buildingTypeId() != null) {
      int buildingTypeId = content.buildingTypeId();
      if (!buildingTypeService.getBuildingTypeIds().contains(buildingTypeId)) {
        errors.add(String.format("'%d' is not a valid building type", buildingTypeId));
      }
    }

    if (content.shorelineTypeId() != null) {
      int shorelineTypeId = content.shorelineTypeId();
      if (!shorelineTypeService.getShorelineTypeIds().contains(shorelineTypeId)) {
        errors.add(String.format("'%d' is not a valid shoreline type", shorelineTypeId));
      }
    }

    if (content.year() != null) {
      int year = content.year();
      final int earliestYear = 1900;
      int currentYear = java.time.Year.now().getValue();

      if (year < earliestYear || year > currentYear) {
        errors.add(
          String.format("'%d' is not a valid year. Year must be between %d and %d", year, earliestYear, currentYear));
      }
    }

    if (!errors.isEmpty()) {
      throw new IllegalArgumentException("Invalid filter value(s): " +  String.join("; ", errors));
    }
  }
}
