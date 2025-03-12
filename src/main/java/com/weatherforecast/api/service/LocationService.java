package com.weatherforecast.api.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.repository.LocationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
public class LocationService {

    private LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location add(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> list() {
        return locationRepository.findUnTrashed();
    }

    public Location get(String code) {
        return locationRepository.findByCode(code);
    }

    public Location update(Location locationInRequest) throws LocationNotFoundException {
        String code = locationInRequest.getCode();

        Location location = locationRepository.findByCode(code);

        if (Objects.isNull(location)) {
            throw new LocationNotFoundException("Location not found with code: " + code);
        }

        location.setCityName(locationInRequest.getCityName());
        location.setRegionName(locationInRequest.getRegionName());
        location.setCountryCode(locationInRequest.getCountryCode());
        location.setCountryName(locationInRequest.getCountryName());
        location.setEnabled(locationInRequest.getEnabled());
        return locationRepository.save(location);
    }

    public void delete(String code) throws LocationNotFoundException {
        Location location = locationRepository.findByCode(code);

        if (Objects.isNull(location)) {
            throw new LocationNotFoundException("Location not found with code: " + code);
        }

        locationRepository.trashByCode(code);;
    }
    
}
