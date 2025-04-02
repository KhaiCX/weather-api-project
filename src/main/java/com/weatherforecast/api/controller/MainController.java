package com.weatherforecast.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.entity.RootEntity;
import com.weatherforecast.api.controller.LocationController;
import com.weatherforecast.api.controller.HourlyWeatherController;
import com.weatherforecast.api.controller.DailyWeatherController;
import com.weatherforecast.api.controller.FullWeatherController;

@RestController
public class MainController {

    @GetMapping("/")
    public ResponseEntity<RootEntity> handleBaseURI() {
        return ResponseEntity.ok().body(creaRootEntity());
    }
    private RootEntity creaRootEntity() {
        RootEntity rootEntity = new RootEntity();

        String locationsUrl = linkTo(methodOn(LocationController.class).listLocations()).toString();
        rootEntity.setLocationsUrl(locationsUrl);

        String locationByCodeUrl = linkTo(methodOn(LocationController.class).getLocation(null)).toString();
        rootEntity.setLocationByCodeUrl(locationByCodeUrl);

        String realtimeWeatherByIpUrl = linkTo(methodOn(RealtimeWeatherController.class).getRealtimeWeatherByIPAddress(null)).toString();
        rootEntity.setRealtimeWeatherByIpUrl(realtimeWeatherByIpUrl);

        String realtimeWeatherByCodeUrl = linkTo(methodOn(RealtimeWeatherController.class).getRealtimeWeatherByLocationCode(null)).toString();
        rootEntity.setRealtimeWeatherByCodeUrl(realtimeWeatherByCodeUrl);

        String hourlyForecastByIpUrl = linkTo(methodOn(HourlyWeatherController.class).listHourlyForecastByIPAddress(null)).toString();
        rootEntity.setHourlyForecastByIpUrl(hourlyForecastByIpUrl);

        String hourlyForecastByCodeUrl = linkTo(methodOn(HourlyWeatherController.class).listHourlyForecastByLocationCode(null, null)).toString();
        rootEntity.setHourlyForecastByCodeUrl(hourlyForecastByCodeUrl);

        String dailyForecastByIpUrl = linkTo(methodOn(DailyWeatherController.class).listDailyForecastByIPAddress(null)).toString();
        rootEntity.setDailyForecastByIpUrl(dailyForecastByIpUrl);

        String dailyForecastByCodeUrl = linkTo(methodOn(DailyWeatherController.class).listDailyForecastByLocationCode(null)).toString();
        rootEntity.setDailyForecastByCodeUrl(dailyForecastByCodeUrl);

        String fullWeatherByIpUrl = linkTo(methodOn(FullWeatherController.class).getFullWeatherByIPAddress(null)).toString();
        rootEntity.setFullWeatherByIpUrl(fullWeatherByIpUrl);

        String fullWeatherByCodeUrl = linkTo(methodOn(FullWeatherController.class).getFullWeatherByLocationCode(null)).toString();
        rootEntity.setFullWeatherByCodeUrl(fullWeatherByCodeUrl);
        return rootEntity;
    }
}
