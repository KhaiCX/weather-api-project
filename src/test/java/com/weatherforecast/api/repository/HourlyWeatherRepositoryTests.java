package com.weatherforecast.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.HourlyWeatherId;
import com.weatherforecast.api.entity.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class HourlyWeatherRepositoryTests {
    @Autowired
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

        HourlyWeather updatedForecast = repository.save(forecast);

        assertEquals(updatedForecast.getId().getLocation().getCode(), locationCode);
        assertEquals(updatedForecast.getId().getHourOfDay(), hourOfDay);
    }

    @Test
    public void testDelete() {
        Location location = new Location().code("NYC_USA");
        HourlyWeatherId id = new HourlyWeatherId(10, location);
        repository.deleteById(id);
        Optional<HourlyWeather> result = repository.findById(id);
        assertEquals(result.isPresent(), false);
    }

    @Test
    public void testFindByLocationCodeFound() {
        String locationCode = "NYC_USA";
        Integer currentHour = 10;

        List<HourlyWeather> listHourlyWeather = repository.findByLocationCode(locationCode, currentHour);

        assertFalse(listHourlyWeather.isEmpty());
    }

    @Test
    public void testFindByLocationCodeNotFound() {
        String locationCode = "NYC_USA";
        Integer currentHour = 25;

        List<HourlyWeather> listHourlyWeather = repository.findByLocationCode(locationCode, currentHour);

        assertTrue(listHourlyWeather.isEmpty());
    }

}
