package com.weatherforecast.api.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.IIOException;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.exception.GeolocationException;

@Service
public class GeolocationService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(GeolocationService.class);

    private String DBPath = "ip2localdb/IP2LOCATION-LITE-DB3.BIN";
    IP2Location ip2Location = new IP2Location();

    public GeolocationService(IP2Location ip2Location) {
        try {
            ip2Location.Open(DBPath);
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    public Location getLocation(String ipAddress) throws GeolocationException {
        try {
            IPResult ipResult = ip2Location.IPQuery(ipAddress);
            if ("!OK".equals(ipResult.getStatus())) {
                throw new GeolocationException("Geolocation failed with status: " + ipResult.getStatus());
            }
            return new Location(ipResult.getCity(), ipResult.getRegion(), ipResult.getCountryLong(), ipResult.getCountryShort());
        } catch (IOException exception) {
            throw new GeolocationException("Error quering IP database", exception);
        }
    }

}
