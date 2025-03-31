package com.weatherforecast.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.common.CommonUtility;
import com.weatherforecast.api.dto.DailyWeatherDTO;
import com.weatherforecast.api.dto.DailyWeatherListDTO;
import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.service.DailyWeatherService;
import com.weatherforecast.api.service.GeolocationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/daily")
public class DailyWeatherController {
    private GeolocationService geolocationService;
    private DailyWeatherService dailyWeatherService;
    private ModelMapper modelMapper;
    public DailyWeatherController(GeolocationService geolocationService, DailyWeatherService dailyWeatherService, ModelMapper modelMapper) {
        this.geolocationService = geolocationService;
        this.dailyWeatherService = dailyWeatherService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> listDailyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);
        try {

            Location locationFromIP = geolocationService.getLocation(ipAddress);
            List<DailyWeather> dailyForecast = locationFromIP.getListDailyWeathers();

            if (dailyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(listEntity2DTO(dailyForecast));
        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (GeolocationException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> listDailyForecastByLocationCode(@PathVariable String locationCode) {
        List<DailyWeather> dailyForecast = dailyWeatherService.getByLocationCode(locationCode);
        if (dailyForecast.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(listEntity2DTO(dailyForecast));
    }

    private DailyWeatherListDTO listEntity2DTO(List<DailyWeather> dailyForecast) {
        Location location = dailyForecast.get(0).getId().getLocation();
        DailyWeatherListDTO listDTO = new DailyWeatherListDTO();
        listDTO.setLocation(location.toString());

        List<DailyWeatherDTO> dtos = new ArrayList<>();
        dailyForecast.forEach(dailyWeather -> {
            DailyWeatherDTO dto = modelMapper.map(dailyWeather, DailyWeatherDTO.class);
            //listDTO.addDailyWeatherDTO(modelMapper.map(dailyWeather, DailyWeatherDTO.class));
            dtos.add(dto);
        });
        listDTO.setDailyForecast(dtos);
        return listDTO;
    }
}
