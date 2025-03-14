package com.weatherforecast.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.common.CommonUtility;
import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.service.GeolocationService;
import com.weatherforecast.api.service.HourlyWeatherService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/hourly")
public class HourlyWeatherController {
    private GeolocationService geolocationService;
    private HourlyWeatherService hourlyWeatherService;
    public HourlyWeatherController(GeolocationService geolocationService, HourlyWeatherService hourlyWeatherService) {
        this.geolocationService = geolocationService;
        this.hourlyWeatherService = hourlyWeatherService;
    }

    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        Integer currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

        try {
            Location locationFromIP = geolocationService.getLocation(ipAddress);

            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIP, currentHour);

            if (hourlyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(hourlyForecast);
        } catch (GeolocationException exception) {
            return ResponseEntity.badRequest().build();

        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
