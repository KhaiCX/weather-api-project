package com.weatherforecast.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.DailyWeatherId;

@Repository
public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {

}
