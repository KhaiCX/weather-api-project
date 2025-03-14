package com.weatherforecast.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.HourlyWeatherId;
import com.weatherforecast.api.entity.Location;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class HourlyWeatherRepositoryTests {
    @Mock
    private HourlyWeatherRepository repository;

    @Test
    public void testAdd() {
        String locationCode = "NYC_USA";
        Integer hourOfDay = 12;

        Location location = new Location().code(locationCode);

        HourlyWeather forecast = new HourlyWeather()
        .location(location)
        .hourOfDay(hourOfDay)
        .temperature(13)
        .precipitation(17)
        .status("Cloudy");

        Mockito.when(repository.save(forecast)).thenReturn(forecast);
        HourlyWeather updatedForecast = repository.save(forecast);

        assertEquals(updatedForecast.getId().getLocation().getCode(), forecast.getId().getLocation().getCode());
        assertEquals(updatedForecast.getId().getHourOfDay(), forecast.getId().getHourOfDay());
    }

    @Test
    public void testDelete() {
        Location location = new Location().code("NYC_USA");
        HourlyWeatherId id = new HourlyWeatherId(10, location);
        repository.deleteById(id);
        Mockito.verify(repository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void testFindByLocationCodeFound() {

        String locationCode = "NYC_USA";
        Integer currentHour = 10;

        Location location = new Location().code(locationCode);

        HourlyWeather forecast = new HourlyWeather()
        .location(location)
        .hourOfDay(currentHour)
        .temperature(13)
        .precipitation(17)
        .status("Cloudy");

        Mockito.when(repository.findByLocationCode(locationCode, currentHour)).thenReturn(List.of(forecast));
        List<HourlyWeather> listHourlyWeather = repository.findByLocationCode(locationCode, currentHour);

        assertFalse(listHourlyWeather.isEmpty());
    }

    @Test
    public void testFindByLocationCodeNotFound() {
        String locationCode = "NYC_USA";
        Integer currentHour = 25;

        Mockito.when(repository.findByLocationCode(locationCode, currentHour)).thenReturn(Collections.emptyList());
        List<HourlyWeather> listHourlyWeather = repository.findByLocationCode(locationCode, currentHour);

        assertTrue(listHourlyWeather.isEmpty());
    }

}
