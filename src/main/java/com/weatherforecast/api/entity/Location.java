package com.weatherforecast.api.entity;

import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @Column(length = 12, nullable = false, unique = true)
    @NotNull(message = "Location code cannot be null")
    @Length(min = 3, max = 12, message = "Location code must have a length between 3 and 12")
    private String code;

    @Column(length = 128, nullable = false)
    @JsonProperty("city_name")
    @NotNull(message = "City name cannot be null")
    @Length(min = 3, max = 128, message = "City name must have a length between 3 and 128")
    private String cityName;

    @Column(length = 128)
    @JsonProperty("region_name")
    private String regionName;

    @Column(length = 2, nullable = false)
    @JsonProperty("country_code")
    @NotNull(message = "Country code cannot be null")
    private String countryCode;

    @Column(length = 64, nullable = false)
    @NotNull(message = "Country name cannot be null")
    @Length(min = 3, max = 128, message = "Country name must have a length between 3 and 64")
    private String countryName;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private RealtimeWeather realtimeWeather;

    private Boolean enabled;

    @JsonIgnore
    private Boolean trashed;

    public Location(String cityName, String regionName, String countryCode, String countryName) {
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Location other = (Location) obj;
        return Objects.equals(code, other.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "Location [code=" + code + ", cityName=" + cityName + ", regionName=" + regionName + ", countryCode="
                + countryCode + ", countryName=" + countryName + ", enabled=" + enabled + ", trashed=" + trashed + "]";
    }
}
