package com.weatherforecast.api.service;

import java.util.Objects;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.repository.LocationRepository;
import com.weatherforecast.api.exception.LocationNotFoundException;

public abstract class AbstractLocationService {
    protected LocationRepository locationRepository;

    public Location get(String locationCode) {
        Location location = locationRepository.findByCode(locationCode);
        if (Objects.isNull(location)) {
            throw new LocationNotFoundException(locationCode);
        }
        return location;
    }
}
