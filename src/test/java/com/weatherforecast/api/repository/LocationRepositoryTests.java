package com.weatherforecast.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LocationRepositoryTests {

    @Autowired
    private LocationRepository locationRepository;

    private Location savedLocation;

    @BeforeEach
    public void createData() {
        Location location = new Location();
        location.setCode("MBMH_IN");
        location.setCityName("Munbai");
        location.setRegionName("Maharashtra");
        location.setCountryCode("IN");
        location.setCountryName("India");
        location.setEnabled(true);
        location.setTrashed(false);
        savedLocation = locationRepository.save(location);
    }

    @Test
    public void testAddLocationSuccess() {
        assertNotNull(savedLocation);
        assertEquals(savedLocation.getCode(), "MBMH_IN");
        assertEquals(savedLocation.getCityName(), "Munbai");
        assertEquals(savedLocation.getRegionName(), "Maharashtra");
        assertEquals(savedLocation.getCountryCode(), "IN");
        assertEquals(savedLocation.getCountryName(), "India");
    }

    @Test
    public void testListSuccess() {
        List<Location> locations = locationRepository.findUnTrashed();
        assertNotEquals(locations.size(), 0);
        locations.forEach(System.out::println);
    }

    @Test
    public void testGetNotFound() {
        Location location = locationRepository.findByCode(savedLocation.getCode());
        assertNotNull(location);
    }

    @Test
    public void testGetFound() {
        Location location = locationRepository.findByCode("Test");
        assertNull(location);
    }

    @Test
    public void testTrashSuccess() {
        String code = "NYC_USA";
        locationRepository.trashByCode(code);
        Location location = locationRepository.findByCode(code);
        assertNull(location);
    }

    // @Test
    // public void testAddRealtimeWeatherData() {
    //     String code = "NYC_USA";
    //     Location location = locationRepository.findByCode(code);
    //     RealtimeWeather realtimeWeather = location.getRealtimeWeather();
    //     if (Objects.isNull(realtimeWeather)) {
    //         realtimeWeather = new RealtimeWeather();
    //         realtimeWeather.setLocation(location);
    //         location.setRealtimeWeather(realtimeWeather);
    //     }
    //     realtimeWeather.setTemperature(10);
    //     realtimeWeather.setHumidity(60);
    //     realtimeWeather.setPrecipitation(70);
    //     realtimeWeather.setStatus("Sunny");
    //     realtimeWeather.setLastUpdated(LocalDateTime.now());

    //     Location saveLocation = locationRepository.save(location);
    //     assertEquals(saveLocation.getRealtimeWeather().getLocationCode(), code);
    // }

    @Test
    public void testAddHourlyWeatherData() {
        Location location = locationRepository.findById("NYC_USA").get();

        List<HourlyWeather> listHourlyWeather = location.getListHourlyWeather();

        HourlyWeather forecast1 = new HourlyWeather().id(location, 10)
        .temperature(15)
        .precipitation(40)
        .status("Sunny");
        HourlyWeather forecast2 = new HourlyWeather().location(location)
        .hourOfDay(11)
        .temperature(16)
        .precipitation(50)
        .status("Cloudy");

        listHourlyWeather.add(forecast1);
        listHourlyWeather.add(forecast2);

        Location updatedLocation = locationRepository.save(location);

        assertNotEquals(updatedLocation.getListHourlyWeather().size(), 0);
    }
}
