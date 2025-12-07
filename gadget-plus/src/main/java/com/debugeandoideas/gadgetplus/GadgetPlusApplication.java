package com.debugeandoideas.gadgetplus;

import com.debugeandoideas.gadgetplus.entities.BillEntity;
import com.debugeandoideas.gadgetplus.entities.OrderEntity;
import com.debugeandoideas.gadgetplus.entities.ProductEntity;
import com.debugeandoideas.gadgetplus.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootApplication
public class GadgetPlusApplication implements CommandLineRunner {

    //CREACION DE ATRIBUTOS REPOSITORYS Y VARIABLES CONSTANTES
    @Autowired// OJO USAR AUTOWIRED YA NO ES UNA BUENA PRACTICCA
    private OrderRepository orderRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCatalogRepository productCatalogRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RejectProductRepository rejectProductRepository;
    //*************************************************************

    public static void main(String[] args) {
        SpringApplication.run(GadgetPlusApplication.class, args);
    }

    //*************************************************************
    @Override
    public void run(String... args) throws Exception {
        //this.orderRepository.findAll().forEach(System.out::println);//LLAMAMOS AL METODO TOSTRING DEL ORDERENTITY
        //this.orderRepository.findAll().forEach(OrderEntity -> System.out.println(OrderEntity.toString()));
        //this.billRepository.findAll().forEach(bill -> System.out.println(bill.toString()));
        //CLASE 28  CASCADE.MERGE Y PERSIST***************************************************
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
        this.orderRepository.save(order);

         */
        // ************* CLASE 29 CASCADE.MERGE Y PERSIST *************
        /*
        var order = this.orderRepository.findById(17L).get();
        System.out.println("PRE PERSISTENCE: " + order.getClientName());
        order.setClientName("GEORGE MARTINEZ");

        order.getBill().setRfc("AAAA1111");
        this.orderRepository.save(order);

        var order2 = this.orderRepository.findById(17L).get();
        System.out.println("POST PERSISTENCE: " + order2.getClientName());
        */

        // ************* Ejercicio 30 CASCADE.DELETE AUNQUE MEJOR ES CASCADE.ALL *************
        //var order = this.orderRepository.findById(17L).get();
        //this.orderRepository.delete(order);
        //borramos el order y el bill asociado con cascade delete con id 17L

        // *************CLASE 33 PROBANDO RELACIONES OneToMany *************
       /* var order = this.orderRepository.findById(1L).orElseThrow();

        //CREO PRODUCTOS Y LO SETEAMOS

        var product1 = ProductEntity.builder().quantity(BigInteger.ONE).build();
        var product2 = ProductEntity.builder().quantity(BigInteger.TWO).build();
        var product3 = ProductEntity.builder().quantity(BigInteger.TEN).build();

        var products = List.of(product1, product2, product3);
        //order.setProducts(products);
        //products.forEach(product -> product.setOrder(order));
        order.addProduct(product1);
        order.addProduct(product2);
        order.addProduct(product3);

        //order.getProducts().removeFirst();// removemos el primer elemento de la lista de productos
        this.orderRepository.save(order);
        */
        // *************CLASE 35 ORPHAN REMOVAL CASCADE DELETE *************
        //var order = this.orderRepository.findById(2L).orElseThrow();

        //CREO PRODUCTOS Y LO SETEAMOS

       /* var product1 = ProductEntity.builder().quantity(BigInteger.ONE).build();
        var product2 = ProductEntity.builder().quantity(BigInteger.TWO).build();
        var product3 = ProductEntity.builder().quantity(BigInteger.TEN).build();

        //var products = List.of(product1, product2, product3);
        //order.setProducts(products);
        //products.forEach(product -> product.setOrder(order));// hace la relacion inversa a nuestra lista de productos
        order.addProduct(product1);
        order.addProduct(product2);
        order.addProduct(product3);*/

        //order.getProducts().removeFirst();// traigo la tabla productos y removemos el primer elemento de la lista de productos, lo dejamos huerfano , de debe evitar tener registros huerfanos en nuestra BD
        //this.orderRepository.save(order);


        // *************CLASE 38 PROBANDO RELACIONES OneToMany *************

         //SELECT * FROM PRODUCTS_CATALOG ME ITERA Y LO IMPRIME
        //this.productCatalogRepository.findAll().forEach(product -> System.out.println(product));

        // *************CLASE 39 PROBANDO RELACIONES PRODUCTOS - ORDENES - CATALOGOS *************
        //SELECT * FROM PRODUCTS_CATALOG
        var productCatalog1 = this.productCatalogRepository.findAll().get(0);
        var productCatalog2 = this.productCatalogRepository.findAll().get(4);
        var productCatalog3 = this.productCatalogRepository.findAll().get(7);

        var order = this.orderRepository.findById(1L).get();

        var product1 = ProductEntity.builder().quantity(BigInteger.ONE).build();
        var product2 = ProductEntity.builder().quantity(BigInteger.TWO).build();
        var product3 = ProductEntity.builder().quantity(BigInteger.TEN).build();

        product1.setCatalog(productCatalog1);
        product2.setCatalog(productCatalog2);
        product3.setCatalog(productCatalog3);

        order.addProduct(product1);
        order.addProduct(product2);
        order.addProduct(product3);

        this.orderRepository.save(order);

        //***************************CLASE 44 probando @ManyToMany**************

        /*final var HOME = this.categoryRepository.findById(1L).orElseThrow();//traemos el home
        final var OFFICE = this.categoryRepository.findById(2L).orElseThrow();//traemos el office

        //traemos todos los productos catalogos
        this.productCatalogRepository.findAll().forEach(product -> {
            //si contiene alguna palabra "home" le asignamos la categoria HOME
            if (product.getDescription().contains("home")) {
                //añadimos la categoria home
                product.addCategory(HOME);
            }
            if (product.getDescription().contains("office")) {
                //añadimos la categoria home
                product.addCategory(OFFICE);
            }
            this.productCatalogRepository.save(product);
        });*/

        //***************************CLASE 46-47 Insertando Registros Aleatorios**************

       /* var random = new Random();

        var productsCatalog = new LinkedList<>(this.productCatalogRepository.findAll());//aqui puedo inicializar en el constructor.

        // EN RANGE CREAMOS TANTOS PRODUCTOS COMO HAY EN EL CATALOGO
        IntStream.range(0, productsCatalog.size()).forEach(i -> {
            //ELEGIMOS ORDEN ALEATORIA
            var idOrderRandom = random.nextLong(16) + 1;
            var orderRandom = this.orderRepository.findById(idOrderRandom).orElseThrow();

            //CREAMOS PRODUCTOS CON DATOS ALEATORIOS
            var product = ProductEntity.builder()
                    .quantity(BigInteger.valueOf(random.nextLong(5) + 1))
                    .catalog(productsCatalog.poll())
                    .build();

            orderRandom.addProduct(product);
            product.setOrder(orderRandom);
            this.orderRepository.save(orderRandom);

        });*/

        //***************************CLASE 49 MAPEANDO LLAVE PRIMARIA COMPUESTA **************

        //this.rejectProductRepository.findAll().forEach(System.out::println);

    }
}
