package com.debugeandoideas.gadgetplus.controllers;

import com.debugeandoideas.gadgetplus.Enum.LikeKey;
import com.debugeandoideas.gadgetplus.dto.DateEval;
import com.debugeandoideas.gadgetplus.dto.ReportProduct;
import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import com.debugeandoideas.gadgetplus.services.ProductCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
    public ResponseEntity<ProductCatalogEntity> getById(@PathVariable String id) {// aqui recibes un UUID en forma de string y debe llamarse igual "id"
        return ResponseEntity.ok(this.productCatalogService.findById(UUID.fromString(id)));
    }

    @GetMapping(path = "name/{name}")
    public ResponseEntity<ProductCatalogEntity> getByname(@PathVariable String name) {
        return ResponseEntity.ok(this.productCatalogService.findByName(name));
    }

    @GetMapping(path = "like/{key}")
    public ResponseEntity<List<ProductCatalogEntity>> getByNameLike(@PathVariable LikeKey key, @RequestParam String word) {
        // comodin
        final var placeholder = "%";
        if (key.equals(LikeKey.AFTER)) {
            return ResponseEntity.ok(this.productCatalogService.findNameLike(placeholder + word));
        }
        if (key.equals(LikeKey.BEFORE)) {
            return ResponseEntity.ok(this.productCatalogService.findNameLike(word + placeholder));
        }
        if (key.equals(LikeKey.BETWEEN)) {
            return ResponseEntity.ok(this.productCatalogService.findNameLike(placeholder + word + placeholder));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(path = "between")
    public ResponseEntity<List<ProductCatalogEntity>> getBetween(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return ResponseEntity.ok(this.productCatalogService.findNameBetween(min, max));
    }

    // Por Categoria aplicando JPQL
    @GetMapping(path = "category")
    public ResponseEntity<List<ProductCatalogEntity>> getByCategory(@RequestParam Long id) {
        return ResponseEntity.ok(this.productCatalogService.findByCategory(id));
    }

    //clase 64 prueba personalizada usando JPQL - > es el mismo que el de abajo
    @GetMapping(path = "/anteriores-a/{fecha}")
    public ResponseEntity<List<ProductCatalogEntity>> getAnterioresA(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        System.out.println("📅 Fecha recibida: " + fecha);
        return ResponseEntity.ok(this.productCatalogService.buscarAnterioresA(fecha));
    }

    @GetMapping(path = "/despues-de/{fecha}")
    public ResponseEntity<List<ProductCatalogEntity>> getDespuesDe(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        System.out.println("📅 Fecha recibida: " + fecha);
        return ResponseEntity.ok(this.productCatalogService.buscarDespuesDe(fecha));
    }
    //***********************************************************************/

    @GetMapping(path = "date-launch/{key}")
    public ResponseEntity<List<ProductCatalogEntity>> getByDate(@PathVariable DateEval key, @RequestParam LocalDate date) {
        return ResponseEntity.ok(this.productCatalogService.findByLauchingDate(date, key));
    }

    @GetMapping(path = "brand-rating")
    public ResponseEntity<List<ProductCatalogEntity>> getByBrandAndRating(@RequestParam String brand, @RequestParam Short rating) {
        //return ResponseEntity.ok(this.productCatalogService.findByBrandAndRating(brand, rating));
        return ResponseEntity.ok(this.productCatalogService.findByBrandOrRating(brand, rating));
    }

    // aplicando el reporte CLASE 69
    @GetMapping(path = "report")
    public ResponseEntity<List<ReportProduct>> getReport() {
        return ResponseEntity.ok(this.productCatalogService.makeReport());
    }

    // aplicando el reporte CLASE 71
    @GetMapping(path = "all")
    public ResponseEntity<Page<ProductCatalogEntity>> getAll(
            @RequestParam(required = false) String field,// como este parametro puede ser nulo importante validar su nulabilidad en el service
            @RequestParam(required = true) Boolean desc,
            @RequestParam(required = true) Integer page
    ) {
        return ResponseEntity.ok(this.productCatalogService.findAll(field, desc, page));
    }

    // aplicando el reporte CLASE 73 paginacion personalizada
    @GetMapping(path = "all-ByBrand")
    public ResponseEntity<Page<ProductCatalogEntity>> getAllByBrand(
            @RequestParam String brand,// no pongo required = false porque es obligatorio
            @RequestParam Integer page
    ) {
        return ResponseEntity.ok(this.productCatalogService.findAllByBrand(brand, page));
    }

    //clase 75
    // aplicando el reporte CLASE 69
    @GetMapping(path = "brand-count/{brand}")
    public ResponseEntity<Integer> getCountByBrand(@PathVariable String brand) {//Aqui devuelve una lista de enteros
        return ResponseEntity.ok(this.productCatalogService.countByBrand(brand));
    }
}
