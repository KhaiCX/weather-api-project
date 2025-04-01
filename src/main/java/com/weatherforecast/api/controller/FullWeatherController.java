package com.weatherforecast.api.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.common.CommonUtility;
import com.weatherforecast.api.dto.FullWeatherDTO;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.service.FullWeatherService;
import com.weatherforecast.api.service.GeolocationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/full")
public class FullWeatherController {
    private GeolocationService geolocationService;
    private FullWeatherService fullWeatherService;
    private ModelMapper modelMapper;
    public FullWeatherController(GeolocationService geolocationService, FullWeatherService fullWeatherService, ModelMapper modelMapper) {
        this.geolocationService = geolocationService;
        this.fullWeatherService = fullWeatherService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> getFullWeatherByIPAress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);
        try {

            Location locationFromIP = geolocationService.getLocation(ipAddress);
            Location locationInDB = fullWeatherService.getByLocation(locationFromIP);

            return ResponseEntity.ok().body(entity2DTO(locationInDB));
        } catch (GeolocationException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    private FullWeatherDTO entity2DTO(Location entity) {
        FullWeatherDTO dto = modelMapper.map(entity, FullWeatherDTO.class);
        return dto;
    }
}
