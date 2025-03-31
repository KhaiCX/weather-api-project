package com.weatherforecast.api.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
    @JsonIgnore
    private String locationCode;
    private Integer temperature;
    private Integer humidity;

    @JsonProperty("wind_speed")
    @Range(min = 0, max = 200, message = "Wind speed must be in the range of 0 to 200 km/h")
    private Integer windSpeed;
    private Integer precipitation;

    @Column(length = 50)
    private String status;

    @JsonProperty("last_updated")
    @JsonIgnore
    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "location_code")
    @MapsId
    @JsonIgnore
    private Location location;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RealtimeWeather other = (RealtimeWeather) obj;
        return Objects.equals(locationCode, other.locationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationCode);
    }

    
}
