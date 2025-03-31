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

    public DailyWeather getShallowCopy() {
        DailyWeather copy = new DailyWeather();
        copy.setId(this.getId());

        return copy;
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
        DailyWeather other = (DailyWeather) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "DailyWeather [id=" + id + ", minTemp=" + minTemp + ", maxTemp=" + maxTemp + ", precipitation="
                + precipitation + ", status=" + status + "]";
    }
}
