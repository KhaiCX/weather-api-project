package com.weatherforecast.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.LocationNotFoundException;
import com.weatherforecast.api.service.LocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody @Valid Location location) {
        Location addedLocation = locationService.add(location);
        URI uri = URI.create("/v1/locations/" + addedLocation.getCode());
        return ResponseEntity.created(uri).body(addedLocation);
    }

    @GetMapping
    public ResponseEntity<List<Location>> listLocations() {
        List<Location> locations = locationService.list();
        return locations.isEmpty() 
        ? ResponseEntity.noContent().build() 
        : ResponseEntity.ok().body(locations);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getLocation(@PathVariable String code) {
        Location location = locationService.get(code);
        return Objects.isNull(location)
        ? ResponseEntity.notFound().build()
        : ResponseEntity.ok().body(location);
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid Location location) {
        try {
            Location updateLocation = locationService.update(location);
            return ResponseEntity.ok().body(updateLocation);
        } catch (LocationNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
