package com.weatherforecast.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;

public class IP2LocationTests {

    private String DBPath = "ip2localdb/IP2LOCATION-LITE-DB3.BIN";

    @Test
    public void testInvalidIP() throws IOException {
        IP2Location ip2Location = new IP2Location();
        ip2Location.Open(DBPath);

        String ipAddress = "abc";
        IPResult ipResult = ip2Location.IPQuery(ipAddress);
        assertEquals(ipResult.getStatus(), "INVALID_IP_ADDRESS");
    }

    @Test
    public void testValidIP() throws IOException {
        IP2Location ip2Location = new IP2Location();
        ip2Location.Open(DBPath);

        String ipAddress = "108.30.178.78";
        IPResult ipResult = ip2Location.IPQuery(ipAddress);
        assertEquals(ipResult.getStatus(), "OK");
        assertEquals(ipResult.getCity(), "New York City");
        System.out.println(ipResult);
    }

}
