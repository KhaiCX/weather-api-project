package com.weatherforecast.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime lastUpdated;
}
