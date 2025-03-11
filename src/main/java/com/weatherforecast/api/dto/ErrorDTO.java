package com.weatherforecast.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ErrorDTO {
    private LocalDateTime timestamp;
    private Integer status;
    private List<String> errors;
    private String path;
}
