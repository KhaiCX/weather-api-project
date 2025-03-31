package com.weatherforecast.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.DailyWeatherId;

@Repository
public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {

    @Query("SELECT d FROM DailyWeather d WHERE d.id.location.code = :locationCode AND d.id.location.trashed = false")
    public List<DailyWeather> findByLocationCode(String locationCode);
}
