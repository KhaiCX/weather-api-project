package com.weatherforecast.api.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherforecast.api.config.TestLocationConfig;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.service.LocationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.add(location)).thenReturn(location);

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.code", is("NYC_USA")))
        .andExpect(header().string("Location", "/v1/locations/NYC_USA"))
        .andDo(print());
    }

}
