package com.weatherforecast.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.common.CommonUtility;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.entity.RealtimeWeather;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.service.GeolocationService;
import com.weatherforecast.api.service.LocationService;
import com.weatherforecast.api.service.RealtimeWeatherService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherController.class);

    private GeolocationService geolocationService;
    private RealtimeWeatherService realtimeWeatherService;

    public RealtimeWeatherController(GeolocationService geolocationService,
            RealtimeWeatherService realtimeWeatherService) {
        this.geolocationService = geolocationService;
        this.realtimeWeatherService = realtimeWeatherService;
    }

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            Location locationFromIP = geolocationService.getLocation(ipAddress);
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);
            return ResponseEntity.ok().body(realtimeWeather);

        } catch (GeolocationException exception) {
            LOGGER.error(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();

        } catch (LocationNotFoundException exception) {
            LOGGER.error(exception.getMessage(), exception);
            return ResponseEntity.notFound().build();
        }
    }

}
