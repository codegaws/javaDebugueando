package com.debugeandoideas.gadgetplus.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity(name = "productCatalog")
@Table(name = "products_catalog", indexes = {
        @Index(name = "idx_product_name", columnList = "product_name"),
})
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
    private String brand;
    private String description;//por reflexion el campo se va a mapear al campo description que tiene 255 por eso no ponemos @Column(length=255)
    private BigDecimal price;
    @Column(name = "launching_date")
    private LocalDate launchingDate;
    @Column(name = "is_discount")
    private Boolean isDiscount;
    private Short rating;


    // VAMOS A TRAER LAS CATEGORIAS DE ESTE PRODUCTO

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH,
                    //CascadeType.MERGE, puede salir un error se estan creando 2 entidades iguales y hacen referencia a la misma tabla
                    CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JoinTable(
            name = "product_join_category",
            joinColumns = @JoinColumn(name = "id_product"),
            inverseJoinColumns = @JoinColumn(name = "id_category")
    )
    private List<CategoryEntity> categories = new LinkedList<>();

    public void addCategory(CategoryEntity category) {
        this.categories.add(category);
    }

    //NO OLVIDES QUE DEBES EVITAR RECURSIVIDAD INFINITA AL SERIALIZAR A JSON EN CategoryEntity ->@JsonIgnore
    //UN producto puede tener muchas categorias, una categoria puede tener muchos productos
    // osea cuando se llame y se cree a caterogias se va a crear un bucle infinito llamando a entidad producto catalogo
    // y de ahi a categoria y asi sucesivamente...
}
