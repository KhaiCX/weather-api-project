package com.weatherforecast.api.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.repository.LocationRepository;
import com.weatherforecast.api.exception.LocationNotFoundException;

@Service
public class FullWeatherService {
    private LocationRepository locationRepository;
    public FullWeatherService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getByLocation(Location locationFromIP) {
        String countryCode = locationFromIP.getCountryCode();
        String cityName = locationFromIP.getCityName();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (Objects.isNull(locationInDB)) {
            throw new LocationNotFoundException(countryCode, cityName);
        }
        return locationInDB;
    }

    public Location get(String locationCode) {
        Location location = locationRepository.findByCode(locationCode);
        if (Objects.isNull(location)) {
            throw new LocationNotFoundException(locationCode);
        }
        return location;
    }
}
