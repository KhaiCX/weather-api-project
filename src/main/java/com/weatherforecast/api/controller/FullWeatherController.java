package com.weatherforecast.api.controller;

import com.weatherforecast.api.exception.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.common.CommonUtility;
import com.weatherforecast.api.dto.FullWeatherDTO;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.service.FullWeatherService;
import com.weatherforecast.api.service.GeolocationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/full")
@Validated
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

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> getFullWeatherByLocationCode(@PathVariable String locationCode) {

        Location locationInDB = fullWeatherService.get(locationCode);

        return ResponseEntity.ok().body(entity2DTO(locationInDB));
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateFullWeather(@PathVariable String locationCode, @RequestBody @Valid FullWeatherDTO dto) throws BadRequestException {
        if (dto.getListHourlyWeather().isEmpty()) {
            throw new BadRequestException("Hourly Weather data cannot be empty");
        }

        if (dto.getListDailyWeather().isEmpty()) {
            throw new BadRequestException("Daily Weather data cannot be empty");
        }

        Location locationInRequest = dto2Entity(dto);
        Location updatedLocation = fullWeatherService.update(locationCode, locationInRequest);

        return ResponseEntity.ok().body(entity2DTO(updatedLocation));
    }

    private FullWeatherDTO entity2DTO(Location entity) {
        FullWeatherDTO dto = modelMapper.map(entity, FullWeatherDTO.class);
        dto.getRealtimeWeather().setLocation(null);
        return dto;
    }

    private Location dto2Entity(FullWeatherDTO dto) {
        return modelMapper.map(dto, Location.class);
    }
}
