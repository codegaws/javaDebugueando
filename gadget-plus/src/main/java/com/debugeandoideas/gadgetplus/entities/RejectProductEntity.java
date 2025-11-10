package com.debugeandoideas.gadgetplus.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reject_products")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(RejectProductId.class)
public class RejectProductEntity {

    @Id
    @Column(name = "product_name")
    private String productName;

    @Id
    @Column(name = "brand_name")  // Cambiar aquí: era "product_brand"
    private String brandName;

    @Column(name = "quantity")
    private Integer quantity;  // También cambiar a Integer en lugar de BigInteger
}
