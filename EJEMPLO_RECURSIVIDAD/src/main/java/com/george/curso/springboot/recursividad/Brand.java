package com.george.curso.springboot.recursividad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Brand {
    private String name;
    private Car car;

    @Override
    public String toString() {
        return "Brand{" +
                "name='" + name + '\'' +
                ", car=" + car +
                '}';
    }
}
