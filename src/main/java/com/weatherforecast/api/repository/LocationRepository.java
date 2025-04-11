package com.weatherforecast.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.weatherforecast.api.entity.Location;

@Repository
public interface LocationRepository extends FilterableLocationRepository, CrudRepository<Location, String>, PagingAndSortingRepository<Location, String> {

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    @Deprecated
    List<Location> findUnTrashed();

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    @Deprecated
    public Page<Location> findUnTrashed(Pageable pageable);

    @Query("SELECT l FROM Location l WHERE l.code = :code and l.trashed = false")
    public Location findByCode(String code);

    @Modifying
    @Query("UPDATE Location l SET l.trashed = true WHERE l.code = :code")
    public void trashByCode(String code);

    @Query("SELECT l FROM Location l WHERE l.countryCode = :countryCode and l.cityName = :cityName and l.trashed = false")
    public Location findByCountryCodeAndCityName(String countryCode, String cityName);

}
