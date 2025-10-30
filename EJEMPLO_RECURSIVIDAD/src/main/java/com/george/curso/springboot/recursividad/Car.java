package com.george.curso.springboot.recursividad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Car {
    private String model;
    @Setter
    private Brand brand;

    @Override
    public String toString() {
        return "Car{" +
                "model='" + model ;
    }
}
