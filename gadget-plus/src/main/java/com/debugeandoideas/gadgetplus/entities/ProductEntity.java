package com.debugeandoideas.gadgetplus.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString(exclude = {"order"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // mapear la cantidad de productos que tiene esta orden
    private BigInteger quantity;
    // RELACION MUCHOS A UNO HACIA ORDER LADO PROPIETARIO LLEVA JOINCOLUMN Y LLAVE FORANEA.
    //@ManyToOne(cascade = CascadeType.ALL)// NO ES BUENO ALL por que borra todo - esto pasa en la cascada : borra primero-> order -> product -> order -> [products]
    @ManyToOne
    @JoinColumn(name = "id_order")//LLAVE FORANEA HACIA ORDER
    private OrderEntity order;

    // CREAMOS OTRA INSTANCIA DE PRODUCTCATALOGENTITY PARA HACER LA RELACION UNO A UNO
    // ESTA MAL DEBERIA SER MANYTOONE Y CASCADE PERSIST
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_product_catalog")
    private ProductCatalogEntity catalog;

}







