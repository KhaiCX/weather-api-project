package com.weatherforecast.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.repository.HourlyWeatherRepository;
import com.weatherforecast.api.repository.LocationRepository;

@Service
public class HourlyWeatherService {

    private HourlyWeatherRepository repository;
    private LocationRepository locationRepository;
    public HourlyWeatherService(HourlyWeatherRepository repository, LocationRepository locationRepository) {
        this.repository = repository;
        this.locationRepository = locationRepository;
    }

    public List<HourlyWeather> getByLocation(Location location, Integer currentHour) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (Objects.isNull(locationInDB)) {
            throw new LocationNotFoundException(location.getCode());
        }

        return repository.findByLocationCode(locationInDB.getCode(), currentHour);
    }

    public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour) throws LocationNotFoundException {
        Location locationInDB = locationRepository.findByCode(locationCode);

        if (Objects.isNull(locationInDB)) {
            throw new LocationNotFoundException(locationCode);
        }
        
        return repository.findByLocationCode(locationCode, currentHour);
    }

    public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyForecastInRequest) throws LocationNotFoundException {
        Location location = locationRepository.findByCode(locationCode);

        if (Objects.isNull(location)) {
            throw new LocationNotFoundException(locationCode);
        }

        for (HourlyWeather item: hourlyForecastInRequest) {
            item.getId().setLocation(location);
        }

        List<HourlyWeather> hourlyWeatherInDB = location.getListHourlyWeather();
        List<HourlyWeather> hourlyWeatherToBeRemoved = new ArrayList<>();

        for (HourlyWeather item: hourlyForecastInRequest) {
            if (!hourlyForecastInRequest.contains(item)) {
                hourlyWeatherToBeRemoved.add(item.getShallowCopy());
            }
        }

        for (HourlyWeather item: hourlyWeatherToBeRemoved) {
            hourlyWeatherInDB.remove(item);
        }

        return (List<HourlyWeather>) repository.saveAll(hourlyForecastInRequest);
    }

}
