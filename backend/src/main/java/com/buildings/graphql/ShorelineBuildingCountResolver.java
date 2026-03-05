package com.buildings.graphql;

import com.buildings.dto.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * GraphQL resolver for ShorelineBuildingCount queries.
 */

@Controller
public class ShorelineBuildingCountResolver {
    @QueryMapping
    public List<ShorelineBuildingCount> buildingCounts(
            @Argument Integer limit,
            @Argument Integer offset) {
        
        // Set defaults if null
        if (limit == null) {
            limit = 50;
        }
        if (offset == null) {
            offset = 0;
        }
        
        return generateMockData(limit, offset);
    }

    private List<ShorelineBuildingCount> generateMockData(Integer limit, Integer offset) {
        List<ShorelineBuildingCount> results = new ArrayList<>();
        
        List<Region> regions = new ArrayList<>();
        regions.add(new Region("01", "Stockholms län", new RegionType(2, "län")));
        regions.add(new Region("22", "Västernorrlands län", new RegionType(2, "län")));
        regions.add(new Region("1980", "Västerås", new RegionType(3, "kommun")));
        regions.add(new Region("2518", "Övertorneå", new RegionType(3, "kommun")));
        regions.add(new Region("00", "Riket", new RegionType(1, "sverige")));
        
        String[] buildingTypes = {"industri", "samhällsfunktion", "bostad", "komplementbyggnad"};
        String[] shorelineTypes = {"totalt", "hav", "inlandsvatten"};
        String[] areaTypes = {"totalt", "inom tätort", "inom formellt skyddad natur"};
        
        int totalCount = 200;
        int startIndex = Math.min(offset, totalCount);
        int endIndex = Math.min(offset + limit, totalCount);
        
        for (int i = startIndex; i < endIndex; i++) {
            ShorelineBuildingCount record = new ShorelineBuildingCount();
            
            record.setId(i);
            record.setRegion(regions.get(i % regions.size()));
            record.setBuildingType(new BuildingType(i, buildingTypes[i % buildingTypes.length]));
            record.setShorelineType(new ShorelineType(i, shorelineTypes[i % shorelineTypes.length]));
            record.setAreaType(new AreaType(i, areaTypes[i % areaTypes.length]));
            record.setYear(2020 + (i % 4));
            record.setBuildingCount((i * 10) + 5);
            
            results.add(record);
        }
        
        return results;
    }
}
