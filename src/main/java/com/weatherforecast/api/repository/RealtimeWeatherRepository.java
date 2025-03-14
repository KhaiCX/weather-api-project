package com.weatherforecast.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.weatherforecast.api.entity.RealtimeWeather;

public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String> {

    @Query("SELECT rw FROM RealtimeWeather rw WHERE rw.location.countryCode = :countryCode and rw.location.cityName = :cityName")
    public RealtimeWeather findByCountryCodeAndCityName(String countryCode, String cityName);
    
    @Query("SELECT rw FROM RealtimeWeather rw WHERE rw.locationCode = :code AND rw.location.trashed = false")
    public RealtimeWeather findByLocationCode(String locationCode);
}
