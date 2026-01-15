package com.debugeandoideas.gadgetplus.controllers;


import com.debugeandoideas.gadgetplus.dto.ProductSearchCriteria;
import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import com.debugeandoideas.gadgetplus.repositories.ProductCatalogRepository;

import com.debugeandoideas.gadgetplus.specs.ProductSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("filter/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductCatalogRepository repository;
    private final ProductSpecificationBuilder specBuilder;

    @GetMapping
    public Page<ProductCatalogEntity> search(@ModelAttribute
                                             ProductSearchCriteria criteria, Pageable pageable) {
        Specification<ProductCatalogEntity> spec =
                specBuilder.build(criteria);
        return repository.findAll(spec, pageable);
    }
}
