package com.weatherforecast.api.common;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtility {
    private static Logger LOGGER = LoggerFactory.getLogger(CommonUtility.class);
    public static String getIPAddress(HttpServletRequest request) {

        String ip = request.getHeader("X-FOWARDED-FOR");
        if (Objects.isNull(ip) || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        LOGGER.info("Client's Ip Address: " + ip);

        return ip;
    }
}
