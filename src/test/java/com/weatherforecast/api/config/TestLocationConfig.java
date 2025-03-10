package com.weatherforecast.api.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.weatherforecast.api.service.LocationService;

@Configuration
public class TestLocationConfig {
    @Bean
    public LocationService locationService() {
        return Mockito.mock(LocationService.class);
    }
}
