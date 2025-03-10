package com.weatherforecast.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.weatherforecast.api.entity.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, String> {

}
