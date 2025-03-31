package com.weatherforecast.api.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.entity.RealtimeWeather;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.repository.LocationRepository;
import com.weatherforecast.api.repository.RealtimeWeatherRepository;

@Service
public class RealtimeWeatherService {

    private RealtimeWeatherRepository realtimeWeatherRepository;
    private LocationRepository locationRepository;

    public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository,
    LocationRepository locationRepository) {
        this.realtimeWeatherRepository = realtimeWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {

        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (Objects.isNull(realtimeWeather)) {
            throw new LocationNotFoundException(countryCode, cityName);
        }

        return realtimeWeather;
    }

    public RealtimeWeather getByLocationCode(String locationCode) throws LocationNotFoundException {

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);

        if (Objects.isNull(realtimeWeather)) {
            throw new LocationNotFoundException(locationCode);
        }

        return realtimeWeather;
    }

    public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather) throws LocationNotFoundException {
        Location location = locationRepository.findByCode(locationCode);

        if (Objects.isNull(location)) {
            throw new LocationNotFoundException(locationCode);
        }

        realtimeWeather.setLocation(location);
        realtimeWeather.setLastUpdated(LocalDateTime.now());

        if (Objects.isNull(location.getRealtimeWeather())) {
            location.setRealtimeWeather(realtimeWeather);
            Location locationUpdated = locationRepository.save(location);
            return locationUpdated.getRealtimeWeather();
        }

        return realtimeWeatherRepository.save(realtimeWeather);
    }
    
}
