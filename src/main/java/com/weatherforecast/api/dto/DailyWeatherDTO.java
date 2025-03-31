package com.weatherforecast.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonPropertyOrder({"day_of_month", "month", "min_temp", "max_temp", "precipitation", "status"})
public class DailyWeatherDTO {

    @JsonProperty("day_of_month")
    private Integer dayOfMonth;
    private Integer month;

    @JsonProperty("min_temp")
    private Integer minTemp;

    @JsonProperty("max_temp")
    private Integer maxTemp;
    private Integer precipitation;
    private String status;
}
