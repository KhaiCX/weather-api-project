package com.weatherforecast.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.weatherforecast.api.entity.RealtimeWeather;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RealtimeWeatherRepositoryTests {

    @Autowired
    private RealtimeWeatherRepository realtimeWeatherRepository;

    @Test
    public void testUpdate() {
        String code = "NYC_USA";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findById(code).get();
                realtimeWeather.setTemperature(-2);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(42);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(12);
        realtimeWeather.setLastUpdated(LocalDateTime.now());

        RealtimeWeather updatedRealtimeWeather = realtimeWeatherRepository.save(realtimeWeather);
        assertEquals(updatedRealtimeWeather.getHumidity(), 32);
    }

}
