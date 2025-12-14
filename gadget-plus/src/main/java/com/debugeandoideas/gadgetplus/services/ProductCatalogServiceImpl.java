package com.debugeandoideas.gadgetplus.services;

import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import com.debugeandoideas.gadgetplus.repositories.ProductCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor//-> se realaciona con todas las final se hace inyeccion de dependencias
@Transactional(readOnly = true)// accedemos a la BD solo de lectura
public class ProductCatalogServiceImpl implements ProductCatalogService {

    private final ProductCatalogRepository catalogRepository;

    @Override
    public ProductCatalogEntity findById(UUID id) {
        return this.catalogRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Producto no encontrado con ID: " + id));
    }

    @Override
    public ProductCatalogEntity findByName(String name) {
        return this.catalogRepository.findByName(name).orElseThrow();
        //return this.catalogRepository.findByName(name).orElse(ProductCatalogEntity.builder().build());//lanzara un objeto vacio
    }

    @Override
    public List<ProductCatalogEntity> findNameLike(String key) {
        return this.catalogRepository.findByNameLike(key);
    }

    @Override
    public List<ProductCatalogEntity> findNameBetween(BigDecimal min, BigDecimal max) {
        return this.catalogRepository.findByBetweenTwoPrices(min, max);
    }

    @Override
    public List<ProductCatalogEntity> findByCategoryName(BigInteger id) {
        return List.of();
    }

    @Override
    public List<ProductCatalogEntity> findByBrandAndRating(String brand, Short rating) {
        return List.of();
    }

    @Override
    public Page<ProductCatalogEntity> findAll(String field, Boolean desc) {
        return null;
    }

    @Override
    public Page<ProductCatalogEntity> findAllByBrand(String brand) {
        return null;
    }

    @Override
    public Integer countByBrand(String brand) {
        return 0;
    }
}
