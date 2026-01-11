package com.debugeandoideas.gadgetplus.services;

import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import com.debugeandoideas.gadgetplus.repositories.ProductCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogBatchImpl implements CatalogBatch {

    private final ProductCatalogRepository productCatalogRepository;

    @Override
    public void insertBatch() {
        //medimos tiempo de ejecucion
        log.info("Inserting in batch...");
        final var start = System.currentTimeMillis();
        this.productCatalogRepository.saveAllAndFlush(products);
        final var end = System.currentTimeMillis();
        log.info("Insert in {} ms", end - start);
    }

    @Override
    public void delteBatch() {
        log.info("Deleting in batch...");
        var ids = this.productCatalogRepository.findAll()
                .stream()
                .map(ProductCatalogEntity::getId)
                .toList();
        final var start = System.currentTimeMillis();
        this.productCatalogRepository.deleteAllById(ids);
        final var end = System.currentTimeMillis();
        log.info("End delete In in {} second", end - start);

    }

    //lista de productos para carga masivas
    private static List<ProductCatalogEntity> products;

    static {
        products = List.of(
                ProductCatalogEntity
                        .builder()
                        .name("portable monitor S")
                        .brand("LG")
                        .price(BigDecimal.valueOf(123.44))
                        .launchingDate(LocalDate.now())
                        .isDiscount(false)
                        .description("New product")
                        .rating(Short.valueOf("0"))
                        .build(),
                ProductCatalogEntity
                        .builder()
                        .name("portable monitor M")
                        .brand("LG")
                        .price(BigDecimal.valueOf(133.44))
                        .launchingDate(LocalDate.now())
                        .isDiscount(false)
                        .description("New product")
                        .rating(Short.valueOf("0"))
                        .build(),
                ProductCatalogEntity
                        .builder()
                        .name("portable monitor L")
                        .brand("LG")
                        .price(BigDecimal.valueOf(143.44))
                        .launchingDate(LocalDate.now())
                        .isDiscount(false)
                        .description("New product")
                        .rating(Short.valueOf("0"))
                        .build(),
                ProductCatalogEntity
                        .builder()
                        .name("portable monitor XL")
                        .brand("LG")
                        .price(BigDecimal.valueOf(1253.44))
                        .launchingDate(LocalDate.now())
                        .isDiscount(false)
                        .description("New product")
                        .rating(Short.valueOf("0"))
                        .build(),
                ProductCatalogEntity
                        .builder()
                        .name("portable monitor Dual")
                        .brand("LG")
                        .price(BigDecimal.valueOf(2253.44))
                        .launchingDate(LocalDate.now())
                        .isDiscount(false)
                        .description("New product")
                        .rating(Short.valueOf("0"))
                        .build()
        );
    }
}
