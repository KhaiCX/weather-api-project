package com.weatherforecast.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.weatherforecast.api.common.RealtimeWeatherFieldFilter;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FullWeatherDTO {
    private String location;

    @JsonProperty("realtime_weather")
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = RealtimeWeatherFieldFilter.class)
    private RealtimeWeatherDTO RealtimeWeather = new RealtimeWeatherDTO();

    @JsonProperty("hourly_weather")
    private List<HourlyWeatherDTO> listHourlyWeather = new ArrayList<>();

    @JsonProperty("daily_weather")
    private List<DailyWeatherDTO> listDailyWeather = new ArrayList<>();
}
