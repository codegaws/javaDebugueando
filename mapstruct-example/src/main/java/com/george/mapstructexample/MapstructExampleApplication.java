package com.george.mapstructexample;

import com.george.mapstructexample.dao.CountryDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MapstructExampleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MapstructExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        CountryDao.db.entrySet().forEach(System.out::println);
    }
}
