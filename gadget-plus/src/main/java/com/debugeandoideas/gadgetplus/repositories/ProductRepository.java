package com.debugeandoideas.gadgetplus.repositories;

import com.debugeandoideas.gadgetplus.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
