package com.debugeandoideas.gadgetplus.repositories;

import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCatalogRepository extends JpaRepository<ProductCatalogEntity, UUID> {

    Optional<ProductCatalogEntity> findByName(String name);

    List<ProductCatalogEntity> findByNameLike(String key);

    // el select * se omite en JPQL y viene por defecto
    @Query("from productCatalog p where p.price between :min and :max")
    List<ProductCatalogEntity> findByBetweenTwoPrices(BigDecimal min, BigDecimal max);

    @Query("from productCatalog p left join fetch p.categories c where c.id= :categoryId")
    List<ProductCatalogEntity> getByCategory(Long categoryId);

    //CLASE 65 APLICANDO LENGUAJE DE SPRING JPA
    List<ProductCatalogEntity> findByLaunchingDateAfter(LocalDate date);

    List<ProductCatalogEntity> findByLaunchingDateBefore(LocalDate date);

    //CLASE 66 BUSCAMOS POR MARCA Y RATING MAYOR A...
    List<ProductCatalogEntity> findByBrandAndRatingGreaterThan(String brand, Short rating);
}
