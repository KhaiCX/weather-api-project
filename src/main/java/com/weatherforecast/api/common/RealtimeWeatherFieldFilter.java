package com.weatherforecast.api.common;
import com.weatherforecast.api.dto.RealtimeWeatherDTO;

public class RealtimeWeatherFieldFilter {
    public boolean equals(Object object) {
        if (object instanceof RealtimeWeatherDTO) {
            RealtimeWeatherDTO dto = (RealtimeWeatherDTO) object;
            return dto.getStatus() == null;
        }
        return false;
    }
}
