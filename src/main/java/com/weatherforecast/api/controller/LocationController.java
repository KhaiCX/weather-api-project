package com.weatherforecast.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherforecast.api.exception.BadRequestException;
import com.weatherforecast.api.dto.LocationDTO;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.service.LocationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/v1/locations")
@Validated
public class LocationController {

    private LocationService locationService;
    private ModelMapper modelMapper;

    private Map<String, String> propertyMap = Map.of(
        "code", "code",
        "city_name", "cityName",
        "region_name", "regionName",
        "country_code", "countryCode",
        "country_name", "countryName",
        "enabled", "enabled"
    );

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

    @Deprecated
    public ResponseEntity<List<?>> listLocations() {
        List<Location> locations = locationService.list();
        return locations.isEmpty() 
        ? ResponseEntity.noContent().build() 
        : ResponseEntity.ok().body(listEntity2ListDTO(locations));
    }

    @GetMapping
    public ResponseEntity<?> listLocations(
        @RequestParam(value = "pageNum", required = false, defaultValue = "1") @Min(value = 1) Integer pageNum,
        @RequestParam(value = "size", required = false, defaultValue = "5") @Min(value = 5) @Max(value = 20) Integer pageSize,
        @RequestParam(value = "sort", required = false, defaultValue = "code") String sortField) throws BadRequestException {
            if (!propertyMap.containsKey(sortField)) {
                throw new BadRequestException("Invalid sort field: " + sortField);
            }
            Page<Location> page = locationService.listByPage(pageNum - 1, pageSize, sortField);

            List<Location> locations = page.getContent();

            if (locations.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(listEntity2ListDTO(locations));
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
