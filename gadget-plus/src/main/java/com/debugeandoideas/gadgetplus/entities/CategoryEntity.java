package com.debugeandoideas.gadgetplus.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryEntity {
    //INICIANDO EL MAPEO

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CodeCategoryEnum code;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    @ToString.Exclude
    @JsonIgnore    // ← Necesario porque ProductCatalogController devuelve entidades// ← Necesario porque ProductCatalogController devuelve entidades,se evita que sea una recursividad infinita al serializar a JSON
    private List<ProductCatalogEntity> productCatalog;

}
