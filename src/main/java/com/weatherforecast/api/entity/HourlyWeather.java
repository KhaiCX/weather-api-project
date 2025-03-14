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

}
