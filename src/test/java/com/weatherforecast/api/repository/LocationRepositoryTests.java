package com.weatherforecast.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.weatherforecast.api.entity.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LocationRepositoryTests {
    @Autowired
    private LocationRepository locationRepository;
    @Test
    public void testAddLocationSuccess() {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);
        Location savedLocation = locationRepository.save(location);
        assertNotNull(savedLocation);
        assertEquals(savedLocation.getCode(), "NYC_USA");
        assertEquals(savedLocation.getCityName(), "New York City");
        assertEquals(savedLocation.getRegionName(), "New York");
        assertEquals(savedLocation.getCountryCode(), "US");
        assertEquals(savedLocation.getCountryName(), "United States of America");
    }
}
