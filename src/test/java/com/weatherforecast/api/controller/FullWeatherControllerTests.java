package com.weatherforecast.api.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherforecast.api.common.FullWeatherModelAssembler;
import com.weatherforecast.api.config.TestConfig;
import com.weatherforecast.api.dto.DailyWeatherDTO;
import com.weatherforecast.api.dto.HourlyWeatherDTO;
import com.weatherforecast.api.dto.FullWeatherDTO;
import com.weatherforecast.api.dto.RealtimeWeatherDTO;
import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.entity.RealtimeWeather;
import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.service.FullWeatherService;
import com.weatherforecast.api.service.GeolocationService;

@WebMvcTest(FullWeatherController.class)
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FullWeatherControllerTests {
    private static final String END_POINT_PATH = "/v1/full";
    private static final String RESPONSE_CONTENT_TYPE = "application/hal+json";
    private static final String REQUEST_CONTENT_TYPE = "application/json";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GeolocationService geolocationService;
    @Autowired
    private FullWeatherService fullWeatherService;
    @SuppressWarnings("removal")
    @SpyBean
    private FullWeatherModelAssembler fullWeatherModelAssembler;

    private Location location;

    private RealtimeWeather realtimeWeather;
    private DailyWeather dailyWeather;
    private HourlyWeather hourlyWeather;

    private RealtimeWeatherDTO realtimeWeatherDTO;
    private DailyWeatherDTO dailyWeatherDTO;
    private HourlyWeatherDTO hourlyWeatherDTO;

    @BeforeEach
    public void createData() {

        location = new Location()
        .code("NYC_USA")
        .cityName("New York City")
        .regionName("New York")
        .countryCode("US")
        .countryName("United States of America")
        .enabled(true)
        .trashed(false);

        realtimeWeather = new RealtimeWeather()
        .temperature(50)
        .humidity(100)
        .precipitation(100)
        .status("cloudy")
        .windSpeed(500)
        .lastUpdated(LocalDateTime.now());

        dailyWeather = new DailyWeather()
        .location(location)
        .dayOfMonth(16)
        .month(7)
        .minTemp(23)
        .maxTemp(32)
        .precipitation(40)
        .status("Cloudy");

        hourlyWeather = new HourlyWeather()
        .location(location)
        .hourOfDay(10)
        .temperature(13)
        .precipitation(17)
        .status("Cloudy");

        realtimeWeatherDTO = new RealtimeWeatherDTO()
        .temperature(50)
        .humidity(100)
        .precipitation(100)
        .status("cloudy")
        .windSpeed(500)
        .lastUpdated(LocalDateTime.now());

        dailyWeatherDTO = new DailyWeatherDTO()
        .dayOfMonth(30)
        .month(7)
        .minTemp(23)
        .maxTemp(30)
        .precipitation(20)
        .status("Clear");

        hourlyWeatherDTO = new HourlyWeatherDTO()
        .hourOfDay(10)
        .temperature(13)
        .precipitation(17)
        .status("Cloudy");

        location.setRealtimeWeather(realtimeWeather);
        location.setListDailyWeather(List.of(dailyWeather));
        location.setListHourlyWeather(List.of(hourlyWeather));
    }

    @Test
    public void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(GeolocationException.class);
        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn404NotFound() throws Exception {
        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        when(fullWeatherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isNotFound())
        .andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn200OK() throws Exception {
        location.setListDailyWeather(List.of(dailyWeather));
        location.setRealtimeWeather(realtimeWeather);
        location.setListHourlyWeather(List.of(hourlyWeather));

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        when(fullWeatherService.getByLocation(location)).thenReturn(location);

        String expectedLocation = location.toString();
        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isOk())
        .andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
        .andExpect(jsonPath("$.location", is(expectedLocation)))
        .andExpect(jsonPath("$.realtime_weather.temperature", is(50)))
        .andExpect(jsonPath("$.hourly_weather[0].hour_of_day", is(10)))
        .andExpect(jsonPath("$.daily_weather[0].day_of_month", is(16)))
        .andExpect(jsonPath("$._links.self.href", is("http://localhost/v1/full")))
        .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn404NotFound() throws Exception {
        String locationCode = "NYC_USA";
        String requestUrl = END_POINT_PATH + "/" + locationCode;

        when(fullWeatherService.get(locationCode)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(requestUrl))
        .andExpect(status().isNotFound())
        .andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn200OK() throws Exception {
        String requestUrl = END_POINT_PATH + "/" + location.getCode();

        location.setListDailyWeather(List.of(dailyWeather));
        location.setRealtimeWeather(realtimeWeather);
        location.setListHourlyWeather(List.of(hourlyWeather));

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        when(fullWeatherService.get(location.getCode())).thenReturn(location);
        String expectedLocation = location.toString();

        mockMvc.perform(get(requestUrl))
        .andExpect(status().isOk())
        .andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
        .andExpect(jsonPath("$.location", is(expectedLocation)))
        .andExpect(jsonPath("$.realtime_weather.temperature", is(50)))
        .andExpect(jsonPath("$.hourly_weather[0].hour_of_day", is(10)))
        .andExpect(jsonPath("$.daily_weather[0].day_of_month", is(16)))
        .andExpect(jsonPath("$._links.self.href", is("http://localhost/v1/full/" + location.getCode())))
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseNoHourlyWeather() throws Exception {
        String requestUrl = END_POINT_PATH + "/" + location.getCode();

        FullWeatherDTO dto = new FullWeatherDTO();
        dto.setLocation(location.toString());
        String requestBody = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put(requestUrl).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
        .andExpect(status().isBadRequest())
        //.andExpect(jsonPath("$.errors[0]", is("Hourly Weather data cannot be empty")))
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseNoDailyWeather() throws Exception {
        String requestUrl = END_POINT_PATH + "/NYC_USA";

        FullWeatherDTO dto = new FullWeatherDTO();

        dto.getListHourlyWeather().add(hourlyWeatherDTO);
        String requestBody = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put(requestUrl).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
        .andExpect(status().isBadRequest())
        //.andExpect(jsonPath("$.errors[0]", is("Daily Weather data cannot be empty")))
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseInvalidRealtimeWeather() throws Exception {
        String requestUrl = END_POINT_PATH + "/NYC_USA";

        FullWeatherDTO dto = new FullWeatherDTO();

        realtimeWeatherDTO.setTemperature(100);
        dto.setRealtimeWeather(realtimeWeatherDTO);
        dto.getListDailyWeather().add(dailyWeatherDTO);
        dto.getListHourlyWeather().add(hourlyWeatherDTO);

        String requestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(requestUrl).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
        .andExpect(status().isBadRequest())
        //.andExpect(jsonPath("$.errors[0]", containsString("Temperator must be in the range of -50 to 50 Celsius degree")))
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseInvalidHourlyWeather() throws Exception {
        String requestUrl = END_POINT_PATH + "/" + location.getCode();

        FullWeatherDTO dto = new FullWeatherDTO();

        hourlyWeatherDTO.setHourOfDay(100);
        dto.setRealtimeWeather(realtimeWeatherDTO);
        dto.getListDailyWeather().add(dailyWeatherDTO);
        dto.getListHourlyWeather().add(hourlyWeatherDTO);

        String requestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(requestUrl).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0]", containsString("Hourly of day must be in the range of 0 to 23")))
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseInvalidDailyWeather() throws Exception {
        String requestUrl = END_POINT_PATH + "/" + location.getCode();

        FullWeatherDTO dto = new FullWeatherDTO();

        dailyWeatherDTO.setStatus("");
        dto.setRealtimeWeather(realtimeWeatherDTO);
        dto.getListDailyWeather().add(dailyWeatherDTO);
        dto.getListHourlyWeather().add(hourlyWeatherDTO);

        String requestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(requestUrl).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0]", containsString("Status must be in between 3-50 characters")))
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        String requestUrl = END_POINT_PATH + "/" + location.getCode();

        FullWeatherDTO dto = new FullWeatherDTO();

        dto.setRealtimeWeather(realtimeWeatherDTO);
        dto.getListDailyWeather().add(dailyWeatherDTO);
        dto.getListHourlyWeather().add(hourlyWeatherDTO);

        String requestBody = objectMapper.writeValueAsString(dto);

        when(fullWeatherService.update(Mockito.eq(location.getCode()), Mockito.any())).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(put(requestUrl).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
        .andExpect(status().isNotFound())
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        String requestUrl = END_POINT_PATH + "/" + location.getCode();

        FullWeatherDTO dto = new FullWeatherDTO();

        dto.setRealtimeWeather(realtimeWeatherDTO);
        dto.getListDailyWeather().add(dailyWeatherDTO);
        dto.getListHourlyWeather().add(hourlyWeatherDTO);

        String requestBody = objectMapper.writeValueAsString(dto);

        when(fullWeatherService.update(Mockito.eq(location.getCode()), Mockito.any())).thenReturn(location);
        mockMvc.perform(put(requestUrl).contentType(RESPONSE_CONTENT_TYPE).content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._links.self.href", is("http://localhost/v1/full/" + location.getCode())))
        .andDo(print());
    }
}
