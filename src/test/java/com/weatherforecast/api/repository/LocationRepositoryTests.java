package com.weatherforecast.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.entity.RealtimeWeather;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LocationRepositoryTests {

    @Mock
    private LocationRepository locationRepository;

    private Location mockLocation;
    private Location mockLocation2;

    @BeforeEach
    public void createData() {

        mockLocation = new Location()
        .code("MBMH_IN")
        .cityName("Munbai")
        .regionName("Maharashtra")
        .countryCode("IN")
        .countryName("India")
        .enabled(true)
        .trashed(false);

        mockLocation2 = new Location()
        .code("NYC_USA")
        .cityName("Munbai")
        .regionName("Maharashtra")
        .countryCode("IN")
        .countryName("India")
        .enabled(true)
        .trashed(false);
    }

    @Test
    public void testAddLocationSuccess() {
        Mockito.when(locationRepository.save(mockLocation)).thenReturn(mockLocation);
        Location location = locationRepository.save(mockLocation);
        assertNotNull(location);
        assertEquals(location.getCode(), "MBMH_IN");
        assertEquals(location.getCityName(), "Munbai");
        assertEquals(location.getRegionName(), "Maharashtra");
        assertEquals(location.getCountryCode(), "IN");
        assertEquals(location.getCountryName(), "India");
    }

    @Test
    @Disabled
    public void testListSuccess() {
        List<Location> mockLocationList = List.of(mockLocation);
        Mockito.when(locationRepository.findUnTrashed()).thenReturn(mockLocationList);
        List<Location> locations = locationRepository.findUnTrashed();
        assertEquals(locations.size(), mockLocationList.size());
        locations.forEach(System.out::println);
    }

    @Test
    public void testListFirstPage() {
        int pageSize = 1;
        int pageNum = 1;
        List<Location> locations = List.of(mockLocation);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Location> mockPage = new PageImpl<>(locations, pageable, locations.size());

        Mockito.when(locationRepository.findUnTrashed(pageable)).thenReturn(mockPage);

        Page<Location> page = locationRepository.findUnTrashed(pageable);
        assertEquals(page.getSize(), mockPage.getSize());
        assertEquals(page.getTotalPages(), mockPage.getTotalPages());
    }

    @Test
    public void testListPageNoContent() {
        int pageSize = 1;
        int pageNum = 10;
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Location> mockPage = new PageImpl<>(Collections.emptyList(), pageable, Collections.emptyList().size());
        Mockito.when(locationRepository.findUnTrashed(pageable)).thenReturn(mockPage);
        Page<Location> page = locationRepository.findUnTrashed(pageable);
        assertTrue(page.isEmpty());
    }

    @Test
    public void testList2ndPageWithSort() {
        int pageSize = 1;
        int pageNum = 0;

        Sort sort = Sort.by("code").ascending();

        List<Location> locations = List.of(mockLocation, mockLocation2);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Location> mockPage = new PageImpl<>(locations, pageable, locations.size());

        Mockito.when(locationRepository.findUnTrashed(pageable)).thenReturn(mockPage);

        Page<Location> page = locationRepository.findUnTrashed(pageable);
        assertEquals(page.getSize(), mockPage.getSize());
        assertEquals(page.getTotalPages(), mockPage.getTotalPages());
        assertEquals(page.getSize(), pageSize);
    }

    @Test
    public void testGetNotFound() {
        String code = "MBMH_IN";
        Mockito.when(locationRepository.findByCode(code)).thenReturn(mockLocation);
        Location location = locationRepository.findByCode(code);
        assertNotNull(location);
        assertEquals(mockLocation.getCode(), location.getCode());
    }

    @Test
    public void testGetFound() {
        String code = "codeTest";
        Mockito.when(locationRepository.findByCode(code)).thenReturn(null);
        Location location = locationRepository.findByCode(code);
        assertNull(location);
    }

    @Test
    public void testTrashSuccess() {
        String code = "MBMH_IN";
        locationRepository.trashByCode(code);
        Mockito.verify(locationRepository, Mockito.times(1)).trashByCode(code);
    }

    @Test
    public void testAddRealtimeWeatherData() {
        RealtimeWeather realtimeWeather = mockLocation.getRealtimeWeather();
        if (Objects.isNull(realtimeWeather)) {
            realtimeWeather = new RealtimeWeather();
            realtimeWeather.setLocation(mockLocation);
            mockLocation.setRealtimeWeather(realtimeWeather);
        }
        realtimeWeather.setTemperature(10);
        realtimeWeather.setHumidity(60);
        realtimeWeather.setPrecipitation(70);
        realtimeWeather.setStatus("Sunny");
        realtimeWeather.setLastUpdated(LocalDateTime.now());

        Mockito.when(locationRepository.save(mockLocation)).thenReturn(mockLocation);
        Location saveLocation = locationRepository.save(mockLocation);
        assertEquals(saveLocation.getRealtimeWeather().getLocationCode(), mockLocation.getRealtimeWeather().getLocationCode());
    }

    @Test
    public void testAddHourlyWeatherData() {

        List<HourlyWeather> listHourlyWeather = mockLocation.getListHourlyWeather();

        HourlyWeather forecast1 = new HourlyWeather().id(mockLocation, 10)
        .temperature(15)
        .precipitation(40)
        .status("Sunny");
        HourlyWeather forecast2 = new HourlyWeather().location(mockLocation)
        .hourOfDay(11)
        .temperature(16)
        .precipitation(50)
        .status("Cloudy");

        listHourlyWeather.add(forecast1);
        listHourlyWeather.add(forecast2);

        Mockito.when(locationRepository.save(mockLocation)).thenReturn(mockLocation);
        Location updatedLocation = locationRepository.save(mockLocation);

        assertNotEquals(updatedLocation.getListHourlyWeather().size(), 0);
    }

    @Test
    public void testFindByCountryCodeAndCityNameNotFound() {
        String countryCode = "AB";
        String cityName = "City";

        Mockito.when(locationRepository.findByCountryCodeAndCityName(countryCode, cityName)).thenReturn(null);
        Location location = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        assertNull(location);
    }

    @Test
    public void testFindByCountryCodeAndCityNameFound() {

        String countryCode = "IN";
        String cityName = "Munbai";
        Mockito.when(locationRepository.findByCountryCodeAndCityName(countryCode, cityName)).thenReturn(mockLocation);
        Location location = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        assertNotNull(location);
        assertEquals(location.getCountryCode(), mockLocation.getCountryCode());
        assertEquals(location.getCityName(), mockLocation.getCityName());
    }

    @Test
    public void testAddDailyWeatherData() {
        List<DailyWeather> listDailyWeathers = mockLocation.getListDailyWeather();
        DailyWeather forecast1 = new DailyWeather()
        .location(mockLocation)
        .dayOfMonth(16)
        .month(7)
        .minTemp(25)
        .maxTemp(33)
        .precipitation(20)
        .status("Sunny");

        DailyWeather forecast2 = new DailyWeather()
        .location(mockLocation)
        .dayOfMonth(17)
        .month(7)
        .minTemp(26)
        .maxTemp(34)
        .precipitation(10)
        .status("Clear");

        listDailyWeathers.add(forecast1);
        listDailyWeathers.add(forecast2);

        Mockito.when(locationRepository.save(mockLocation)).thenReturn(mockLocation);
        Location updatedLocation = locationRepository.save(mockLocation);
        assertEquals(updatedLocation.getListDailyWeather().size(), mockLocation.getListDailyWeather().size());
    }
}
