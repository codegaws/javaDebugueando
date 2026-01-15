## ⃣📚**JPA SPECIFCATIONS Y CRITERIA⏭️⏭️ ****

### JPA CRITERIA API

![IMG](images/img_24.png)

![IMG](images/img_25.png)

![IMG](images/img_26.png)

## ⃣📚**Clase 123:EXPLICACION DEL REQUERIMIENTO DEL PDF ⏭️⏭️ ****

```sql

-- Parámetros: brand = 'Amazon', hasDiscount = true
SELECT DISTINCT pc.*
FROM products_catalog pc
WHERE pc.brand_name = 'Amazon'
  AND pc.is_discount = true
ORDER BY pc.rating DESC, pc.price ASC
LIMIT 20 OFFSET 0;
-- Resultado esperado: 2 productos
-- • Alexa super (200.89, rating: 4)
-- • Alexa small (500.89, rating: 1)
```
#### explicacion :
Exacto, estás correcto pero hay una pequeña precisión:

**LIMIT 20 OFFSET 0** significa:
- **LIMIT 20**: máximo 20 filas en el resultado
- **OFFSET 0**: saltar 0 filas desde el inicio (comenzar desde la primera fila)

**Para paginación:**
- `OFFSET 0` = página 1 (filas 1-20)
- `OFFSET 20` = página 2 (filas 21-40)
- `OFFSET 40` = página 3 (filas 41-60)

**Fórmula común:**
```sql
LIMIT [tamaño_página] OFFSET [página * tamaño_página - tamaño_página]
```

**Ejemplos con páginas de 20 elementos:**
```sql
-- Página 1
LIMIT 20 OFFSET 0

-- Página 2  
LIMIT 20 OFFSET 20

-- Página 3
LIMIT 20 OFFSET 40
```

En tu caso con `LIMIT 20 OFFSET 0`, obtienes la **primera página** con máximo 20 resultados.

---

### Explicacion sobre 128 combinaciones (2^7) en el contexto del patrón Specification


Analizando el documento, veo que el problema se refiere a un sistema de búsqueda con **7 criterios opcionales**. Te explico de dónde salen las 128 combinaciones:

## ¿De dónde sale 2^7 = 128?

Cada criterio de búsqueda puede estar en **2 estados**:
- ✅ **Presente** (se usa en la búsqueda)
- ❌ **Ausente** (no se usa en la búsqueda)

Con 7 criterios independientes: 2 × 2 × 2 × 2 × 2 × 2 × 2 = 2^7 = **128 combinaciones**

## Ejemplos de combinaciones posibles:

Supongamos que los 7 criterios son: `nombre`, `edad`, `ciudad`, `profesión`, `salario`, `experiencia`, `estado`

### Algunas de las 128 combinaciones:

1. **Ningún criterio** (búsqueda general)
   ```sql
   SELECT * FROM empleados
   ```

2. **Solo nombre**
   ```sql
   SELECT * FROM empleados WHERE nombre = ? 
   ```

3. **Solo edad**
   ```sql
   SELECT * FROM empleados WHERE edad = ?
   ```

4. **Nombre + edad**
   ```sql
   SELECT * FROM empleados WHERE nombre = ? AND edad = ?
   ```

5. **Nombre + ciudad**
   ```sql
   SELECT * FROM empleados WHERE nombre = ? AND ciudad = ?
   ```

6. **Edad + ciudad + salario**
   ```sql
   SELECT * FROM empleados WHERE edad = ? AND ciudad = ? AND salario = ?
   ```

7. **Todos los criterios**
   ```sql
   SELECT * FROM empleados 
   WHERE nombre = ? AND edad = ?  AND ciudad = ? 
   AND profesión = ? AND salario = ? AND experiencia = ?  AND estado = ?
   ```

### Representación binaria de las combinaciones:

| Combinación | nombre | edad | ciudad | profesión | salario | experiencia | estado |
|-------------|---------|------|--------|-----------|---------|-------------|---------|
| 1           | 0       | 0    | 0      | 0         | 0       | 0           | 0       |
| 2           | 1       | 0    | 0      | 0         | 0       | 0           | 0       |
| 3           | 0       | 1    | 0      | 0         | 0       | 0           | 0       |
| 4           | 1       | 1    | 0      | 0         | 0       | 0           | 0       |
| ...          | ...     | ...  | ...    | ...       | ...     | ...         | ...     |
| 128         | 1       | 1    | 1      | 1         | 1       | 1           | 1       |

## El problema del enfoque tradicional:

Sin el patrón **Specification**, necesitarías crear métodos como:

```java
// Solo algunos ejemplos de los 128 métodos necesarios:
findByNombre(String nombre)
findByEdad(Integer edad)
findByNombreAndEdad(String nombre, Integer edad)
findByNombreAndCiudad(String nombre, String ciudad)
findByEdadAndCiudadAndSalario(Integer edad, String ciudad, Double salario)
findByNombreAndEdadAndCiudadAndProfesionAndSalarioAndExperienciaAndEstado(...)
// ... y 122 métodos más!
```

Por eso el documento propone el **patrón Specification**, que permite combinar criterios dinámicamente sin necesidad de crear todos esos métodos explícitamente. 

Excelente pregunta. Te explico detalladamente de dónde surgen estas **128 combinaciones** y por qué se usa **2^7**.

## ¿De dónde sale el número 2?

El **2** representa las **dos posibilidades** que tiene cada criterio de búsqueda:

1. **Está presente** en la búsqueda (se usa como filtro)
2. **No está presente** en la búsqueda (se ignora)

## ¿Por qué 2^7?

Con **7 criterios de búsqueda**, cada uno puede estar **presente o ausente**, entonces:

- Criterio 1: 2 opciones (presente/ausente)
- Criterio 2: 2 opciones (presente/ausente)
- Criterio 3: 2 opciones (presente/ausente)
- ...  y así sucesivamente

**Total de combinaciones = 2 × 2 × 2 × 2 × 2 × 2 × 2 = 2^7 = 128**

## Ejemplos de las 128 combinaciones posibles

Según el documento, los criterios son:

1. **Nombre**
2. **Email**
3. **Teléfono**
4. **Fecha de creación desde**
5. **Fecha de creación hasta**
6. **Activo/Inactivo**
7. **Departamento**

### Algunos ejemplos de las combinaciones:

```java
// Combinación 1: Ningún criterio (búsqueda sin filtros)
findAll()

// Combinación 2: Solo nombre
findByNombre(String nombre)

// Combinación 3: Solo email
findByEmail(String email)

// Combinación 4: Nombre + Email
findByNombreAndEmail(String nombre, String email)

// Combinación 5: Nombre + Teléfono
findByNombreAndTelefono(String nombre, String telefono)

// Combinación 6: Solo fecha desde
findByFechaCreacionGreaterThanEqual(Date fechaDesde)

// Combinación 7: Nombre + Email + Activo
findByNombreAndEmailAndActivo(String nombre, String email, Boolean activo)

// Combinación 8: Todos los criterios
findByNombreAndEmailAndTelefonoAndFechaCreacionBetweenAndActivoAndDepartamento(
    String nombre, String email, String telefono, 
    Date fechaDesde, Date fechaHasta, Boolean activo, String departamento)

// ... y así hasta 128 combinaciones diferentes
```

### Representación binaria de las combinaciones:

Cada combinación se puede representar en binario (1 = presente, 0 = ausente):

```
Combinación 1:  0000000 (ningún criterio)
Combinación 2:  0000001 (solo criterio 7)  
Combinación 3:  0000010 (solo criterio 6)
Combinación 4:  0000011 (criterios 6 y 7)
...
Combinación 128: 1111111 (todos los criterios)
```

## El problema que esto genera

Sin el patrón Specification, necesitarías crear **128 métodos diferentes** en tu repositorio:

```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Solo algunos ejemplos de los 128 métodos necesarios
    List<Usuario> findByNombre(String nombre);
    List<Usuario> findByEmail(String email);
    List<Usuario> findByNombreAndEmail(String nombre, String email);
    List<Usuario> findByNombreAndEmailAndTelefono(String nombre, String email, String telefono);
    List<Usuario> findByActivoAndDepartamento(Boolean activo, String departamento);
    // ... 123 métodos más
}
```

## La solución con Specification Pattern

En lugar de 128 métodos, tienes **UNA SOLA búsqueda flexible**:

```java
public Page<Usuario> buscarUsuarios(UsuarioCriteria criteria, Pageable pageable) {
    Specification<Usuario> spec = UsuarioSpecification.conCriterios(criteria);
    return usuarioRepository.findAll(spec, pageable);
}
```

**¡Por eso el Specification Pattern es tan poderoso!** Reduce 128 métodos a una sola implementación flexible. 

## ⃣📚**Clase 124:CRITERIA BUILDER ⏭️⏭️ ****
 EXPLICACION DEL CODIGO : AUN ESTA AQUI EN ESTE CAPITULO INCOMPLETO
 
   
```java
package com.debugeandoideas.gadgetplus.specs;

import com.debugeandoideas.gadgetplus.dto.ProductSearchCriteria;
import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ProductSpecificationBuilder {

    //el root es la entidad a la que le vamos a aplicar los criterios
    public Specification<ProductCatalogEntity> build(ProductSearchCriteria criteria) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            //validamos la nulabilidad
            if (Objects.nonNull(criteria.getBrand())) {
                predicates.add(cb.equal(root.get("brand"), criteria.getBrand()));
            }
        }
    }
}

```
# EXPLICACION DEL CODIGO:
Te explico el código desarrollado hasta este punto:

## Análisis del código actual

**ProductSpecificationBuilder** es un builder que construye especificaciones dinámicas para consultas JPA usando Criteria API.

### Estructura del Specification

```java
return (root, query, cb) -> {
    // Lógica de construcción de predicados
}
```

- **root**: Representa la entidad raíz (`ProductCatalogEntity`) - es como el "FROM" en SQL
- **query**: Objeto CriteriaQuery para construir la consulta completa
- **cb**: CriteriaBuilder que proporciona métodos para crear predicados y expresiones

### Sobre List<Predicate>

**No**, `Predicate` no es una lista de booleanos. Un `Predicate` representa una **condición/criterio** en la consulta SQL. Por ejemplo:

- `cb.equal(root.get("brand"), criteria.getBrand())` crea un predicado equivalente a `WHERE brand = 'valor'`

La `List<Predicate>` almacena **múltiples condiciones** que luego se combinan (generalmente con AND/OR).

### Problema en tu código

Falta el return statement. El método debe retornar un `Predicate`:

```java
public Specification<ProductCatalogEntity> build(ProductSearchCriteria criteria) {
    return (root, query, cb) -> {
        final List<Predicate> predicates = new ArrayList<>();
        
        if (Objects.nonNull(criteria.getBrand())) {
            predicates.add(cb.equal(root.get("brand"), criteria.getBrand()));
        }
        
        // Combinar todos los predicados con AND
        return cb.and(predicates.toArray(new Predicate[0]));
    };
}
```

### Uso típico

Cuando agregas más criterios, cada `if` agrega un nuevo `Predicate` a la lista, construyendo dinámicamente consultas como:
`SELECT * FROM product_catalog WHERE brand = ? AND price > ? AND category = ?`


```java
package....

@Component
@Slf4j
public class ProductSpecificationBuilder {

    //el root es la entidad a la que le vamos a aplicar los criterios
    public Specification<ProductCatalogEntity> build(ProductSearchCriteria criteria) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            //validamos la nulabilidad
            if (Objects.nonNull(criteria.getBrand())) {
                predicates.add(cb.equal(root.get("brand"), criteria.getBrand()));
            }
            if (Objects.nonNull(criteria.getMinPrice())) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
            }
            if (Objects.nonNull(criteria.getMaxPrice())) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
            }
            if (Objects.nonNull(criteria.getMinRating())) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), criteria.getMinRating()));
            }
            if (Objects.nonNull(criteria.getHasDiscount())) {
                predicates.add(cb.equal(root.get("isDescount"), criteria.getHasDiscount()));
            }

            //***************ahora toca category que es un JOIN asi se trabaja con JPA Criteria API*****************
            if (Objects.nonNull(criteria.getCategoryCode())) {
                Join<ProductCatalogEntity, CategoryEntity> categoryJoin =
                        root.join("categories", JoinType.INNER);

                predicates.add(cb.equal(categoryJoin.get("code").as(String.class),//ojo al as(String.class) para que me envie en string
                        criteria.getCategoryCode()));

                //para que no me envie resultados duplicados
                query.distinct(true);
            }

            if (Objects.nonNull(criteria.getLaunchedAfter())) {
                predicates.add(cb.greaterThan(root.get("launchingDate"), criteria.getLaunchedAfter()));
            }

            //ORDER-BY por precio ascendente
            query.orderBy(cb.desc(root.get("rating")), cb.asc(root.get("price")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };// fin del return
    }// build
}//fin ProductSpecificationBuilder

```

---

# EXPLICACION DEL CODIGO COMPLETO:
## Análisis completo del ProductSpecificationBuilder

Este código crea un **builder de especificaciones dinámicas** para construir consultas JPA de forma programática, equivalente a generar SQL dinámico.

### **Propósito general**
Construye consultas flexibles donde los filtros se aplican solo si los criterios de búsqueda tienen valores, evitando consultas con parámetros nulos.

---

## **Desglose por secciones**

### **1. Filtros básicos por campos simples**

```java
// Filtro por marca
if (Objects.nonNull(criteria.getBrand())) {
    predicates.add(cb.equal(root.get("brand"), criteria.getBrand()));
}
```
**SQL equivalente:** `WHERE brand = 'Samsung'`

```java
// Filtros por rango de precio
if (Objects.nonNull(criteria.getMinPrice())) {
    predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
}
if (Objects.nonNull(criteria.getMaxPrice())) {
    predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
}
```
**SQL equivalente:** `WHERE price >= 100 AND price <= 500`

### **2. Filtro con JOIN**

```java
if (Objects.nonNull(criteria.getCategoryCode())) {
    Join<ProductCatalogEntity, CategoryEntity> categoryJoin = 
            root.join("categories", JoinType.INNER);
    
    predicates.add(cb.equal(categoryJoin.get("code").as(String.class), 
            criteria.getCategoryCode()));
    
    query.distinct(true);
}
```

**SQL equivalente:**
```sql
SELECT DISTINCT pc.* 
FROM product_catalog pc 
INNER JOIN category c ON pc.category_id = c.id 
WHERE c.code = 'ELECTRONICS'
```

- **`root.join("categories", JoinType.INNER)`**: Crea un INNER JOIN
- **`.as(String.class)`**: Convierte el tipo de dato a String
- **`query.distinct(true)`**: Evita registros duplicados por el JOIN

### **3. Ordenamiento**

```java
query.orderBy(cb.desc(root.get("rating")), cb.asc(root.get("price")));
```

**SQL equivalente:** `ORDER BY rating DESC, price ASC`

### **4. Combinación final**

```java
return cb.and(predicates.toArray(new Predicate[0]));
```

---

## **¿Qué es `new Predicate[0]`?**

Es un **array vacío** usado como patrón para convertir la `List<Predicate>` en un array de `Predicate[]`.

**Funcionamiento:**
- Si tienes 3 predicados, `toArray(new Predicate[0])` crea un array `Predicate[3]`
- `cb.and()` requiere un array de predicados, no una lista

**Resultado final:** Combina todos los predicados con operador **AND**

---

## **SQL final generado (ejemplo)**

Si pasas criterios: brand="Samsung", minPrice=100, categoryCode="PHONES"

```sql
SELECT DISTINCT pc.* 
FROM product_catalog pc 
INNER JOIN category c ON pc.category_id = c.id 
WHERE pc.brand = 'Samsung' 
  AND pc.price >= 100 
  AND c.code = 'PHONES'
ORDER BY pc.rating DESC, pc.price ASC
```

La ventaja es que si `minPrice` es null, esa condición simplemente no se incluye en la consulta final.

## ⃣📚**Clase 125:CRITERIA BUILDER ⏭️⏭️ ****

# explicacion del codigo 

## ⃣📚**Clase 126:EXPLICACION DETALLADA DE NUESTRO CRITERIA BUILDER⏭️⏭️ ****

## Explicacion sobre Controlador ProductController

## ¿Para qué sirve `@ModelAttribute`?

`@ModelAttribute` es una anotación de Spring que **vincula automáticamente los parámetros de la URL** (query parameters) con un objeto Java.

### **Funcionamiento en tu código**

```java
@GetMapping
public Page<ProductCatalogEntity> search(@ModelAttribute ProductSearchCriteria criteria, 
                                        Pageable pageable) {
    // ...
}
```

### **Ejemplo práctico**

Cuando haces una petición GET con parámetros:

```
GET /filter/products?brand=Samsung&minPrice=100&maxPrice=500&minRating=4&hasDiscount=true&categoryCode=PHONES
```

**Sin `@ModelAttribute`** tendrías que hacer esto:

```java
@GetMapping
public Page<ProductCatalogEntity> search(@RequestParam(required = false) String brand,
                                        @RequestParam(required = false) Double minPrice,
                                        @RequestParam(required = false) Double maxPrice,
                                        @RequestParam(required = false) Double minRating,
                                        @RequestParam(required = false) Boolean hasDiscount,
                                        @RequestParam(required = false) String categoryCode,
                                        Pageable pageable) {
    ProductSearchCriteria criteria = new ProductSearchCriteria();
    criteria.setBrand(brand);
    criteria.setMinPrice(minPrice);
    criteria.setMaxPrice(maxPrice);
    criteria.setMinRating(minRating);
    criteria.setHasDiscount(hasDiscount);
    criteria.setCategoryCode(categoryCode);
    // ...
}
```

**Con `@ModelAttribute`** Spring hace el mapeo automáticamente:

```java
@GetMapping
public Page<ProductCatalogEntity> search(@ModelAttribute ProductSearchCriteria criteria, 
                                        Pageable pageable) {
    // criteria ya está poblado automáticamente
}
```

### **Mapeo automático**

Spring toma cada parámetro de la URL y lo asigna al campo correspondiente en `ProductSearchCriteria`:

| Parámetro URL | Campo en ProductSearchCriteria |
|---------------|--------------------------------|
| `brand=Samsung` | `criteria.setBrand("Samsung")` |
| `minPrice=100` | `criteria.setMinPrice(100.0)` |
| `maxPrice=500` | `criteria.setMaxPrice(500.0)` |
| `minRating=4` | `criteria.setMinRating(4.0)` |
| `hasDiscount=true` | `criteria.setHasDiscount(true)` |
| `categoryCode=PHONES` | `criteria.setCategoryCode("PHONES")` |

### **Ventajas de usar `@ModelAttribute`**

1. **Código más limpio**: Menos parámetros en el método
2. **Mapeo automático**: Spring se encarga de la conversión de tipos
3. **Validación**: Puedes usar anotaciones de validación en la clase
4. **Flexibilidad**: Fácil agregar/quitar criterios sin cambiar la firma del método

### **Ejemplo de uso completo**

```java
// URL de ejemplo
// GET /filter/products?brand=Samsung&minPrice=100&categoryCode=ELECTRONICS&page=0&size=20

// Spring automáticamente crea:
ProductSearchCriteria criteria = new ProductSearchCriteria();
criteria.setBrand("Samsung");        // desde brand=Samsung
criteria.setMinPrice(100.0);         // desde minPrice=100
criteria.setCategoryCode("ELECTRONICS"); // desde categoryCode=ELECTRONICS
// Los demás campos quedan null

// También maneja Pageable automáticamente:
Pageable pageable = PageRequest.of(0, 20); // desde page=0&size=20
```

**¡Es una forma muy elegante de manejar múltiples parámetros de búsqueda!**
