package com.weatherforecast.api.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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
    private String code;

    @Column(length = 128, nullable = false)
    private String cityName;

    @Column(length = 128)
    private String regionName;

    @Column(length = 2, nullable = false)
    private String countryCode;

    @Column(length = 64, nullable = false)
    private String countryName;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private RealtimeWeather realtimeWeather;

    @OneToMany(mappedBy = "id.location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HourlyWeather> listHourlyWeather = new ArrayList<>();

    private Boolean enabled;

    @JsonIgnore
    private Boolean trashed;

    @OneToMany(mappedBy = "id.location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyWeather> listDailyWeather = new ArrayList<>();

    public Location(String cityName, String regionName, String countryCode, String countryName) {
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public Location code(String code) {
        setCode(code);
        return this;
    }

    public Location cityName(String cityName) {
        setCityName(cityName);
        return this;
    }

    public Location regionName(String regionName) {
        setRegionName(regionName);
        return this;
    }

    public Location countryCode(String countryCode) {
        setCountryCode(countryCode);
        return this;
    }

    public Location countryName(String countryName) {
        setCountryName(countryName);
        return this;
    }

    public Location realtimeWeather(RealtimeWeather realtimeWeather) {
        setRealtimeWeather(realtimeWeather);
        return this;
    }

    public Location listHourlyWeather(List<HourlyWeather> listHourlyWeather) {
        setListHourlyWeather(listHourlyWeather);
        return this;
    }

    public Location listDailyWeather(List<DailyWeather> listDailyWeather) {
        setListDailyWeather(listDailyWeather);
        return this;
    }

    public Location enabled(Boolean enabled) {
        setEnabled(enabled);
        return this;
    }
    
    public Location trashed(Boolean trashed) {
        setTrashed(trashed);
        return this;
    }

    public void copyFieldsFrom(Location another) {
        setCityName(another.getCityName());
        setRegionName(another.getRegionName());
        setCountryCode(another.getCountryCode());
        setCountryName(another.getCountryName());
        setEnabled(another.getEnabled());
    }

    public void copeAllFieldFrom(Location another) {
        copyFieldsFrom(another);
        setCode(another.getCode());
        setTrashed(another.getTrashed());
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
        return cityName + ", " + (!Objects.isNull(regionName) ? regionName + ", " : "") + countryCode;
    }
}
