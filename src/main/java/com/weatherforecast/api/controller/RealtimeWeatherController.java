package com.weatherforecast.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.common.CommonUtility;
import com.weatherforecast.api.dto.RealtimeWeatherDTO;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.entity.RealtimeWeather;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.service.GeolocationService;
import com.weatherforecast.api.service.RealtimeWeatherService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherController.class);

    private GeolocationService geolocationService;
    private RealtimeWeatherService realtimeWeatherService;
    private ModelMapper modelMapper;

    public RealtimeWeatherController(GeolocationService geolocationService,
            RealtimeWeatherService realtimeWeatherService,
            ModelMapper modelMapper) {
        this.geolocationService = geolocationService;
        this.realtimeWeatherService = realtimeWeatherService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            Location locationFromIP = geolocationService.getLocation(ipAddress);
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);
            return ResponseEntity.ok().body(addLinksByIp(entity2DTO(realtimeWeather)));

        } catch (GeolocationException exception) {
            LOGGER.error(exception.getMessage(), exception);
            return ResponseEntity.badRequest().build();

        } catch (LocationNotFoundException exception) {
            LOGGER.error(exception.getMessage(), exception);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable String locationCode) {
        try {
            RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
            return ResponseEntity.ok().body(addLinksByLocation(entity2DTO(realtimeWeather), locationCode));
        } catch (LocationNotFoundException exception) {
            LOGGER.error(exception.getMessage(), exception);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateRealtimeWeather(@PathVariable String locationCode, @RequestBody @Valid RealtimeWeatherDTO dto) {
        RealtimeWeather realtimeWeather = dto2Entity(dto);
        realtimeWeather.setLocationCode(locationCode);

        RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeather);

        return ResponseEntity.ok().body(addLinksByLocation(entity2DTO(updatedRealtimeWeather), locationCode));
    }

    private RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather) {
        return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
    }

    private RealtimeWeather dto2Entity(RealtimeWeatherDTO dto) {
        return modelMapper.map(dto, RealtimeWeather.class);
    }

    private RealtimeWeatherDTO addLinksByIp(RealtimeWeatherDTO dto) {
        dto.add(linkTo(methodOn(RealtimeWeatherController.class).getRealtimeWeatherByIPAddress(null)).withSelfRel());
        dto.add(linkTo(methodOn(DailyWeatherController.class).listDailyForecastByIPAddress(null)).withRel("daily_forecast"));
        dto.add(linkTo(methodOn(HourlyWeatherController.class).listHourlyForecastByIPAddress(null)).withRel("hourly_forecast"));
        dto.add(linkTo(methodOn(FullWeatherController.class).getFullWeatherByIPAddress(null)).withRel("full_forecast"));
        return dto;
    }

    private RealtimeWeatherDTO addLinksByLocation(RealtimeWeatherDTO dto, String locationCode) {
        dto.add(linkTo(methodOn(RealtimeWeatherController.class).getRealtimeWeatherByLocationCode(locationCode)).withSelfRel());
        dto.add(linkTo(methodOn(DailyWeatherController.class).listDailyForecastByLocationCode(locationCode)).withRel("daily_forecast"));
        dto.add(linkTo(methodOn(HourlyWeatherController.class).listHourlyForecastByLocationCode(locationCode, null)).withRel("hourly_forecast"));
        dto.add(linkTo(methodOn(FullWeatherController.class).getFullWeatherByLocationCode(locationCode)).withRel("full_forecast"));
        return dto;
    }
}
