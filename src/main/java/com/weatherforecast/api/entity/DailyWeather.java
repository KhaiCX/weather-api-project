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
@Table(name = "weather_daily")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DailyWeather {
    @EmbeddedId
    private DailyWeatherId id = new DailyWeatherId();
    private Integer minTemp;
    private Integer maxTemp;
    private Integer precipitation;

    @Column(length = 50)
    private String status;

    public DailyWeather precipitation(Integer precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public DailyWeather status(String status) {
        setStatus(status);
        return this;
    }

    public DailyWeather location(Location location) {
        this.id.setLocation(location);
        return this;
    }

    public DailyWeather dayOfMonth(Integer dayOfMonth) {
        this.id.setDayOfMonth(dayOfMonth);
        return this;
    }

    public DailyWeather month(Integer month) {
        this.id.setMonth(month);
        return this;
    }

    public DailyWeather minTemp(Integer minTemp) {
        this.setMinTemp(minTemp);
        return this;
    }

    public DailyWeather maxTemp(Integer maxTemp) {
        this.setMaxTemp(maxTemp);
        return this;
    }
}
