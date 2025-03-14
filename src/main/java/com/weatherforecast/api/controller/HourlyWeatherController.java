package com.weatherforecast.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.common.CommonUtility;
import com.weatherforecast.api.dto.HourlyWeatherDTO;
import com.weatherforecast.api.dto.HourlyWeatherListDTO;
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
    private ModelMapper modelMapper;
    public HourlyWeatherController(GeolocationService geolocationService, HourlyWeatherService hourlyWeatherService, ModelMapper modelMapper) {
        this.geolocationService = geolocationService;
        this.hourlyWeatherService = hourlyWeatherService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {

            Integer currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

            Location locationFromIP = geolocationService.getLocation(ipAddress);

            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(locationFromIP, currentHour);

            if (hourlyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(listEntity2DTO(hourlyForecast));
        } catch (NumberFormatException | GeolocationException exception) {
            return ResponseEntity.badRequest().build();

        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    private HourlyWeatherListDTO listEntity2DTO(List<HourlyWeather> hourlyForecast) {
        Location location = hourlyForecast.get(0).getId().getLocation();

        HourlyWeatherListDTO listDTO = new HourlyWeatherListDTO();
        listDTO.setLocation(location.toString());
        List<HourlyWeatherDTO> dtos = new ArrayList<>();
        hourlyForecast.forEach(hf -> {
            HourlyWeatherDTO dto = modelMapper.map(hf, HourlyWeatherDTO.class);
            dtos.add(dto);
        });
        listDTO.setHourlyForecast(dtos);
        return listDTO;
    }

}
