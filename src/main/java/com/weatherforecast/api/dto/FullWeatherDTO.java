package com.weatherforecast.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FullWeatherDTO {
    private String location;

    @JsonProperty("realtime_weather")
    private RealtimeWeatherDTO RealtimeWeather = new RealtimeWeatherDTO();

    @JsonProperty("hourly_weather")
    private List<HourlyWeatherDTO> listHourlyWeather = new ArrayList<>();

    @JsonProperty("daily_weather")
    private List<DailyWeatherDTO> listDailyWeather = new ArrayList<>();
}
