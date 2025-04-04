package com.weatherforecast.api.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.repository.LocationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LocationService extends AbstractLocationService {

    private LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location add(Location location) {
        return locationRepository.save(location);
    }

    @Deprecated
    public List<Location> list() {
        return locationRepository.findUnTrashed();
    }

    public Page<Location> listByPage(Integer pageSize, Integer pageNum, String sortField) {
        Sort sort = Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        return locationRepository.findUnTrashed(pageable);
    }

    public Location update(Location locationInRequest) {
        String code = locationInRequest.getCode();

        Location location = locationRepository.findByCode(code);

        if (Objects.isNull(location)) {
            throw new LocationNotFoundException(code);
        }

        location.copyFieldsFrom(locationInRequest);
        return locationRepository.save(location);
    }

    public void delete(String code) {
        Location location = locationRepository.findByCode(code);

        if (Objects.isNull(location)) {
            throw new LocationNotFoundException(code);
        }

        locationRepository.trashByCode(code);;
    }
    
}
