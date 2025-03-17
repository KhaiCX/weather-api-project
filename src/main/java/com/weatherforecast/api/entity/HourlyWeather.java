package com.weatherforecast.api.entity;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "HourlyWeather [hourOfDay=" + id.getHourOfDay() + ", temperature=" + temperature + ", precipitation=" + precipitation + ", status=" + status + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HourlyWeather other = (HourlyWeather) obj;
        return Objects.equals(id, other.id);
    }

    public HourlyWeather getShallowCopy() {
        HourlyWeather copy = new HourlyWeather();
        copy.setId(this.getId());
        return copy;
    }

}
