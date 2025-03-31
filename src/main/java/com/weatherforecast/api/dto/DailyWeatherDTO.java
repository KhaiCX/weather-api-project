package com.weatherforecast.api.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonPropertyOrder({"day_of_month", "month", "min_temp", "max_temp", "precipitation", "status"})
public class DailyWeatherDTO {

    @JsonProperty("day_of_month")
    @Range(min = 1, max = 31, message = "Day of month must be between 1 and 31")
    private Integer dayOfMonth;

    @Range(min = 1, max = 12, message = "Month must be between 1 and 12")
    private Integer month;

    @JsonProperty("min_temp")
    @Range(min = -50, max = 50, message = "Minimum Temperator must be in the range of -50 to 50 Celsius degree")
    private Integer minTemp;

    @JsonProperty("max_temp")
    @Range(min = -50, max = 50, message = "Maximum Temperator must be in the range of -50 to 50 Celsius degree")
    private Integer maxTemp;

    @Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
    private Integer precipitation;

    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    private String status;

    public DailyWeatherDTO dayOfMonth(Integer dayOfMonth) {
        setDayOfMonth(dayOfMonth);
        return this;
    }

    public DailyWeatherDTO month(Integer month) {
        setMonth(month);
        return this;
    }

    public DailyWeatherDTO minTemp(Integer minTemp) {
        setMinTemp(minTemp);
        return this;
    }

    public DailyWeatherDTO maxTemp(Integer maxTemp) {
        setMaxTemp(maxTemp);
        return this;
    }

    public DailyWeatherDTO precipitation(Integer precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public DailyWeatherDTO status(String status) {
        setStatus(status);
        return this;
    }
}
