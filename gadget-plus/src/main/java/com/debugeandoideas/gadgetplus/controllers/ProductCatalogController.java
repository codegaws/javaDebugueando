package com.debugeandoideas.gadgetplus.controllers;

import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import com.debugeandoideas.gadgetplus.services.ProductCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping(path = "product-catalog")
@RequiredArgsConstructor
public class ProductCatalogController {

    private final ProductCatalogService productCatalogService;

    @GetMapping(path = "{id}")// este id se debe llamar
    public ResponseEntity<ProductCatalogEntity> getById(@PathVariable String id) {// aqui recibes un UUID en forma de string
        return ResponseEntity.ok(this.productCatalogService.findById(UUID.fromString(id)));
    }
}
