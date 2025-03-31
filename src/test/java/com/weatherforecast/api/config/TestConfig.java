package com.weatherforecast.api.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.weatherforecast.api.service.DailyWeatherService;
import com.weatherforecast.api.service.GeolocationService;
import com.weatherforecast.api.service.HourlyWeatherService;
import com.weatherforecast.api.service.LocationService;
import com.weatherforecast.api.service.RealtimeWeatherService;

@Configuration
public class TestConfig {

    @Bean
    public LocationService locationService() {
        return Mockito.mock(LocationService.class);
    }

    @Bean
    public RealtimeWeatherService realtimeWeatherService() {
        return Mockito.mock(RealtimeWeatherService.class);
    }

    @Bean
    public GeolocationService geolocationService() {
        return Mockito.mock(GeolocationService.class);
    }

    @Bean
    public HourlyWeatherService hourlyWeatherService() {
        return Mockito.mock(HourlyWeatherService.class);
    }

    @Bean
    public DailyWeatherService dailyWeatherService() {
        return Mockito.mock(DailyWeatherService.class);
    }
}
