package com.weatherforecast.api;

import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ip2location.IP2Location;
import com.weatherforecast.api.dto.DailyWeatherDTO;
import com.weatherforecast.api.dto.FullWeatherDTO;
import com.weatherforecast.api.dto.HourlyWeatherDTO;
import com.weatherforecast.api.dto.RealtimeWeatherDTO;
import com.weatherforecast.api.entity.DailyWeather;
import com.weatherforecast.api.entity.HourlyWeather;
import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.entity.RealtimeWeather;

@SpringBootApplication
public class WeatherApiProjectApplication {

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		configureMappingForHourlyWeather(modelMapper);
		configureMappingForDailyWeather(modelMapper);
		configureMappingForFullWeather(modelMapper);
		configureMappingForRealtimeWeather(modelMapper);
		return modelMapper;
	}

	@Bean
	public IP2Location geIp2Location() {
		return new IP2Location();
	}

	private void configureMappingForHourlyWeather(ModelMapper modelMapper) {
		modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class)
			.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);

		modelMapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class)
			.addMapping(src -> src.getHourOfDay(),
				(dest, value) -> {dest.getId().setHourOfDay(!Objects.isNull(value) ? (Integer) value : 0);});
	}

	private void configureMappingForDailyWeather(ModelMapper modelMapper) {
		modelMapper.typeMap(DailyWeather.class, DailyWeatherDTO.class)
			.addMapping(src -> src.getId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth)
			.addMapping(src -> src.getId().getMonth(), DailyWeatherDTO::setMonth);

		modelMapper.typeMap(DailyWeatherDTO.class, DailyWeather.class)
			.addMapping(src -> src.getDayOfMonth(), (dest, value) -> dest.getId().setDayOfMonth(!Objects.isNull(value) ? (Integer) value : 0))
			.addMapping(src -> src.getMonth(), (dest, value) -> dest.getId().setMonth(!Objects.isNull(value) ? (Integer) value : 0));
	}

	private void configureMappingForFullWeather(ModelMapper modelMapper) {
		modelMapper.typeMap(Location.class, FullWeatherDTO.class)
			.addMapping(src -> src.toString(), FullWeatherDTO::setLocation);
	}

	private void configureMappingForRealtimeWeather(ModelMapper modelMapper) {
		modelMapper.typeMap(RealtimeWeatherDTO.class, RealtimeWeather.class)
			.addMappings(m -> m.skip(RealtimeWeather::setLocation));
	}

	public static void main(String[] args) {
		SpringApplication.run(WeatherApiProjectApplication.class, args);
	}

}
