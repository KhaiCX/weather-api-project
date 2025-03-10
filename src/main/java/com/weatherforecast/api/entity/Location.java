package com.weatherforecast.api.entity;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String code;

    @Column(length = 128, nullable = false)
    @JsonProperty("city_name")
    @NotBlank
    private String cityName;

    @Column(length = 128)
    @JsonProperty("region_name")
    @NotNull
    private String regionName;

    @Column(length = 2, nullable = false)
    @JsonProperty("country_code")
    @NotBlank
    private String countryCode;

    @Column(length = 64, nullable = false)
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
}
