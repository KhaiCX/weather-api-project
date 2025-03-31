package com.weatherforecast.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherforecast.api.config.TestConfig;
import com.weatherforecast.api.dto.RealtimeWeatherDTO;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.entity.RealtimeWeather;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.service.GeolocationService;
import com.weatherforecast.api.service.RealtimeWeatherService;

import static org.hamcrest.Matchers.is;
@WebMvcTest(RealtimeWeatherController.class)
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RealtimeWeatherControllerTests {

    private static final String END_POINT_PATH = "/v1/realtime";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RealtimeWeatherService realtimeWeatherService;
    @Autowired
    private GeolocationService geolocationService;

    @Test
    public void testGetShouldReturnStatus400BadRequest() throws Exception {
        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(GeolocationException.class);

        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

    @Test
    public void testGetShouldReturnStatus404NotFound() throws Exception {
        Location location = new Location();

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isNotFound())
        .andDo(print());
    }

    @Test
    public void testGetShouldReturnStatus200OK() throws Exception {
        Location location = new Location();
        location.setCode("SFCA_USA");
        location.setCityName("San Franciso");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(12);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(88);
        realtimeWeather.setWindSpeed(5);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setLastUpdated(LocalDateTime.now());

        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getByLocation(location)).thenReturn(realtimeWeather);

        String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryCode();

        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.location", is(expectedLocation)))
        .andDo(print());
    }

    @Test
    public void testGetByLocationCodeShouldReturn404NotFound() throws Exception {
        String code = "ABC";
        
        Mockito.when(realtimeWeatherService.getByLocationCode(code)).thenThrow(LocationNotFoundException.class);

        String requestUrl = END_POINT_PATH + "/" + code;

        mockMvc.perform(get(requestUrl))
        .andExpect(status().isNotFound())
        .andDo(print());
    }

    @Test
    public void testGetByLocationCodeShouldReturnStatus200OK() throws Exception {
        String code = "NYC_USA";

        Location location = new Location();
        location.setCode(code);
        location.setCityName("San Franciso");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(12);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(88);
        realtimeWeather.setWindSpeed(5);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setLastUpdated(LocalDateTime.now());

        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);

        Mockito.when(realtimeWeatherService.getByLocationCode(code)).thenReturn(realtimeWeather);

        String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryCode();

        String requestUrl = END_POINT_PATH + "/" + code;

        mockMvc.perform(get(requestUrl))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.location", is(expectedLocation)))
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception {
        String locationCode = "US";
        String requestUrl = END_POINT_PATH + "/" + locationCode;

        RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
        dto.setTemperature(120);
        dto.setHumidity(132);
        dto.setPrecipitation(188);
        dto.setWindSpeed(500);
        dto.setStatus("Cloudy");
        dto.setLastUpdated(LocalDateTime.now());

        String bodyContent = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put(requestUrl).contentType("application/json").content(bodyContent))
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        String locationCode = "US";
        String requestUrl = END_POINT_PATH + "/" + locationCode;

        RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
        dto.setTemperature(20);
        dto.setHumidity(32);
        dto.setPrecipitation(88);
        dto.setWindSpeed(5);
        dto.setStatus("Cloudy");
        dto.setLastUpdated(LocalDateTime.now());

        Mockito.when(realtimeWeatherService.update(Mockito.eq(locationCode), Mockito.any())).thenThrow(LocationNotFoundException.class);

        String bodyContent = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put(requestUrl).contentType("application/json").content(bodyContent))
        .andExpect(status().isNotFound())
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        String code = "NYC_USA";
        String requestUrl = END_POINT_PATH + "/" + code;

        Location location = new Location();
        location.setCode(code);
        location.setCityName("San Franciso");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setLocationCode(code);
        realtimeWeather.setTemperature(12);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(88);
        realtimeWeather.setWindSpeed(5);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setLastUpdated(LocalDateTime.now());

        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);

        RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
        dto.setTemperature(12);
        dto.setHumidity(32);
        dto.setPrecipitation(88);
        dto.setWindSpeed(5);
        dto.setStatus("Cloudy");
        dto.setLastUpdated(LocalDateTime.now());

        Mockito.when(realtimeWeatherService.update(code, realtimeWeather)).thenReturn(realtimeWeather);

        String bodyContent = objectMapper.writeValueAsString(dto);

        String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryCode();

        mockMvc.perform(put(requestUrl).contentType("application/json").content(bodyContent))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.location", is(expectedLocation)))
        .andDo(print());
    }
}
