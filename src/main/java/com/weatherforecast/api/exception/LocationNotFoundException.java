package com.weatherforecast.api.exception;

public class LocationNotFoundException extends RuntimeException {
    
    public LocationNotFoundException(String locationCode) {
        super("Location not found with the given code: " + locationCode);
    }

}
