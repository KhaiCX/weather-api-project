package com.weatherforecast.api.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "realtime_weather")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RealtimeWeather {

    @Id
    @Column(name = "location_code")
    
    private String locationCode;
    private Integer temperature;
    private Integer humidity;

    @JsonProperty("wind_speed")
    private Integer windSpeed;
    private Integer precipitation;
    private String status;

    @JsonProperty("last_updated")
    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "location_code")
    @MapsId
    private Location location;
}
