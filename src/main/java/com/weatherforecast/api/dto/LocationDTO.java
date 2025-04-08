package com.weatherforecast.api.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"code", "city_name", "region_name", "country_code", "country_name", "enabled"})
@Getter
@Setter
@Relation(collectionRelation = "locations")
public class LocationDTO {
    @NotNull(message = "Location code cannot be null")
    @Length(min = 3, max = 12, message = "Location code must have 3-12 characters")
    private String code;

    @JsonProperty("city_name")
    @NotNull(message = "City name cannot be null")
    @Length(min = 3, max = 128, message = "City name must have 3-128 characters")
    private String cityName;

    @JsonProperty("region_name")
    @Length(min = 3, max = 128, message = "Region name must have 3-128 characters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String regionName;

    @JsonProperty("country_code")
    @NotNull(message = "Country code cannot be null")
    @Length(min = 2, max = 2, message = "Country code must have 2 characters")
    private String countryCode;

    @JsonProperty("country_name")
    @NotNull(message = "Country name cannot be null")
    @Length(min = 3, max = 64, message = "Country name must have 3-64 characters")
    private String countryName;

    private Boolean enabled;

    public LocationDTO code(String code) {
        setCode(code);
        return this;
    }

    public LocationDTO cityName(String cityName) {
        setCityName(cityName);
        return this;
    }

    public LocationDTO regionName(String regionName) {
        setRegionName(regionName);
        return this;
    }

    public LocationDTO countryCode(String countryCode) {
        setCountryCode(countryCode);
        return this;
    }

    public LocationDTO countryName(String countryName) {
        setCountryName(countryName);
        return this;
    }

    public LocationDTO enabled(Boolean enabled) {
        setEnabled(enabled);
        return this;
    }
}
