package com.debugeandoideas.gadgetplus.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor// crea constructor sin parametros
@Builder // Patron de diseño builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
// no es necesario por que debajo lo mapea como created_at a pesar que se llama createdAt
    private LocalDateTime createdAt;

    @Column(name = "client_name", length = 32, nullable = false)
    private String clientName;//no es necesario mapear el guion bajo

    // Relación uno a uno con BillEntity DELETE.TYPE.MERGE y PERSIST
    //con DETACH BORRAMOS TANTO EL HIJO COMO EL PADRE OSEA DEL ORDER Y DEL BILL
    // lo menos comun es ver esto -> cascade = {CascadeType.DETACH, CascadeType.REMOVE}
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_bill", nullable = false, unique = true)//Aqui es la union de la FK es como vas hacer el join
    private BillEntity bill;

    // Relación uno a muchos con ProductEntity ONETOMANY es la relacion inversa - aplicando ORPHAN REMOVAL
    @OneToMany(mappedBy = "order",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();

    public void addProduct(ProductEntity product) {
        products.add(product);
        product.setOrder(this);//seteamos la relacion bidireccional¿a quien pertenece?sino lo haces el producto no sabra a donde pertenece.
    }

   /*
    @OneToMany(mappedBy = "order",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<ProductEntity> products = new ArrayList<>();

    public void addProduct(ProductEntity p) {
        products.add(p);
        p.setOrder(this);//seteamos la relacion bidireccional¿a quien pertenece?sino lo haces el producto no sabra a donde pertenece.
    }
*/

    /**
     * ESTO ES LO QUE PASA CUANDO HACEMOS p.setOrder(this) EN EL METODO addProduct
     *
     * @Entity
     * @Table(name = "products")
     * public class ProductEntity {
     * @Id
     * @GeneratedValue(strategy = GenerationType. IDENTITY)
     * private Long id;
     * <p>
     * private BigInteger quantity;
     * @Column(name = "id_order")
     * private Long idOrder;               // ← Este campo se va a setear
     * @ManyToOne
     * @JoinColumn(name = "id_order")
     * private OrderEntity order;          // ← Este objeto se va a setear
     * <p>
     * // Método que se ejecuta cuando haces p.setOrder(this):
     * public void setOrder(OrderEntity order) {
     * this. order = order;             // ← Guarda la referencia completa
     * this.idOrder = order.getId();   // ← Extrae el ID y lo setea como FK
     * }
     * }
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}


