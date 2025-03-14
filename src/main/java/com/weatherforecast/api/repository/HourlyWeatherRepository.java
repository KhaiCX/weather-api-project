package com.weatherforecast.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.HourlyWeatherId;
@Repository
public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherId> {

    @Query("SELECT h FROM HourlyWeather h WHERE h.id.location.code = :locationCode AND h.id.hourOfDay > :currentHour AND h.id.location.trashed = false")
    public List<HourlyWeather> findByLocationCode(String locationCode, Integer currentHour);
}
