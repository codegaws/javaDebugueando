package com.george.mapstructexample.service;

import com.george.mapstructexample.dao.CountryDao;
import com.george.mapstructexample.dto.CountryDto;
import com.george.mapstructexample.mapper.CountryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CountryService {

    public CountryDto readById(UUID id) {

        if (CountryDao.db.containsKey(id)) {
            return CountryMapper.countryMapper.toCountryDto(CountryDao.db.get(id));
        } else {
            log.error("Country with id {}", id);
            throw new RuntimeException("Country with id" + id + " not found");
        }
    }
}
