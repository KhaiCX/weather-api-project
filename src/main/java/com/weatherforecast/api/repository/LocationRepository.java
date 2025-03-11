package com.weatherforecast.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.weatherforecast.api.entity.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, String> {

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    public List<Location> findUnTrashed();

    @Query("SELECT l FROM Location l WHERE l.code = :code")
    public Location findByCode(String code);

}
