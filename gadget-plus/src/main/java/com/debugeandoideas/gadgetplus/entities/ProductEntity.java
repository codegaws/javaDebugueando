package com.debugeandoideas.gadgetplus.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
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
    //@ManyToOne(cascade = CascadeType.ALL)// NO ES BUENO ALL
    @ManyToOne
    @JoinColumn(name = "id_order")//LLAVE FORANEA HACIA ORDER
    private OrderEntity order;

    // CREAMOS OTRA INSTANCIA DE PRODUCTCATALOGENTITY PARA HACER LA RELACION UNO A UNO

    @OneToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "id_product_catalog")
    private ProductCatalogEntity catalog;
}







