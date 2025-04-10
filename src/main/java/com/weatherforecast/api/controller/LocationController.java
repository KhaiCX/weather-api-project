package com.weatherforecast.api.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
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
        @RequestParam(value = "sort", required = false, defaultValue = "code") String sortField,
        @RequestParam(value = "enabled", required = false, defaultValue = "") String enabled,
        @RequestParam(value = "region_name", required = false, defaultValue = "") String regionName,
        @RequestParam(value = "country_code", required = false, defaultValue = "") String countryCode) throws BadRequestException {
            if (!propertyMap.containsKey(sortField)) {
                throw new BadRequestException("Invalid sort field: " + sortField);
            }
            Map<String, Object> filterFields = new HashMap<>();
            if (!"".equals(enabled)) {
                filterFields.put("enabled", Boolean.parseBoolean(enabled));
            }
            if (!"".equals(regionName)) {
                filterFields.put("regionName", regionName);
            }
            if (!"".equals(countryCode)) {
                filterFields.put("countryCode", countryCode);
            }
            Page<Location> page = locationService.listByPage(pageNum - 1, pageSize, sortField, filterFields);

            List<Location> locations = page.getContent();

            if (locations.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(addPageMetaDataAndLink2Collection(listEntity2ListDTO(locations), page, sortField, enabled, regionName, countryCode));
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

    private CollectionModel<LocationDTO> addPageMetaDataAndLink2Collection(List<LocationDTO> dtos, Page<Location> pageInfo, String sortField, String enabled, String regionName, String countryCode) throws BadRequestException {

        String actualEnabled = "".equals(enabled) ? null : enabled;
        String actualRegionName = "".equals(regionName) ? null : regionName;
        String actualCountryCode = "".equals(countryCode) ? null : countryCode;
        //add self link to each invididual location
        for (LocationDTO dto: dtos) {
            dto.add(linkTo(methodOn(LocationController.class).getLocation(dto.getCode())).withSelfRel());
        }

        int pageSize = pageInfo.getSize();
        int pageNum = pageInfo.getNumber() + 1;
        long totalElements = pageInfo.getTotalElements();
        int totalPages = pageInfo.getTotalPages();

        PageMetadata pageMetadata = new PageMetadata(pageSize, pageNum, totalElements);
        CollectionModel<LocationDTO> collectionModel = PagedModel.of(dtos, pageMetadata);

        //add self link to location
        collectionModel.add(linkTo(methodOn(LocationController.class).listLocations(pageNum, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode)).withSelfRel());

        if (pageNum > 1) {
            //add link to first page if the current page is not a first one
            collectionModel.add(linkTo(methodOn(LocationController.class).listLocations(1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode)).withRel(IanaLinkRelations.FIRST));

            //add link to the previous page if the current page is not a first one
            collectionModel.add(linkTo(methodOn(LocationController.class).listLocations(pageNum - 1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode)).withRel(IanaLinkRelations.PREV));
        }

        if (pageNum < totalPages) {
            //add link to next page if the current page is not a first one
            collectionModel.add(linkTo(methodOn(LocationController.class).listLocations(pageNum + 1, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode)).withRel(IanaLinkRelations.NEXT));

            //add link to last page if the current page is not a first one
            collectionModel.add(linkTo(methodOn(LocationController.class).listLocations(totalPages, pageSize, sortField, actualEnabled, actualRegionName, actualCountryCode)).withRel(IanaLinkRelations.LAST));

        }
        return collectionModel;
    }

}
