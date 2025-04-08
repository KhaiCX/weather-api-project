package com.weatherforecast.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherforecast.api.config.TestConfig;
import com.weatherforecast.api.dto.LocationDTO;
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
import static org.mockito.Mockito.when;

@WebMvcTest(LocationController.class)
@Import(TestConfig.class)
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
    private LocationDTO dto;

    @BeforeEach
    public void createData() {
        location1 = new Location()
        .code("NYC_USA")
        .cityName("New York City")
        .regionName("New York")
        .countryCode("US")
        .countryName("United States of America")
        .enabled(true);

        location2 = new Location()
        .code("DIHLI_IN")
        .cityName("New York City")
        .regionName("New York")
        .countryCode("US")
        .countryName("United States of America")
        .enabled(true)
        .trashed(true);

        dto = new LocationDTO()
        .code(location1.getCode())
        .cityName(location1.getCityName())
        .regionName(location1.getRegionName())
        .countryCode(location1.getCountryCode())
        .countryName(location1.getCountryName())
        .enabled(location1.getEnabled());
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

        String bodyContent = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.code", is("NYC_USA")))
        .andExpect(header().string("Location", "/v1/locations/NYC_USA"))
        .andDo(print());
    }

    @Test
    @Disabled
    public void testListShouldReturn204NoContent() throws Exception {
        Mockito.when(locationService.list()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isNoContent())
        .andDo(print());
    }

    @Test
    public void testListByPageReturn204NoContent() throws Exception {
        Mockito.when(locationService.listByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(Page.empty());
        mockMvc.perform(get(END_POINT_PATH))
        .andExpect(status().isNoContent())
        .andDo(print());
    }

    @Test
    public void testListByPageReturn400BadRequestInvalidPageNum() throws Exception {
        int pageNum = 0;
        int pageSize = 5;
        String sortField = "code";
        Mockito.when(locationService.listByPage(pageNum, pageSize, sortField)).thenReturn(Page.empty());

        String requestURI = END_POINT_PATH + "?pageNum=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;
        mockMvc.perform(get(requestURI))
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

    @Test
    public void testListByPageReturn400BadRequestInvalidPageSize() throws Exception {
        int pageNum = 1;
        int pageSize = 3;
        String sortField = "code";
        Mockito.when(locationService.listByPage(pageNum, pageSize, sortField)).thenReturn(Page.empty());

        String requestURI = END_POINT_PATH + "?pageNum=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;
        mockMvc.perform(get(requestURI))
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

    @Test
    public void testListByPageReturn400BadRequestInvalidSortField() throws Exception {
        int pageNum = 1;
        int pageSize = 5;
        String sortField = "code_test";
        Mockito.when(locationService.listByPage(pageNum, pageSize, sortField)).thenReturn(Page.empty());

        String requestURI = END_POINT_PATH + "?pageNum=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;
        mockMvc.perform(get(requestURI))
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

    @Test
    @Disabled
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
    public void testListByPageReturn200OK() throws Exception {

        List<Location> locations = List.of(location1, location2);
        int pageNum = 1;
        int pageSize = 5;
        String sortField = "code";

        int totalElements = locations.size();
        Sort sort = Sort.by(sortField);
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Location> page = new PageImpl<>(locations, pageable, totalElements);

        Mockito.when(locationService.listByPage(pageNum - 1, pageSize, sortField)).thenReturn(page);

        String requestURI = END_POINT_PATH + "?pageNum=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;

        mockMvc.perform(get(requestURI))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/hal+json"))
        .andExpect(jsonPath("$._embedded.locations[0].code", is("NYC_USA")))
        .andExpect(jsonPath("$._embedded.locations[0].city_name", is("New York City")))
        .andExpect(jsonPath("$._embedded.locations[1].code", is("DIHLI_IN")))
        .andExpect(jsonPath("$._embedded.locations[1].city_name", is("New York City")))
        .andExpect(jsonPath("$.page.size", is(pageSize)))
        .andExpect(jsonPath("$.page.number", is(pageNum + 1)))
        .andExpect(jsonPath("$.page.totalElements", is(totalElements)))
        .andExpect(jsonPath("$.page.totalPages", is(1)))
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
        String code = "codeTest";
        String requestUrl = END_POINT_PATH + "/" + code;
        LocationNotFoundException exception = new LocationNotFoundException(code);
        when(locationService.get(code)).thenThrow(exception);
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
    public void testListByPageShouldReturn200OK() throws Exception {
        String code = "NYC_USA";
        Mockito.when(locationService.get(code)).thenReturn(location1);
        String requestUrl = END_POINT_PATH + "/" + code;
        mockMvc.perform(get(requestUrl))
        .andExpect(status().isOk())
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        LocationNotFoundException exception = new LocationNotFoundException(dto.getCode());

        Mockito.when(locationService.update(Mockito.any())).thenThrow(exception);

        String bodyContent = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(END_POINT_PATH)
        .contentType("application/json").content(bodyContent))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors[0]", is(exception.getMessage())))
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception {

        LocationDTO location = new LocationDTO();
        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH)
        .contentType("application/json").content(bodyContent))
        .andExpect(status().isBadRequest())
        .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {

        Mockito.when(locationService.update(location1)).thenReturn(location1);
        String bodyContent = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(END_POINT_PATH)
        .contentType("application/json").content(bodyContent))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.code", is("NYC_USA")))
        .andExpect(jsonPath("$.city_name", is("New York City")))
        .andExpect(jsonPath("$.region_name", is("New York")))
        .andExpect(jsonPath("$.country_code", is("US")))
        .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {

        String code = "codeTest";
        String requestUrl = END_POINT_PATH + "/" + code;

        LocationNotFoundException exception = new LocationNotFoundException(code);
        Mockito.doThrow(exception).when(locationService).delete(code);

        mockMvc.perform(delete(requestUrl))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors[0]", is(exception.getMessage())))
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
        dto.setCode(null);
        String bodyContent = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.errors[0]", is("Location code cannot be null")))
        .andDo(print());

    }

    @Test
    public void testValidateRequestBodyLocationCodeLength() throws Exception {

        dto.setCode("NYC_USAAAAAAAAAAAAAAA");
        String bodyContent = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.errors[0]", is("Location code must have 3-12 characters")))
        .andDo(print());

    }

}
