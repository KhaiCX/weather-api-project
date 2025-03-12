package com.weatherforecast.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherforecast.api.config.TestLocationConfig;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.service.LocationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;

@WebMvcTest(LocationController.class)
@Import(TestLocationConfig.class)
public class LocationControllerTests {

    private static final String END_POINT_PATH = "/v1/locations";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LocationService locationService;

    private Location location1;
    private Location location2;

    @BeforeEach
    public void createData() {
        location1 = new Location();
        location1.setCode("NYC_USA");
        location1.setCityName("New York City");
        location1.setRegionName("New York");
        location1.setCountryCode("US");
        location1.setCountryName("United States of America");
        location1.setEnabled(true);

        location2 = new Location();
        location2.setCode("LACA_USA");
        location2.setCityName("Los Angerles City");
        location2.setRegionName("Los Angerles");
        location2.setCountryCode("US");
        location2.setCountryName("United States of Los Angerles");
        location2.setEnabled(true);
        location2.setTrashed(true);
    }
    
    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {
        Location location = new Location();
        String bodyContent = objectMapper.writeValueAsString(location);
        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception {

        Mockito.when(locationService.add(location1)).thenReturn(location1);

        String bodyContent = objectMapper.writeValueAsString(location1);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.code", is("NYC_USA")))
        .andExpect(header().string("Location", "/v1/locations/NYC_USA"))
        .andDo(print());
    }

    @Test
    public void testListShouldReturn204NoContent() throws Exception {
        Mockito.when(locationService.list()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isNoContent())
        .andDo(print());
    }

    @Test
    public void testListShouldReturn200OK() throws Exception {

        Mockito.when(locationService.list()).thenReturn(List.of(location1, location2));

        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$[0].code", is("NYC_USA")))
        .andExpect(jsonPath("$[1].code", is("LACA_USA")))
        .andDo(print());
    }

    @Test
    public void testGetShouldReturn405NotAllowed() throws Exception {
        String requestUrl = END_POINT_PATH + "/codeTest";
        mockMvc.perform(post(requestUrl))
        .andExpect(status().isMethodNotAllowed())
        .andDo(print());
    }

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {
        String requestUrl = END_POINT_PATH + "/codeTest";
        mockMvc.perform(get(requestUrl))
        .andExpect(status().isNotFound())
        .andDo(print());
    }

    @Test
    public void testGetShouldReturn200OK() throws Exception {
        String code = "NYC_USA";
        Mockito.when(locationService.get(code)).thenReturn(location1);
        String requestUrl = END_POINT_PATH + "/" + code;
        mockMvc.perform(get(requestUrl))
        .andExpect(status().isOk())
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        Location location = new Location();
        location.setCode("codeTest");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.update(location)).thenThrow(new LocationNotFoundException("No location found"));

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH)
        .contentType("application/json").content(bodyContent))
        .andExpect(status().isNotFound())
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception {
        Location location = new Location();
        //location.setCode("codeTest");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.update(location)).thenThrow(new LocationNotFoundException("No location found"));

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH)
        .contentType("application/json").content(bodyContent))
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City Test");
        location.setRegionName("New York Test");
        location.setCountryCode("US");
        location.setCountryName("United States of America Test");
        location.setEnabled(true);

        Mockito.when(locationService.update(location)).thenReturn(location);

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH)
        .contentType("application/json").content(bodyContent))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.code", is("NYC_USA")))
        .andExpect(jsonPath("$.city_name", is("New York City Test")))
        .andExpect(jsonPath("$.region_name", is("New York Test")))
        .andExpect(jsonPath("$.country_code", is("US")))
        .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {

        String code = "codeTest";
        String requestUrl = END_POINT_PATH + "/" + code;

        Mockito.doThrow(LocationNotFoundException.class).when(locationService).delete(code);

        mockMvc.perform(delete(requestUrl))
        .andExpect(status().isNotFound())
        .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn204NoContent() throws Exception {
        String code = "codeTest";
        String requestUrl = END_POINT_PATH + "/" + code;

        Mockito.doNothing().when(locationService).delete(code);

        mockMvc.perform(delete(requestUrl))
        .andExpect(status().isNoContent())
        .andDo(print());
    }

    @Test
    public void testValidateRequestBodyLocationCode() throws Exception {

        Location location = new Location();
        //location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);
        
        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.errors[0]", is("Location code cannot be null")))
        .andDo(print());

    }

    @Test
    public void testValidateRequestBodyLocationCodeLength() throws Exception {

        Location location = new Location();
        location.setCode("");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);
        
        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.errors[0]", is("Location code must have a length between 3 and 12")))
        .andDo(print());

    }

}
