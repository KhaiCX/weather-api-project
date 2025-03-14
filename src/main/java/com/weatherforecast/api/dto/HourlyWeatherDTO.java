package com.weatherforecast.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"hour_of_day", "temperature", "precipitation", "status"})
public class HourlyWeatherDTO {
    @JsonProperty("hour_of_day")
    private Integer hourOfDay;
    private Integer temperature;
    private Integer precipitation;
    private String status;

}
