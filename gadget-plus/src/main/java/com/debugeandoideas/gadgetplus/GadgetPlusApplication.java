package com.debugeandoideas.gadgetplus;

import com.debugeandoideas.gadgetplus.entities.BillEntity;
import com.debugeandoideas.gadgetplus.entities.OrderEntity;
import com.debugeandoideas.gadgetplus.repositories.BillRepository;
import com.debugeandoideas.gadgetplus.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class GadgetPlusApplication implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BillRepository billRepository;

    public static void main(String[] args) {
        SpringApplication.run(GadgetPlusApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
      /* this.orderRepository.findAll().forEach(OrderEntity -> System.out.println(OrderEntity.toString()));
        this.billRepository.findAll().forEach(bill -> System.out.println(bill.toString()));*/

        // SETEAMOS
        /*var bill = BillEntity.builder()
                .rfc("AS537GD7X")
                .totalAmount(BigDecimal.TEN)
                .id("b-17")
                .build();


        var order = OrderEntity.builder()
                .createdAt(LocalDateTime.now())
                .clientName("Alex Martinez")
                .bill(bill)
                .build();
        this.orderRepository.save(order);*/


        var order = this.orderRepository.findById(17L).get();
        System.out.println("PRE PERSISTENCE: " + order.getClientName());
        order.setClientName("GEORGE MARTINEZ");

        order.getBill().setRfc("AAAA1111");
        this.orderRepository.save(order);

        var order2 = this.orderRepository.findById(17L).get();
        System.out.println("POST PERSISTENCE: " + order2.getClientName());

    }
}
