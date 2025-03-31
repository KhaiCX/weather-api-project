package com.weatherforecast.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.repository.DailyWeatherRepository;
import com.weatherforecast.api.repository.LocationRepository;

@Service
public class DailyWeatherService {
    private DailyWeatherRepository dailyWeatherRepository;
    private LocationRepository locationRepository;

    public DailyWeatherService(DailyWeatherRepository dailyWeatherRepository, LocationRepository locationRepository) {
        this.dailyWeatherRepository = dailyWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public List<DailyWeather> getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);
        if (Objects.isNull(locationInDB)) {
            throw new LocationNotFoundException(countryCode, cityName);
        }
        return dailyWeatherRepository.findByLocationCode(locationInDB.getCode());
    }

    public List<DailyWeather> getByLocationCode(String locationCode) {
        Location location = locationRepository.findByCode(locationCode);

        if (Objects.isNull(location)) {
            throw new LocationNotFoundException(locationCode);
        }

        return dailyWeatherRepository.findByLocationCode(locationCode);
    }

    public List<DailyWeather> updateByLocationCode(String code, List<DailyWeather> dailyWeatherInRequest) {
        Location location = locationRepository.findByCode(code);

        if (Objects.isNull(location)) {
            throw new LocationNotFoundException(code);
        }

        for (DailyWeather data: dailyWeatherInRequest) {
            data.getId().setLocation(location);
        }

        List<DailyWeather> dailyWeatherInDB = location.getListDailyWeather();
        List<DailyWeather> dailyWeatherToBeRemoved = new ArrayList<>();

        for (DailyWeather dailyForecast: dailyWeatherInDB) {
            if (!dailyWeatherInRequest.contains(dailyForecast)) {
                dailyWeatherToBeRemoved.add(dailyForecast.getShallowCopy());
            }
        }

        for (DailyWeather forecastToBeRemoved: dailyWeatherToBeRemoved) {
            dailyWeatherInDB.remove(forecastToBeRemoved);
        }

        return (List<DailyWeather>) dailyWeatherRepository.saveAll(dailyWeatherInRequest);

    }
}
