package com.weatherforecast.api.common;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.weatherforecast.api.controller.FullWeatherController;
import com.weatherforecast.api.dto.FullWeatherDTO;
@Component
public class FullWeatherModelAssembler implements RepresentationModelAssembler<FullWeatherDTO, EntityModel<FullWeatherDTO>> {

    @Override
    public EntityModel<FullWeatherDTO> toModel(FullWeatherDTO entity) {
        EntityModel<FullWeatherDTO> entityModel = EntityModel.of(entity);
        entityModel.add(linkTo(
                methodOn(FullWeatherController.class).getFullWeatherByIPAddress(null))
                .withSelfRel());
        return entityModel;
    }

}
