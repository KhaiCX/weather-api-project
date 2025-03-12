package com.weatherforecast.api.entity;

import java.time.LocalDateTime;

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
    private Integer windSpeed;
    private Integer precipitation;
    private String status;
    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "location_code")
    @MapsId
    private Location location;
}
