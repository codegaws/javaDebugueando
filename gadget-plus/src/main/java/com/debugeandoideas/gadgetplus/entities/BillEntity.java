package com.debugeandoideas.gadgetplus.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "bill")
@Getter
@Setter
@ToString(exclude = {"order"})
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
    private String clientRfc;

    //RELACION ONETOONE
    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private OrderEntity order;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BillEntity that = (BillEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}



