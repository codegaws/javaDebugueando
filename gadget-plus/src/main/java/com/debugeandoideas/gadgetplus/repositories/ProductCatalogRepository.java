package com.debugeandoideas.gadgetplus.repositories;

import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import com.debugeandoideas.gadgetplus.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductCatalogRepository extends JpaRepository<ProductCatalogEntity, UUID> {
}
