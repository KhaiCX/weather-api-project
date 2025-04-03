package com.weatherforecast.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

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
import com.weatherforecast.api.dto.HourlyWeatherDTO;
import com.weatherforecast.api.dto.HourlyWeatherListDTO;
import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.BadRequestException;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.service.GeolocationService;
import com.weatherforecast.api.service.HourlyWeatherService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/hourly")
@Validated
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

            return ResponseEntity.ok().body(addLinksByIp(listEntity2DTO(hourlyForecast)));
        } catch (NumberFormatException | GeolocationException exception) {
            return ResponseEntity.badRequest().build();

        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> listHourlyForecastByLocationCode(@PathVariable String locationCode, HttpServletRequest request) {
        try {

            Integer currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocationCode(locationCode, currentHour);

            if (hourlyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(addLinksByLocation(listEntity2DTO(hourlyForecast), locationCode));
        } catch (NumberFormatException exception) {
            return ResponseEntity.badRequest().build();

        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateHourlyForecast(@PathVariable String locationCode, @RequestBody @Valid List<HourlyWeatherDTO> listDTO) throws BadRequestException {
        if (listDTO.isEmpty()) {
            throw new BadRequestException("Hourly forecast data cannot be empty");
        }
        List<HourlyWeather> listHourlyWeather = listDTO2ListEntity(listDTO);
        listDTO.forEach(System.out::println);
        listHourlyWeather.forEach(System.out::println);
        try {
            List<HourlyWeather> updateHourlyWeather = hourlyWeatherService.updateByLocationCode(locationCode, listHourlyWeather);
            return ResponseEntity.ok().body(addLinksByLocation(listEntity2DTO(updateHourlyWeather), locationCode));
        } catch (LocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private List<HourlyWeather> listDTO2ListEntity(List<HourlyWeatherDTO> listDTO) {
        List<HourlyWeather> listEntity = new ArrayList<>();
        listDTO.forEach(dto -> {
            listEntity.add(modelMapper.map(dto, HourlyWeather.class));
        });
        return listEntity;
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

    private HourlyWeatherListDTO addLinksByIp(HourlyWeatherListDTO dtos) {
        dtos.add(linkTo(methodOn(HourlyWeatherController.class).listHourlyForecastByIPAddress(null)).withSelfRel());
        dtos.add(linkTo(methodOn(RealtimeWeatherController.class).getRealtimeWeatherByIPAddress(null)).withRel("realtime_weather"));
        dtos.add(linkTo(methodOn(DailyWeatherController.class).listDailyForecastByIPAddress(null)).withRel("daily_forecast"));
        dtos.add(linkTo(methodOn(FullWeatherController.class).getFullWeatherByIPAddress(null)).withRel("full_forecast"));
        return dtos;
    }

    private HourlyWeatherListDTO addLinksByLocation(HourlyWeatherListDTO dtos, String locationCode) {
        dtos.add(linkTo(methodOn(HourlyWeatherController.class).listHourlyForecastByLocationCode(locationCode, null)).withSelfRel());
        dtos.add(linkTo(methodOn(RealtimeWeatherController.class).getRealtimeWeatherByLocationCode(locationCode)).withRel("realtime_weather"));
        dtos.add(linkTo(methodOn(DailyWeatherController.class).listDailyForecastByLocationCode(locationCode)).withRel("daily_forecast"));
        dtos.add(linkTo(methodOn(FullWeatherController.class).getFullWeatherByLocationCode(locationCode)).withRel("full_forecast"));
        return dtos;
    }

}
