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
public class DailyWeatherId implements Serializable {
    private Integer dayOfMonth;
    private Integer month;

    @ManyToOne
    @JoinColumn(name = "location_code")
    private Location location;

    @Override
    public int hashCode() {
        return Objects.hash(dayOfMonth, location, month);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DailyWeatherId other = (DailyWeatherId) obj;
        return dayOfMonth == other.dayOfMonth && Objects.equals(location, other.location) && month == other.month;
    }

    @Override
    public String toString() {
        return "DailyWeatherId [dayOfMonth=" + dayOfMonth + ", month=" + month + ", location=" + location + "]";
    }
}
