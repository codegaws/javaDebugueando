package com.george.curso.springboot.recursividad;

public class Main {
    public static void main(String[] args) {

        final var car = Car.builder()
                .model("A50")
                .build();
        final var brand = Brand.builder()
                .name("Toyota")
                .car(car)
                .build();

        car.setBrand(brand);

        System.out.println(car.toString());
    }
}
