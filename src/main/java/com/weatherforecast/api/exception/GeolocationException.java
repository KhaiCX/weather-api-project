package com.weatherforecast.api.exception;

public class GeolocationException extends Exception {

    public GeolocationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public GeolocationException(String message) {
        super(message);
    }
}
