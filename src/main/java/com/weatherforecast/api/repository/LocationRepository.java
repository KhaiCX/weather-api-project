package com.weatherforecast.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.weatherforecast.api.entity.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, String> {

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    public List<Location> findUnTrashed();

    @Query("SELECT l FROM Location l WHERE l.code = :code and l.trashed = false")
    public Location findByCode(String code);

    @Modifying
    @Query("UPDATE Location l SET l.trashed = true WHERE l.code = :code")
    public void trashByCode(String code);

    @Query("SELECT l FROM Location l WHERE l.countryCode = :countryCode and l.cityName = :cityName and l.trashed = false")
    public Location findByCountryCodeAndCityName(String countryCode, String cityName);

}
