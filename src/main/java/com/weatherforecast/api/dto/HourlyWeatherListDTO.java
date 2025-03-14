package com.weatherforecast.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class HourlyWeatherListDTO {
    private String location;

    @JsonProperty("hourly_forecast")
    private List<HourlyWeatherDTO> hourlyForecast = new ArrayList<>();
}
