package com.debugeandoideas.gadgetplus.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column
    private BigDecimal totalAmount;

    @Column(name = "client_rfc", length = 14, nullable = false)
    private String rfc;

    //RELACION
    @ToString.Exclude
    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private OrderEntity order;
}



