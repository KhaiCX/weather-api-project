package com.weatherforecast.api.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"hour_of_day", "temperature", "precipitation", "status"})
public class HourlyWeatherDTO {
    @JsonProperty("hour_of_day")
    @Range(min = 0, max = 23, message = "Hourly of day must be in the range of 0 to 23")
    private Integer hourOfDay;

    @Range(min = -50, max = 50, message = "Temperator must be in the range of -50 to 50 Celsius degree")
    private Integer temperature;

    @Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
    private Integer precipitation;

    @Column(length = 50)
    @NotBlank(message = "Status must be not empty")
    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    private String status;

    public HourlyWeatherDTO hourOfDay(Integer hourOfDay) {
        setHourOfDay(hourOfDay);
        return this;
    }

    public HourlyWeatherDTO temperature(Integer temperature) {
        setTemperature(temperature);
        return this;
    }

    public HourlyWeatherDTO precipitation(Integer precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public HourlyWeatherDTO status(String status) {
        setStatus(status);
        return this;
    }

    @Override
    public String toString() {
        return "HourlyWeatherDTO [hourOfDay=" + hourOfDay + ", temperature=" + temperature + ", precipitation=" + precipitation + ", status=" + status + "]";
    }

}
