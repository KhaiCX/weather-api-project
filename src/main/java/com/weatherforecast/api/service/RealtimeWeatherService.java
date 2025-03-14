package com.weatherforecast.api.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.entity.RealtimeWeather;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.repository.RealtimeWeatherRepository;

@Service
public class RealtimeWeatherService {

    private RealtimeWeatherRepository realtimeWeatherRepository;

    public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository) {
        this.realtimeWeatherRepository = realtimeWeatherRepository;
    }

    public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {

        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (Objects.isNull(realtimeWeather)) {
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }

        return realtimeWeather;
    }

    public RealtimeWeather getByLocationCode(String locationCode) throws LocationNotFoundException {

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);

        if (Objects.isNull(realtimeWeather)) {
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }

        return realtimeWeather;
    }
    
}
