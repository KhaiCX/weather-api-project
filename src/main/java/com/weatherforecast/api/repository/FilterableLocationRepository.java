package com.weatherforecast.api.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.weatherforecast.api.entity.Location;

public interface FilterableLocationRepository {
    public Page<Location> listWithFilter(Pageable pageable, Map<String, Object> filterFields);
}
