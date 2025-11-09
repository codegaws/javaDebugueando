package com.debugeandoideas.gadgetplus.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "products_catalog")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;// el UUID se mapea a un campo de tipo CHAR(36) en la base de datos es de JPA 3.1 en adelante
    @Column(name = "product_name", length = 64)
    private String name;
    @Column(name = "brand_name", length = 64)
    private String brad;
    private String description;//por reflexion el campo se va a mapear al campo description que tiene 255 por eso no ponemos @Column(length=255)
    private BigDecimal price;
    private LocalDate launching_date;
    @Column(name = "is_discount")
    private Boolean isDiscount;
    private Short rating;

}
