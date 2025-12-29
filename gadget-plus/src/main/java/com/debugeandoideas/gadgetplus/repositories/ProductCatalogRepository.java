package com.debugeandoideas.gadgetplus.repositories;

import com.debugeandoideas.gadgetplus.dto.ReportProduct;
import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

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

    //CLASE 64-65 APLICANDO LENGUAJE DE SPRING JPA
    //practiquita usando nombre personalizado a mi metodo fechita usando JPQL

    @Query("SELECT p FROM productCatalog p WHERE p.launchingDate < :fecha")
    List<ProductCatalogEntity> buscarAnterioresA(@Param("fecha") LocalDate fecha);

    @Query("SELECT p FROM productCatalog p WHERE p.launchingDate > :fecha")
    List<ProductCatalogEntity> buscarDespuesDe(@Param("fecha") LocalDate fecha);

    //**************BUSCANDO FECHAS CON QUERYMETHOS*****************

    List<ProductCatalogEntity> findByLaunchingDateAfter(LocalDate date);

    List<ProductCatalogEntity> findByLaunchingDateBefore(LocalDate date);


    //CLASE 66 BUSCAMOS POR MARCA Y RATING MAYOR A...
    List<ProductCatalogEntity> findByBrandAndRatingGreaterThan(String brand, Short rating);

    List<ProductCatalogEntity> findByBrandOrRatingGreaterThan(String brand, Short rating);

    //clase 68 GROUP BY
    @Query("select new com.debugeandoideas.gadgetplus.dto.ReportProduct("
            + "pc.brand, "
            + "avg(pc.price), " // avg se mapea como double no bigdecimal
            + "sum(pc.price)) "
            + "from productCatalog pc "
            + "group by pc.brand")
    List<ReportProduct> findAndMakeReport();

    //clase 73 paginacion personalizada
    Page<ProductCatalogEntity> findAllByBrand(String brand, Pageable pageable);//siempre debe ir el pageable con el Page

    //🎯Metodo especial que llama a un procedimiento almacenado (stored procedure) en la base de datos
    // clase 75 stored procedure -> llamando storedprocedure✅
    //devuelve la cantidad de elementos que hay con esa misma marca o brand✅
    @Procedure(procedureName = "count_total_products_by_brand", outputParameterName = "response")//asi se llama el output parameter
    Integer countTotalProductsByBrandStoredProcedure(@Param(value="brand") String brand);//recibe un string que es la marca

}
