package com.weatherforecast.api;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ip2location.IP2Location;
import com.weatherforecast.api.dto.HourlyWeatherDTO;
import com.weatherforecast.api.entity.HourlyWeather;

@SpringBootApplication
public class WeatherApiProjectApplication {

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		var typeMap = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
		typeMap.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);
		return modelMapper;
	}

	@Bean
	public IP2Location geIp2Location() {
		return new IP2Location();
	}

	public static void main(String[] args) {
		SpringApplication.run(WeatherApiProjectApplication.class, args);
	}

}
