package com.weatherforecast.api.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HourlyWeatherId implements Serializable {
    private Integer hourOfDay;

    @ManyToOne
    @JoinColumn(name = "location_code")
    private Location location;

    @Override
    public int hashCode() {
        return Objects.hash(hourOfDay, location);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HourlyWeatherId other = (HourlyWeatherId) obj;
        return hourOfDay == other.hourOfDay && Objects.equals(location, other.location);
    }

}
