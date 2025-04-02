package com.weatherforecast.api.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.entity.RealtimeWeather;
import com.weatherforecast.api.repository.LocationRepository;
import com.weatherforecast.api.exception.LocationNotFoundException;

@Service
public class FullWeatherService {
    private LocationRepository locationRepository;
    public FullWeatherService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getByLocation(Location locationFromIP) {
        String countryCode = locationFromIP.getCountryCode();
        String cityName = locationFromIP.getCityName();

        Location locationInDB = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (Objects.isNull(locationInDB)) {
            throw new LocationNotFoundException(countryCode, cityName);
        }
        return locationInDB;
    }

    public Location get(String locationCode) {
        Location location = locationRepository.findByCode(locationCode);
        if (Objects.isNull(location)) {
            throw new LocationNotFoundException(locationCode);
        }
        return location;
    }

    public Location update(String locationCode, Location locationInRequest) {
        Location locationInDB = locationRepository.findByCode(locationCode);

        if (Objects.isNull(locationInDB)) {
            throw new LocationNotFoundException(locationCode);
        }

        RealtimeWeather realtimeWeather = locationInRequest.getRealtimeWeather();
        realtimeWeather.setLocation(locationInDB);

        List<DailyWeather> listDailyWeathers = locationInRequest.getListDailyWeather();
        listDailyWeathers.forEach(dw -> dw.getId().setLocation(locationInDB));

        List<HourlyWeather> listHourlyWeathers = locationInRequest.getListHourlyWeather();
        listHourlyWeathers.forEach(hw -> hw.getId().setLocation(locationInDB));

        locationInRequest.setCode(locationInDB.getCode());
        locationInRequest.setCityName(locationInDB.getCityName());
        locationInRequest.setRegionName(locationInDB.getRegionName());
        locationInRequest.setCountryCode(locationInDB.getCountryCode());
        locationInRequest.setCountryName(locationInDB.getCountryName());
        locationInRequest.setTrashed(locationInDB.getTrashed());
        locationInRequest.setEnabled(locationInDB.getEnabled());

        return locationRepository.save(locationInRequest);
    }
}
