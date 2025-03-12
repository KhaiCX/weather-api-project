package com.weatherforecast.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.weatherforecast.api.entity.RealtimeWeather;

public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String> {


}
