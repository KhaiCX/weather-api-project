package com.weatherforecast.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.HourlyWeatherId;
@Repository
public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherId> {

}
