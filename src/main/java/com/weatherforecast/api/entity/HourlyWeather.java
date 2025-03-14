package com.weatherforecast.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "weather_hourly")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HourlyWeather {
    @EmbeddedId
    private HourlyWeatherId id = new HourlyWeatherId();
    private Integer temperature;
    private Integer precipitation;

    @Column(length = 50)
    private String status;

    public HourlyWeather temperature(Integer temp) {
        setTemperature(temp);
        return this;
    }

    public HourlyWeather id(Location location, Integer hour) {
        this.id.setLocation(location);
        this.id.setHourOfDay(hour);
        return this;
    }

    public HourlyWeather precipitation(Integer precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public HourlyWeather status(String status) {
        setStatus(status);
        return this;
    }

    public HourlyWeather location(Location location) {
        this.id.setLocation(location);
        return this;
    }

    public HourlyWeather hourOfDay(Integer hour) {
        this.id.setHourOfDay(hour);
        return this;
    }

}
