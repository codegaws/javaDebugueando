package com.debugeandoideas.gadgetplus.services;

import com.debugeandoideas.gadgetplus.dto.DateEval;
import com.debugeandoideas.gadgetplus.dto.ReportProduct;
import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCatalogService {

    ProductCatalogEntity findById(UUID id);

    ProductCatalogEntity findByName(String name);

    List<ProductCatalogEntity> findNameLike(String key);

    List<ProductCatalogEntity> findNameBetween(BigDecimal min, BigDecimal max);

    List<ProductCatalogEntity> findByCategory(Long id);

    List<ProductCatalogEntity> findByLauchingDate(LocalDate date, DateEval key);

    List<ProductCatalogEntity> findByBrandAndRating(String brand, Short rating);

    List<ProductCatalogEntity> findByBrandOrRating(String brand, Short rating);

    List<ReportProduct> makeReport();// clase 69

    Page<ProductCatalogEntity> findAll(String field, Boolean desc,Integer page );//clase 71 paginacion

    Page<ProductCatalogEntity> findAllByBrand(String brand);

    Integer countByBrand(String brand);

}
