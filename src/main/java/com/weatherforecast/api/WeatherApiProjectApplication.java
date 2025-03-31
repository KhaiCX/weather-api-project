package com.weatherforecast.api;

import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ip2location.IP2Location;
import com.weatherforecast.api.dto.DailyWeatherDTO;
import com.weatherforecast.api.dto.HourlyWeatherDTO;
import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.HourlyWeather;

@SpringBootApplication
public class WeatherApiProjectApplication {

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		var typeMap1 = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
		typeMap1.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);

		var typeMap2 = modelMapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class);
		typeMap2.addMapping(src -> src.getHourOfDay(),
		(dest, value) -> {dest.getId().setHourOfDay(!Objects.isNull(value) ? (Integer) value : 0);});

		var typeMap3 = modelMapper.typeMap(DailyWeather.class, DailyWeatherDTO.class);
		typeMap3.addMapping(src -> src.getId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth);
		typeMap3.addMapping(src -> src.getId().getMonth(), DailyWeatherDTO::setMonth);

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
