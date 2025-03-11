package com.weatherforecast.api.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ErrorDTO {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String path;
}
