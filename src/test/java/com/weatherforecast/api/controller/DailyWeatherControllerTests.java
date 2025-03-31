package com.weatherforecast.api.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.weatherforecast.api.config.TestConfig;
import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.GeolocationException;
import com.weatherforecast.api.service.DailyWeatherService;
import com.weatherforecast.api.service.GeolocationService;

@WebMvcTest(DailyWeatherController.class)
@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DailyWeatherControllerTests {
    private static final String END_POINT_PATH = "/v1/daily";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GeolocationService geolocationService;
    @Autowired
    private DailyWeatherService dailyWeatherService;

    @Test
    public void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenThrow(GeolocationException.class);
        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn204NoContent() throws Exception {

        Location location = new Location().code("DELHI_IN");

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);

        Mockito.when(dailyWeatherService.getByLocation(location)).thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isNoContent())
        .andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn200OK() throws Exception {

        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setListDailyWeathers(Collections.emptyList());

        DailyWeather forecast1 = new DailyWeather()
        .location(location)
        .dayOfMonth(16)
        .month(7)
        .minTemp(23)
        .maxTemp(32)
        .precipitation(40)
        .status("Cloudy");

        DailyWeather forecast2 = new DailyWeather()
        .location(location)
        .dayOfMonth(17)
        .month(7)
        .minTemp(25)
        .maxTemp(34)
        .precipitation(30)
        .status("Sunny");

        location.setListDailyWeathers(List.of(forecast1, forecast2));

        Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
        when(dailyWeatherService.getByLocation(location)).thenReturn(location.getListDailyWeathers());

        String expectedLocation = location.toString();
        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.location", is(expectedLocation)))
        .andExpect(jsonPath("$.daily_forecast[0].day_of_month", is(16)))
        .andDo(print());
    }

    // @Test
    // public void testGetByCodeShouldReturn400BadRequest() throws Exception {
    //     String locationCode = "NYC_USA";
    //     String requestUrl = END_POINT_PATH + "/" + locationCode;
    //     mockMvc.perform(get(requestUrl))
    //     .andExpect(status().isBadRequest())
    //     .andDo(print());
    // }

    // @Test
    // public void testGetByCodeShouldReturn404NotFound() throws Exception {
    //     Integer currentHour = 9;
    //     String locationCode = "NYC_USA";
    //     String requestUrl = END_POINT_PATH + "/" + locationCode;

    //     //when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenThrow(LocationNotFoundException.class);

    //     mockMvc.perform(get(requestUrl).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
    //     .andExpect(status().isNotFound())
    //     .andDo(print());
    // }

    // @Test
    // public void testGetByCodeShouldReturn204NoContent() throws Exception {
    //     Integer currentHour = 9;
    //     String locationCode = "NYC_USA";
    //     String requestUrl = END_POINT_PATH + "/" + locationCode;

    //     when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenReturn(Collections.emptyList());

    //     mockMvc.perform(get(requestUrl).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
    //     .andExpect(status().isNoContent())
    //     .andDo(print());
    // }

    // @Test
    // public void testGetByCodeShouldReturn200OK() throws Exception {
    //     Integer currentHour = 9;
    //     String locationCode = "NYC_USA";
    //     String requestUrl = END_POINT_PATH + "/" + locationCode;
    //     Location location = new Location();
    //     location.setCode(locationCode);
    //     location.setCityName("New York City");
    //     location.setRegionName("New York");
    //     location.setCountryCode("US");
    //     location.setCountryName("United States of America");

    //     HourlyWeather forecast1 = new HourlyWeather()
    //     .location(location)
    //     .hourOfDay(10)
    //     .temperature(13)
    //     .precipitation(17)
    //     .status("Cloudy");

    //     HourlyWeather forecast2 = new HourlyWeather()
    //     .location(location)
    //     .hourOfDay(11)
    //     .temperature(15)
    //     .precipitation(60)
    //     .status("Sunny");

    //     when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenReturn(List.of(forecast1, forecast2));

    //     mockMvc.perform(get(requestUrl).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
    //     .andExpect(status().isOk())
    //     .andExpect(content().contentType("application/json"))
    //     .andExpect(jsonPath("$.location", is(location.toString())))
    //     .andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))
    //     .andDo(print());
    // }

    // @Test
    // public void testUpdateShouldReturn400BadRequestBecauseNoData() throws Exception {
    //     String requestUrl = END_POINT_PATH + "/NYC_USA";
    //     List<HourlyWeatherDTO> listDTO = Collections.emptyList();
    //     String requestBody = objectMapper.writeValueAsString(listDTO);
    //     mockMvc.perform(put(requestUrl).contentType("application/json").content(requestBody))
    //     .andExpect(status().isBadRequest())
    //     .andExpect(jsonPath("$.errors[0]", is("Hourly forecast data cannot be empty")))
    //     .andDo(print());
    // }

    // @Test
    // public void testUpdateShouldReturn400BadRequestBecauseInvalidData() throws Exception {
    //     String requestUrl = END_POINT_PATH + "/NYC_USA";

    //     HourlyWeatherDTO dto1 = new HourlyWeatherDTO()
    //     .hourOfDay(10)
    //     .temperature(13)
    //     .precipitation(700)
    //     .status("Cloudy");

    //     HourlyWeatherDTO dto2 = new HourlyWeatherDTO()
    //     .hourOfDay(-1)
    //     .temperature(15)
    //     .precipitation(60)
    //     .status("");

    //     String requestBody = objectMapper.writeValueAsString(List.of(dto1, dto2));
    //     mockMvc.perform(put(requestUrl).contentType("application/json").content(requestBody))
    //     .andExpect(status().isBadRequest())
    //     .andExpect(jsonPath("$.errors[0]", containsString("Precipitation must be in the range of 0 to 100 percentage")))
    //     .andDo(print());
    // }

    // @Test
    // public void testUpdateShouldReturn404NotFound() throws Exception {
    //     String locationCode = "NYC_USA";
    //     String requestUrl = END_POINT_PATH + "/" + locationCode;

    //     HourlyWeatherDTO dto1 = new HourlyWeatherDTO()
    //     .hourOfDay(10)
    //     .temperature(13)
    //     .precipitation(70)
    //     .status("Cloudy");

    //     List<HourlyWeatherDTO> listDTO = List.of(dto1);
    //     String requestBody = objectMapper.writeValueAsString(listDTO);
    //     when(hourlyWeatherService.updateByLocationCode(Mockito.eq(locationCode), Mockito.anyList())).thenThrow(LocationNotFoundException.class);
    //     mockMvc.perform(put(requestUrl).contentType("application/json").content(requestBody))
    //     .andExpect(status().isNotFound())
    //     .andDo(print());
    // }

    // @Test
    // public void testUpdateShouldReturn200OK() throws Exception {
    //     String locationCode = "NYC_USA";
    //     String requestUrl = END_POINT_PATH + "/" + locationCode;

    //     HourlyWeatherDTO dto1 = new HourlyWeatherDTO()
    //     .hourOfDay(10)
    //     .temperature(13)
    //     .precipitation(70)
    //     .status("Cloudy");

    //     HourlyWeatherDTO dto2 = new HourlyWeatherDTO()
    //     .hourOfDay(11)
    //     .temperature(15)
    //     .precipitation(60)
    //     .status("Sunny");

    //     Location location = new Location();
    //     location.setCode(locationCode);
    //     location.setCityName("New York City");
    //     location.setCityName("New York");
    //     location.setCountryCode("US");
    //     location.setCountryName("United States of America");

    //     HourlyWeather forecast1 = new HourlyWeather()
    //     .location(location)
    //     .hourOfDay(10)
    //     .temperature(13)
    //     .precipitation(17)
    //     .status("Cloudy");

    //     HourlyWeather forecast2 = new HourlyWeather()
    //     .location(location)
    //     .hourOfDay(11)
    //     .temperature(15)
    //     .precipitation(60)
    //     .status("Sunny");
    //     List<HourlyWeatherDTO> listDTO = List.of(dto1, dto2);

    //     List<HourlyWeather> listHourlyWeather = List.of(forecast1, forecast2);

    //     when(hourlyWeatherService.updateByLocationCode(Mockito.eq(locationCode), Mockito.anyList())).thenReturn(listHourlyWeather);

    //     String requestBody = objectMapper.writeValueAsString(listDTO);
    //     mockMvc.perform(put(requestUrl).contentType("application/json").content(requestBody))
    //     .andExpect(status().isOk())
    //     .andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))
    //     .andDo(print());
    // }
}
