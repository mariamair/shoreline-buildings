package com.buildings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ShorelineBuildingCountApp {
    public static void main(String[] args) {
        SpringApplication.run(ShorelineBuildingCountApp.class, args);
    }

    @RestController
    public static class HealthController {
        @GetMapping("/health")
        public String health() {
            return "Building Count API is running!";
        }
    }
}
