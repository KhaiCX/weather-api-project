package com.weatherforecast.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.common.CommonUtility;
import com.weatherforecast.api.dto.DailyWeatherDTO;
import com.weatherforecast.api.dto.DailyWeatherListDTO;
import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.BadRequestException;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.service.DailyWeatherService;
import com.weatherforecast.api.service.GeolocationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/daily")
@Validated
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
            List<DailyWeather> dailyForecast = locationFromIP.getListDailyWeather();

            if (dailyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(addLinksByIp(listEntity2DTO(dailyForecast)));
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
        return ResponseEntity.ok().body(addLinksByLocation(listEntity2DTO(dailyForecast), locationCode));
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateDailyForecast(@PathVariable String locationCode, @RequestBody @Valid List<DailyWeatherDTO> listDTO) throws BadRequestException {

        if (listDTO.isEmpty()) {
            throw new BadRequestException("Daily forecast data cannot be empty");
        }

        listDTO.forEach(System.out::println);

        List<DailyWeather> dailyWeather = listDTO2ListEntity(listDTO);

        System.out.println("=================");

        dailyWeather.forEach(System.out::println);

        List<DailyWeather> updatedForecast = dailyWeatherService.updateByLocationCode(locationCode, dailyWeather);

        return ResponseEntity.ok().body(addLinksByLocation(listEntity2DTO(updatedForecast), locationCode));
    }

    private DailyWeatherListDTO listEntity2DTO(List<DailyWeather> dailyForecast) {
        Location location = dailyForecast.get(0).getId().getLocation();
        DailyWeatherListDTO listDTO = new DailyWeatherListDTO();
        listDTO.setLocation(location.toString());

        List<DailyWeatherDTO> dtos = new ArrayList<>();
        dailyForecast.forEach(dailyWeather -> {
            DailyWeatherDTO dto = modelMapper.map(dailyWeather, DailyWeatherDTO.class);
            dtos.add(dto);
        });
        listDTO.setDailyForecast(dtos);
        return listDTO;
    }

    private List<DailyWeather> listDTO2ListEntity(List<DailyWeatherDTO> listDTO) {
        List<DailyWeather> listEntity = new ArrayList<>();

        listDTO.forEach(dto -> {
            listEntity.add(modelMapper.map(dto, DailyWeather.class));
        });

        return listEntity;
    }

    private EntityModel<DailyWeatherListDTO> addLinksByIp(DailyWeatherListDTO dtos) {
        return EntityModel.of(dtos)
            .add(linkTo(
                methodOn(DailyWeatherController.class).listDailyForecastByIPAddress(null))
                .withSelfRel())
            .add(linkTo(
                methodOn(RealtimeWeatherController.class).getRealtimeWeatherByIPAddress(null))
                .withRel("realtime_weather"))
            .add(linkTo(
                methodOn(HourlyWeatherController.class).listHourlyForecastByIPAddress(null))
                .withRel("hourly_forecast"))
            .add(linkTo(
                methodOn(FullWeatherController.class).getFullWeatherByIPAddress(null))
                .withRel("full_forecast"));

    }

    private EntityModel<DailyWeatherListDTO> addLinksByLocation(DailyWeatherListDTO dtos, String locationCode) {
        return EntityModel.of(dtos)
            .add(linkTo(
                methodOn(DailyWeatherController.class).listDailyForecastByLocationCode(locationCode))
                .withSelfRel())
            .add(linkTo(
                methodOn(RealtimeWeatherController.class).getRealtimeWeatherByLocationCode(locationCode))
                .withRel("realtime_weather"))
            .add(linkTo(
                methodOn(HourlyWeatherController.class).listHourlyForecastByLocationCode(locationCode, null))
                .withRel("hourly_forecast"))
            .add(linkTo(
                methodOn(FullWeatherController.class).getFullWeatherByLocationCode(locationCode))
                .withRel("full_forecast"));
    }
    
}
