package com.weatherforecast.api.dto;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RealtimeWeatherDTO {
    
    private String location;

    @Range(min = -50, max = 50, message = "Temperator must be in the range of -50 to 50 Celsius degree")
    private Integer temperature;

    @Range(min = 0, max = 100, message = "Humidity must be in the range of 0 to 100 percentage")
    private Integer humidity;

    @Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
    private Integer precipitation;

    @NotBlank(message = "Status must be not empty")
    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    private String status;

    @JsonProperty("wind_speed")
    private Integer windSpeed;

    @JsonProperty("last_updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime lastUpdated;

    public RealtimeWeatherDTO location(String location) {
        setLocation(location);
        return this;
    }

    public RealtimeWeatherDTO temperature(Integer temperature) {
        setTemperature(temperature);
        return this;
    }

    public RealtimeWeatherDTO humidity(Integer humidity) {
        setHumidity(humidity);
        return this;
    }

    public RealtimeWeatherDTO precipitation(Integer precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public RealtimeWeatherDTO status(String status) {
        setStatus(status);
        return this;
    }

    public RealtimeWeatherDTO windSpeed(Integer windSpeed) {
        setWindSpeed(windSpeed);
        return this;
    }

    public RealtimeWeatherDTO lastUpdated(LocalDateTime lastUpdated) {
        setLastUpdated(lastUpdated);
        return this;
    }
}
