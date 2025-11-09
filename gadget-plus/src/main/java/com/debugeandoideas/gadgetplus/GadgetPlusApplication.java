package com.debugeandoideas.gadgetplus;

import com.debugeandoideas.gadgetplus.entities.ProductEntity;
import com.debugeandoideas.gadgetplus.repositories.BillRepository;
import com.debugeandoideas.gadgetplus.repositories.OrderRepository;
import com.debugeandoideas.gadgetplus.repositories.ProductRepository;
import com.debugeandoideas.gadgetplus.repositories.ProductCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;
import java.util.List;

@SpringBootApplication
public class GadgetPlusApplication implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCatalogRepository productCatalogRepository;

    public static void main(String[] args) {
        SpringApplication.run(GadgetPlusApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
      /* this.orderRepository.findAll().forEach(OrderEntity -> System.out.println(OrderEntity.toString()));
        this.billRepository.findAll().forEach(bill -> System.out.println(bill.toString()));*/

        //EJERCICIO CASCADE.MERGE Y PERSIST***************************************************
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
        /*
        var order = this.orderRepository.findById(17L).get();
        System.out.println("PRE PERSISTENCE: " + order.getClientName());
        order.setClientName("GEORGE MARTINEZ");

        order.getBill().setRfc("AAAA1111");
        this.orderRepository.save(order);

        var order2 = this.orderRepository.findById(17L).get();
        System.out.println("POST PERSISTENCE: " + order2.getClientName());
        */

        // ************* Ejercicio CASCADE.DELETE AUNQUE MEJOR ES CASCADE.ALL *************
       /* var order = this.orderRepository.findById(17L).get();
        this.orderRepository.delete(order);*/
        //borramos el order y el bill asociado con cascade delete con id 17L

        // *************CLASE 37 PROBANDO RELACIONES OneToMany *************
        //var order = this.orderRepository.findById(2L).orElseThrow();

        //CREO PRODUCTOS
        /*
        var product1 = ProductEntity.builder().quantity(BigInteger.ONE).build();
        var product2 = ProductEntity.builder().quantity(BigInteger.TWO).build();
        var product3 = ProductEntity.builder().quantity(BigInteger.TEN).build();

        var products = List.of(product1, product2, product3);
        //order.setProducts(products);
        //products.forEach(product -> product.setOrder(order));
        order.addProduct(product1);
        order.addProduct(product2);
        order.addProduct(product3);
        */
        //order.getProducts().removeFirst();// removemos el primer elemento de la lista de productos
        //this.orderRepository.save(order);

        // *************CLASE 38 PROBANDO RELACIONES OneToMany *************

        // SELECT * FROM PRODUCTS_CATALOG ME ITERA Y LO IMPRIME
        //this.productCatalogRepository.findAll().forEach(product -> System.out.println(product));

        // SELECT * FROM PRODUCTS_CATALOG
        // *************CLASE 39 PROBANDO RELACIONES PRODUCTOS - ORDENES - CATALOGOS *************
        var productCatalog1 = this.productCatalogRepository.findAll().get(0);
        var productCatalog2 = this.productCatalogRepository.findAll().get(4);
        var productCatalog3 = this.productCatalogRepository.findAll().get(7);

        var order = this.orderRepository.findById(1L).get();

        var product1 = ProductEntity.builder().quantity(BigInteger.ONE).build();
        var product2 = ProductEntity.builder().quantity(BigInteger.TWO).build();
        var product3 = ProductEntity.builder().quantity(BigInteger.TEN).build();

        var products = List.of(product1, product2, product3);
        product1.setCatalog(productCatalog1);
        product2.setCatalog(productCatalog2);
        product3.setCatalog(productCatalog3);

        order.addProduct(product1);
        order.addProduct(product2);
        order.addProduct(product3);
        this.orderRepository.save(order);
    }
}
