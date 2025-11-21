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

    /**
     * En Spring Boot, marcar el atributo productCatalogService como final es una buena práctica porque:
     * Inmutabilidad: Garantiza que la referencia al servicio no pueda ser cambiada después de la construcción del controlador.
     * Seguridad en concurrencia: Hace que la clase sea más segura en entornos multihilo, ya que el valor no puede ser reasignado.
     * Compatibilidad con @RequiredArgsConstructor: Lombok genera automáticamente un constructor que inicializa los campos final (y los marcados como @NonNull), facilitando la inyección de dependencias.
     */
    private final ProductCatalogService productCatalogService;

    @GetMapping(path = "{id}")// este id se debe llamar
    public ResponseEntity<ProductCatalogEntity> getById(@PathVariable String id) {// aqui recibes un UUID en forma de string
        return ResponseEntity.ok(this.productCatalogService.findById(UUID.fromString(id)));
    }
}
