package com.weatherforecast.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.entity.RealtimeWeather;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RealtimeWeatherRepositoryTests {

    @Mock
    private RealtimeWeatherRepository realtimeWeatherRepository;

    private RealtimeWeather mockRealtimeWeather;

    @BeforeEach
    public void createData() {
        mockRealtimeWeather = new RealtimeWeather();
        mockRealtimeWeather.setLocationCode("NYC_USA");
        mockRealtimeWeather.setHumidity(32);
        mockRealtimeWeather.setPrecipitation(42);
        mockRealtimeWeather.setStatus("Snowy");
        mockRealtimeWeather.setWindSpeed(12);
        mockRealtimeWeather.setLastUpdated(LocalDateTime.now());
    }

    @Test
    public void testUpdate() {

        Mockito.when(realtimeWeatherRepository.save(mockRealtimeWeather)).thenReturn(mockRealtimeWeather);
        RealtimeWeather updatedRealtimeWeather = realtimeWeatherRepository.save(mockRealtimeWeather);
        assertEquals(updatedRealtimeWeather.getLocationCode(), mockRealtimeWeather.getLocationCode());
        assertEquals(updatedRealtimeWeather.getHumidity(), mockRealtimeWeather.getHumidity());
        assertEquals(updatedRealtimeWeather.getTemperature(), mockRealtimeWeather.getTemperature());
        assertEquals(updatedRealtimeWeather.getStatus(), mockRealtimeWeather.getStatus());
        assertEquals(updatedRealtimeWeather.getWindSpeed(), mockRealtimeWeather.getWindSpeed());
    }

    @Test
    public void testFindByCountryCodeAndCityNameNotFound() {
        String countryCode = "JP";
        String cityName = "Tokyo";
        Mockito.when(realtimeWeatherRepository.findByCountryCodeAndCityName(countryCode, cityName)).thenReturn(null);
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCityName(countryCode, cityName);
        assertNull(realtimeWeather);
    }

    @Test
    public void testFindByCountryCodeAndCityNameFound() {
        Location mockLocation = new Location();
        mockLocation.setCode("MBMH_IN");
        mockLocation.setCityName("Munbai");
        mockLocation.setRegionName("Maharashtra");
        mockLocation.setCountryCode("IN");
        mockLocation.setCountryName("India");
        mockLocation.setEnabled(true);
        mockLocation.setTrashed(false);
        mockRealtimeWeather.setLocation(mockLocation);
        Mockito.when(realtimeWeatherRepository.findByCountryCodeAndCityName(mockLocation.getCountryCode(), mockLocation.getCityName())).thenReturn(mockRealtimeWeather);
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCityName(mockLocation.getCountryCode(), mockLocation.getCityName());
        assertNotNull(realtimeWeather);
        assertEquals(realtimeWeather.getLocation().getCountryCode(), mockRealtimeWeather.getLocation().getCountryCode());
        assertEquals(realtimeWeather.getLocation().getCityName(), mockRealtimeWeather.getLocation().getCityName());
    }

}
