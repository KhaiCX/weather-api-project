package com.weatherforecast.api.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.repository.HourlyWeatherRepository;
import com.weatherforecast.api.repository.LocationRepository;

@Service
public class HourlyWeatherService {

    private HourlyWeatherRepository repository;
    private LocationRepository locationRepository;
    public HourlyWeatherService(HourlyWeatherRepository repository, LocationRepository locationRepository) {
        this.repository = repository;
        this.locationRepository = locationRepository;
    }

    public List<HourlyWeather> getByLocation(Location location, Integer currentHour) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (Objects.isNull(locationInDB)) {
            throw new LocationNotFoundException("Location not found with the given country code and city name");
        }

        return repository.findByLocationCode(locationInDB.getCode(), currentHour);
    }

    public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour) throws LocationNotFoundException {
        Location locationInDB = locationRepository.findByCode(locationCode);

        if (Objects.isNull(locationInDB)) {
            throw new LocationNotFoundException("No location found with the given code: " + locationCode);
        }
        
        return repository.findByLocationCode(locationCode, currentHour);
    }

    public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyForecastInRequest) throws LocationNotFoundException {
        Location location = locationRepository.findByCode(locationCode);

        if (Objects.isNull(location)) {
            throw new LocationNotFoundException("No location not found with given code: " + locationCode);
        }
        return Collections.emptyList();
    }

}
