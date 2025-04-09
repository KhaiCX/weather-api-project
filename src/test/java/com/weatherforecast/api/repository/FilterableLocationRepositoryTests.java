package com.weatherforecast.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.weatherforecast.api.entity.Location;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FilterableLocationRepositoryTests {
    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testListWithDefault() {
        int pageSize = 2;
        int pageNum = 0;
        String sortField = "code";

        Sort sort = Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Location> page = locationRepository.listWithFilter(pageable, Collections.emptyMap());

        List<Location> content = page.getContent();
        List<Location> expectContent = List.of(new Location().code("MBMH_IN"), new Location().code("NYC_USA"));
        content.forEach(System.out::println);
        assertEquals(content.size(), pageSize);
        assertEquals(content.get(0).getCode(), expectContent.get(0).getCode());
        assertEquals(content.get(1).getCode(), expectContent.get(1).getCode());

    }

    @Test
    public void testListNoFilterSortedByCityName() {
        int pageSize = 2;
        int pageNum = 0;
        String sortField = "cityName";

        Sort sort = Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Location> page = locationRepository.listWithFilter(pageable, Collections.emptyMap());
        
        List<Location> content = page.getContent();
        List<Location> expectContent = List.of(new Location().code("MBMH_IN").cityName("Munbai"), new Location().code("NYC_USA").cityName("New York City"));
        content.forEach(System.out::println);
        assertEquals(content.size(), pageSize);
        assertEquals(content.get(0).getCityName(), expectContent.get(0).getCityName());
        assertEquals(content.get(1).getCityName(), expectContent.get(1).getCityName());

    }

    @Test
    public void testListFilteredRegionNameSortedByCityName() {
        int pageSize = 2;
        int pageNum = 0;
        String sortField = "cityName";
        String regionName = "regionName";

        Sort sort = Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Map<String, Object> filterFields = new HashMap<>();
        filterFields.put("regionName", regionName);
        Page<Location> page = locationRepository.listWithFilter(pageable, filterFields);
        
        List<Location> content = page.getContent();
        List<Location> expectContent = List.of(
            new Location().code("MBMH_IN")
            .cityName("Munbai")
            .regionName("Maharashtra"),
            new Location().code("NYC_USA")
            .cityName("New York City")
            .regionName("New York"));
        content.forEach(System.out::println);
        assertEquals(content.size(), pageSize);
        assertEquals(content.get(0).getCityName(), expectContent.get(0).getCityName());
        assertEquals(content.get(1).getCityName(), expectContent.get(1).getCityName());
        assertEquals(content.get(0).getRegionName(), expectContent.get(0).getRegionName());
        assertEquals(content.get(1).getRegionName(), expectContent.get(1).getRegionName());

    }

    @Test
    public void testListFilteredCountryCodeSortedByCode() {
        int pageSize = 2;
        int pageNum = 0;
        String sortField = "cityName";
        String countryCode = "US";
        Boolean enabled = true;

        Sort sort = Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Map<String, Object> filterFields = new HashMap<>();
        filterFields.put("countryCode", countryCode);
        filterFields.put("enabled", enabled);
        Page<Location> page = locationRepository.listWithFilter(pageable, filterFields);
        
        List<Location> content = page.getContent();
        List<Location> expectContent = List.of(
            new Location().code("MBMH_IN")
            .cityName("Munbai")
            .regionName("Maharashtra")
            .countryCode("IN")
            .enabled(true),
            new Location().code("NYC_USA")
            .cityName("New York City")
            .regionName("New York")
            .countryCode("US")
            .enabled(true));
        content.forEach(System.out::println);
        assertEquals(content.size(), pageSize);
        assertEquals(content.get(0).getCountryCode(), expectContent.get(0).getCountryCode());
        assertEquals(content.get(1).getCountryCode(), expectContent.get(1).getCountryCode());
        assertEquals(content.get(0).getEnabled(), expectContent.get(0).getEnabled());
        assertEquals(content.get(1).getEnabled(), expectContent.get(1).getEnabled());

    }
}
