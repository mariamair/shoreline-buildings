package com.buildings.graphql;

import com.buildings.dto.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
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
        
        MockDataGenerator mockDataGenerator = new MockDataGenerator();
        return mockDataGenerator.generateShorelineBuildingCountMock(limit, offset);
    }

    
}
