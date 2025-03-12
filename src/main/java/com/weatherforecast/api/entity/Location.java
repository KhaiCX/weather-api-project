package com.weatherforecast.api.entity;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Location code cannot be left blank")
    private String code;

    @Column(length = 128, nullable = false)
    @JsonProperty("city_name")
    @NotBlank(message = "City name cannot be left blank")
    private String cityName;

    @Column(length = 128)
    @JsonProperty("region_name")
    private String regionName;

    @Column(length = 2, nullable = false)
    @JsonProperty("country_code")
    @NotBlank(message = "Country code cannot be left blank")
    private String countryCode;

    @Column(length = 64, nullable = false)
    @NotBlank(message = "Country name cannot be left blank")
    private String countryName;

    private Boolean enabled;

    @JsonIgnore
    private Boolean trashed;

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
