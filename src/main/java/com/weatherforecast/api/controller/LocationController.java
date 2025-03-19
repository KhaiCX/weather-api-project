package com.weatherforecast.api.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.dto.LocationDTO;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.service.LocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/locations")
public class LocationController {

    private LocationService locationService;
    private ModelMapper modelMapper;

    public LocationController(LocationService locationService, ModelMapper modelMapper) {
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<?> addLocation(@RequestBody @Valid LocationDTO dto) {
        Location addedLocation = locationService.add(dto2Entity(dto));
        URI uri = URI.create("/v1/locations/" + addedLocation.getCode());
        return ResponseEntity.created(uri).body(entity2DTO(addedLocation));
    }

    @GetMapping
    public ResponseEntity<List<?>> listLocations() {
        List<Location> locations = locationService.list();
        return locations.isEmpty() 
        ? ResponseEntity.noContent().build() 
        : ResponseEntity.ok().body(listEntity2ListDTO(locations));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getLocation(@PathVariable String code) {
        Location location = locationService.get(code);

        return ResponseEntity.ok().body(entity2DTO(location));
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDTO dto) {
        Location updateLocation = locationService.update(dto2Entity(dto));

        return ResponseEntity.ok().body(entity2DTO(updateLocation));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable String code) {
        locationService.delete(code);

        return ResponseEntity.noContent().build();
    }

    private List<LocationDTO> listEntity2ListDTO(List<Location> listEntity) {
        return listEntity.stream().map(entity -> entity2DTO(entity))
        .collect(Collectors.toList());
    }

    private LocationDTO entity2DTO(Location entity) {
        return modelMapper.map(entity, LocationDTO.class);
    }

    private Location dto2Entity(LocationDTO dto) {
        return modelMapper.map(dto, Location.class);
    }

}
