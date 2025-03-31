package com.weatherforecast.api.exception;

public class LocationNotFoundException extends RuntimeException {
    
    public LocationNotFoundException(String locationCode) {
        super("Location not found with the given code: " + locationCode);
    }

    public LocationNotFoundException(String countryCode, String cityName) {
        super("Location not found with the country code: " + countryCode + " and city name: " + cityName);
    }

}
