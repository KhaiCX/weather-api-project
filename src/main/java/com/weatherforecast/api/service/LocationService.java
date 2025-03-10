package com.weatherforecast.api.service;

import org.springframework.stereotype.Service;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {
    private LocationRepository locationRepository;
    public Location add(Location location) {
        return locationRepository.save(location);
    }
}
