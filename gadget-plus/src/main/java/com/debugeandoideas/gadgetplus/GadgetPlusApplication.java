package com.debugeandoideas.gadgetplus;

import com.debugeandoideas.gadgetplus.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GadgetPlusApplication implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;

    public static void main(String[] args) {
        SpringApplication.run(GadgetPlusApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //this.orderRepository.findAll().forEach(o -> System.out.println(o.getClientName()));
        this.orderRepository.findAll().forEach(o -> System.out.println(o.toString()));// aqui te trae todo el objeto order con bill incluido
    }
}
