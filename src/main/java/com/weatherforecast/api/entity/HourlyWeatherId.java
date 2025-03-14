package com.weatherforecast.api.entity;

import java.io.Serializable;

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

}
