package com.george.mapstructexample.mapper;

import com.george.mapstructexample.dto.CountryDto;
import com.george.mapstructexample.models.Country;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CountryMapper {

    CountryMapper countryMapper = Mappers.getMapper(CountryMapper.class);//?
    CountryDto toCountryDto(Country country);

}
