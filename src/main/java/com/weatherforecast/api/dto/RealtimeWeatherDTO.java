package com.weatherforecast.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RealtimeWeatherDTO {
    
    private String location;
    private Integer temperature;
    private Integer humidity;
    private Integer precipitation;
    private String status;

    @JsonProperty("wind_speed")
    private Integer windSpeed;

    @JsonProperty("last_updated")
    private LocalDateTime lastUpdated;
}
