package com.debugeandoideas.gadgetplus.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "client_name", length = 32, nullable = false)
    private String clientName;//no es necesario mapear el guion bajo

    @OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "id_bill", nullable = false, unique = true)
    private BillEntity bill;
}
