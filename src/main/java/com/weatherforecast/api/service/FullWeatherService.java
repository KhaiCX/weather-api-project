package com.weatherforecast.api.service;

import java.time.LocalDateTime;
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
public class FullWeatherService extends AbstractLocationService{
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

    public Location update(String locationCode, Location locationInRequest) {
        Location locationInDB = locationRepository.findByCode(locationCode);

        if (Objects.isNull(locationInDB)) {
            throw new LocationNotFoundException(locationCode);
        }

        setLocationForWeatherData(locationInRequest, locationInDB);
        
        seveRealtimeWeatherIfNotExistBefore(locationInDB, locationInRequest);

        locationInRequest.copyFieldsFrom(locationInDB);

        return locationRepository.save(locationInRequest);
    }

    private void seveRealtimeWeatherIfNotExistBefore(Location locationInDB, Location locationInRequest) {
        if (Objects.isNull(locationInDB.getRealtimeWeather())) {
            locationInDB.setRealtimeWeather(locationInRequest.getRealtimeWeather());
            locationRepository.save(locationInDB);
        }
    }

    private void setLocationForWeatherData(Location locationInRequest, Location locationInDB) {
        RealtimeWeather realtimeWeather = locationInRequest.getRealtimeWeather();
        realtimeWeather.setLocation(locationInDB);
        realtimeWeather.setLastUpdated(LocalDateTime.now());

        List<DailyWeather> listDailyWeathers = locationInRequest.getListDailyWeather();
        listDailyWeathers.forEach(dw -> dw.getId().setLocation(locationInDB));

        List<HourlyWeather> listHourlyWeathers = locationInRequest.getListHourlyWeather();
        listHourlyWeathers.forEach(hw -> hw.getId().setLocation(locationInDB));
    }
}
