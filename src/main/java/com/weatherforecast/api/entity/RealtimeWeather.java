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

    @Range(min = -50, max = 50, message = "Temperator must be in the range of -50 to 50 Celsius degree")
    private Integer temperature;

    @Range(min = 0, max = 100, message = "Humidity must be in the range of 0 to 100 percentage")
    private Integer humidity;

    @JsonProperty("wind_speed")
    @Range(min = 0, max = 200, message = "Wind speed must be in the range of 0 to 200 km/h")
    private Integer windSpeed;

    @Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
    private Integer precipitation;

    @Column(length = 50)
    @NotBlank(message = "Status must be not empty")
    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
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
