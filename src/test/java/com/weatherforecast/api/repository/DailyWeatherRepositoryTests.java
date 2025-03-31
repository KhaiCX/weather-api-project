package com.weatherforecast.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.DailyWeatherId;
import com.weatherforecast.api.entity.Location;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DailyWeatherRepositoryTests {
    @Mock
    private DailyWeatherRepository repository;

    @Test
    public void testAdd() {
        String locationCode = "DANA_VN";

        Location location = new Location().code(locationCode);

        DailyWeather forecast1 = new DailyWeather()
        .location(location)
        .dayOfMonth(16)
        .month(7)
        .minTemp(25)
        .maxTemp(33)
        .precipitation(20)
        .status("Sunny");

        Mockito.when(repository.save(forecast1)).thenReturn(forecast1);
        DailyWeather addForecast = repository.save(forecast1);

        assertEquals(addForecast.getId().getLocation().getCode(), locationCode);
    }

    @Test
    public void testDelete() {
        String locationCode = "DELHI_IN";
        Location location = new Location().code(locationCode);
        DailyWeatherId id = new DailyWeatherId(16, 7, location);
        repository.deleteById(id);
        Mockito.verify(repository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void testFindByLocationCodeFound() {
        String locationCode = "DELHI_IN";
        Mockito.when(repository.findByLocationCode(locationCode)).thenReturn(List.of(new DailyWeather()));
        List<DailyWeather> dailyWeather = repository.findByLocationCode(locationCode);
        assertEquals(dailyWeather.size(), 1);
    }

    @Test
    public void testFindByLocationCodeNotFound() {
        String locationCode = "DELHI_IN";
        Mockito.when(repository.findByLocationCode(locationCode)).thenReturn(Collections.emptyList());
        List<DailyWeather> dailyWeather = repository.findByLocationCode(locationCode);
        assertEquals(dailyWeather.size(), 0);
    }
}
