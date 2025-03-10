package com.weatherforecast.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.service.LocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;
    // public LocationController(LocationService locationService) {
    //     this.locationService = locationService;
    // }

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody @Valid Location location) {
        Location addedLocation = locationService.add(location);
        URI uri = URI.create("/v1/locations/" + addedLocation.getCode());
        return ResponseEntity.created(uri).body(addedLocation);
    }

}
