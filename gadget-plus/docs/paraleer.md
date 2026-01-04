# 📚 Guía Completa: Relaciones Bidireccionales, `@ToString` y `@JsonIgnore`

## 🔍 **1. Análisis de tus relaciones**

### 📊 **Mapa de relaciones en tu proyecto**

```
┌─────────────────────┐       1:N        ┌─────────────────────┐
│   OrderEntity       │◄────────────────►│   ProductEntity     │
│   (orders)          │  products/order  │   (products)        │
└─────────────────────┘                  └─────────────────────┘
         │                                         │
         │ 1:1                                     │ N:1
         ▼                                         ▼
┌─────────────────────┐                  ┌─────────────────────┐
│   BillEntity        │                  │ ProductCatalogEntity│
│   (bill)            │                  │ (products_catalog)  │
└─────────────────────┘                  └─────────────────────┘
                                                   │
                                                   │ N:N
                                                   ▼
                                         ┌─────────────────────┐
                                         │   CategoryEntity    │
                                         │   (categories)      │
                                         └─────────────────────┘
```

---

## 🎯 **2. Respuestas a tus consultas**

### ❓ **¿Por qué `CategoryEntity` ↔ `ProductCatalogEntity` es bidireccional pero `ProductEntity` → `ProductCatalogEntity` no?**

| Relación | Tipo | ¿Bidireccional? | ¿Por qué? |
|----------|------|-----------------|-----------|
| `Category` ↔ `ProductCatalog` | N:N | ✅ **SÍ** | Necesitas navegar en ambas direcciones (productos de una categoría Y categorías de un producto) |
| `ProductEntity` → `ProductCatalog` | N:1 | ❌ **NO** | Solo necesitas saber qué catálogo tiene un producto, no qué productos tienen ese catálogo |

### 📝 **Regla de diseño**

> 🎯 **Solo crea relaciones bidireccionales cuando NECESITAS navegar en ambas direcciones.**

```java
// ProductEntity → ProductCatalogEntity (UNIDIRECCIONAL)
// ✅ Puedes hacer: product.getCatalog()
// ❌ NO puedes hacer: catalog.getProducts() ← No existe

// CategoryEntity ↔ ProductCatalogEntity (BIDIRECCIONAL)
// ✅ Puedes hacer: category.getProductCatalog()
// ✅ Puedes hacer: productCatalog.getCategories()
```

---

### ❓ **¿En `OrderEntity` es necesario tener una lista de productos pero en `ProductCatalogEntity` no?**

| Entidad | ¿Lista necesaria? | Razón |
|---------|------------------|-------|
| `OrderEntity.products` | ✅ **SÍ** | Una orden DEBE saber qué productos contiene para calcular totales, mostrar detalles, etc. |
| `ProductCatalogEntity.products` | ❌ **NO** | El catálogo NO necesita saber qué productos de órdenes lo referencian |

### 💡 **Ejemplo práctico**

```java
// ✅ Caso de uso real: Mostrar una orden
OrderEntity orden = orderRepository.findById(1L);
orden.getProducts().forEach(p -> {
    System.out.println(p.getCatalog().getName());  // ← Necesitas la lista
});

// ❌ Caso innecesario: ¿Cuántas órdenes tienen este catálogo?
// Esto se hace con una QUERY, no con una relación bidireccional
productCatalogRepository.findOrdersByProductCatalogId(catalogId);
```

---

## 🔄 **3. Diferencia entre `@ToString` y `@ToString.Exclude`**

### 📋 **Comparación**

| Anotación | Ubicación | Efecto |
|-----------|-----------|--------|
| `@ToString` | **Clase** | Genera `toString()` con TODOS los campos |
| `@ToString(exclude = {"campo1", "campo2"})` | **Clase** | Genera `toString()` excluyendo campos específicos |
| `@ToString.Exclude` | **Campo** | Excluye ESE campo específico del `toString()` |

### 📝 **Son equivalentes**

```java
// Opción 1: Exclude en la clase
@ToString(exclude = {"order", "catalog"})
public class ProductEntity {
    private OrderEntity order;
    private ProductCatalogEntity catalog;
}

// Opción 2: Exclude en cada campo (EQUIVALENTE)
@ToString
public class ProductEntity {
    @ToString.Exclude
    private OrderEntity order;
    
    @ToString.Exclude
    private ProductCatalogEntity catalog;
}
```

### 🎯 **¿Cuál usar?**

| Situación | Recomendación |
|-----------|---------------|
| Excluir 1\-2 campos | `@ToString.Exclude` en el campo |
| Excluir muchos campos | `@ToString(exclude = {...})` en la clase |
| Consistencia en proyecto | Elegir UNA forma y mantenerla |

---

## ⚠️ **4. Regla para saber cuándo usar `@ToString.Exclude`**

### 🎯 **Regla de oro**

> 🔴 **USA `@ToString.Exclude` cuando el campo referencia OTRA ENTIDAD que a su vez te referencia a ti.**

### 📊 **Tabla de decisión**

| Pregunta | Respuesta | Acción |
|----------|-----------|--------|
| ¿El campo es una entidad? | ❌ No (String, int, etc.) | No necesitas exclude |
| ¿El campo es una entidad? | ✅ Sí | Sigue preguntando ↓ |
| ¿Esa entidad tiene un campo que me referencia? | ❌ No | No necesitas exclude |
| ¿Esa entidad tiene un campo que me referencia? | ✅ Sí | ⚠️ **USA `@ToString.Exclude`** |

### 📝 **Aplicación en tu código**

```java
// ProductCatalogEntity
@ToString  // ← Sin exclude porque:
public class ProductCatalogEntity {
    
    private List<CategoryEntity> categories;
    // ↑ CategoryEntity SÍ me referencia (productCatalog)
    // PERO... CategoryEntity ya tiene @ToString.Exclude
    // Entonces NO hay ciclo infinito
}
```

### 🔍 **¿Por qué `ProductCatalogEntity` no tiene exclude?**

```
ProductCatalogEntity.toString()
    ↓
    categories = [CategoryEntity.toString()]
                      ↓
                      productCatalog = EXCLUIDO ← @ToString.Exclude
                      ✅ SE DETIENE AQUÍ
```

> 💡 **Solo necesitas `@ToString.Exclude` en UN LADO de la relación bidireccional.**

---

## 🌐 **5. Explicación de `@JsonIgnore`**

### ❓ **¿Qué es `@JsonIgnore`?**

Es una anotación de **Jackson** (librería de serialización JSON) que excluye un campo cuando conviertes un objeto a JSON.

### 🔄 **Diferencia con `@ToString.Exclude`**

| Anotación | Librería | ¿Cuándo se usa? |
|-----------|----------|-----------------|
| `@ToString.Exclude` | Lombok | Al llamar `objeto.toString()` |
| `@JsonIgnore` | Jackson | Al serializar a JSON (APIs REST) |

### 📝 **Ejemplo práctico**

```java
@RestController
public class CategoryController {
    
    @GetMapping("/categories/{id}")
    public CategoryEntity getCategory(@PathVariable Long id) {
        return categoryRepository.findById(id).get();
        // ↑ Esto serializa a JSON automáticamente
    }
}
```

### ⚠️ **SIN `@JsonIgnore` - Problema**

```java
// CategoryEntity SIN @JsonIgnore
@ManyToMany(mappedBy = "categories")
private List<ProductCatalogEntity> productCatalog;
```

**Respuesta JSON (CICLO INFINITO):**

```json
{
  "id": 1,
  "code": "ELECTRONICS",
  "productCatalog": [
    {
      "id": "uuid-123",
      "name": "iPhone",
      "categories": [
        {
          "id": 1,
          "code": "ELECTRONICS",
          "productCatalog": [
            {
              "id": "uuid-123",
              "categories": [
                // ∞ INFINITO → StackOverflowError
              ]
            }
          ]
        }
      ]
    }
  ]
}
```

### ✅ **CON `@JsonIgnore` - Solución**

```java
// CategoryEntity CON @JsonIgnore
@ManyToMany(mappedBy = "categories")
@JsonIgnore  // ← Excluye del JSON
private List<ProductCatalogEntity> productCatalog;
```

**Respuesta JSON (LIMPIA):**

```json
{
  "id": 1,
  "code": "ELECTRONICS",
  "description": "Productos electrónicos"
}
```

> 💡 **`productCatalog` NO aparece en el JSON**

---

## 📊 **6. Resumen: ¿Cuándo usar cada anotación?**

| Situación | `@ToString.Exclude` | `@JsonIgnore` |
|-----------|---------------------|---------------|
| Relación bidireccional | ✅ En UN lado | ✅ En UN lado |
| Campo sensible (password) | ✅ Opcional | ✅ **Obligatorio** |
| Campo técnico (audit) | ✅ Opcional | ✅ Opcional |
| Evitar ciclo en `toString()` | ✅ **Necesario** | ❌ No aplica |
| Evitar ciclo en API REST | ❌ No aplica | ✅ **Necesario** |

---

## 🏆 **7. Reglas finales para tu proyecto**

### ✅ **Checklist para nuevas entidades**

```markdown
1. [ ] ¿Tiene relación bidireccional?
   - SI → Agregar @ToString.Exclude en UN lado
   - NO → No hacer nada

2. [ ] ¿Se expone en API REST?
   - SI → Agregar @JsonIgnore en el lado "inverso" (mappedBy)
   - NO → No hacer nada

3. [ ] ¿Es el lado "propietario" (@JoinColumn)?
   - SI → Generalmente NO lleva @JsonIgnore
   - NO (mappedBy) → Generalmente SÍ lleva @JsonIgnore
```

### 📝 **Tu código está correcto**

| Entidad | `@ToString.Exclude` | `@JsonIgnore` | Estado |
|---------|---------------------|---------------|--------|
| `OrderEntity` | `{"bill", "products"}` | - | ✅ |
| `ProductEntity` | `{"order", "catalog"}` | - | ✅ |
| `BillEntity` | `{"order"}` | - | ✅ |
| `CategoryEntity` | `productCatalog` | `productCatalog` | ✅ |
| `ProductCatalogEntity` | - | - | ✅ (el otro lado ya está protegido) |


# OTRA CONSULTA
# 🎯 **Análisis de tus `@ToString.Exclude`**

## ✅ **Tienes razón parcialmente**

### 📊 **Revisión de tus relaciones**

| Relación | Tipo | ¿Bidireccional? | ¿Exclude necesario? |
|----------|------|-----------------|---------------------|
| `OrderEntity` ↔ `BillEntity` | 1:1 | ✅ **SÍ** | ✅ En **UN** lado |
| `OrderEntity` ↔ `ProductEntity` | 1:N | ✅ **SÍ** | ✅ En **UN** lado |
| `ProductEntity` → `ProductCatalogEntity` | N:1 | ❌ **NO** | ❌ **Innecesario** |

---

## 🔍 **Detalle de cada caso**

### 1️⃣ **OrderEntity ↔ BillEntity (1:1 Bidireccional)**

```java
// OrderEntity
@OneToOne
private BillEntity bill;  // ← Referencia a Bill

// BillEntity  
@OneToOne(mappedBy = "bill")
private OrderEntity order;  // ← Referencia de vuelta
```

> ✅ **Necesitas exclude en UN lado** → Lo tienes correcto

---

### 2️⃣ **OrderEntity ↔ ProductEntity (1:N Bidireccional)**

```java
// OrderEntity
@OneToMany(mappedBy = "order")
private List<ProductEntity> products;  // ← Lista de productos

// ProductEntity
@ManyToOne
private OrderEntity order;  // ← Referencia de vuelta
```

> ✅ **ES BIDIRECCIONAL** → Necesitas exclude en UN lado

---

### 3️⃣ **ProductEntity → ProductCatalogEntity (N:1 Unidireccional)**

```java
// ProductEntity
@ManyToOne
private ProductCatalogEntity catalog;  // ← Referencia al catálogo

// ProductCatalogEntity
// ❌ NO tiene List<ProductEntity> products
```

> ❌ **NO es bidireccional** → `@ToString(exclude = {"catalog"})` es **INNECESARIO**

---

## 🎯 **Corrección recomendada**

### **ProductEntity.java** (Quitar `catalog` del exclude)

```java
@ToString(exclude = {"order"})  // ← Solo "order" es necesario
public class ProductEntity {
    
    @ManyToOne
    private OrderEntity order;  // ← Bidireccional con OrderEntity
    
    @ManyToOne
    private ProductCatalogEntity catalog;  // ← Unidireccional, NO necesita exclude
}
```

---

## 📋 **Resumen final**

| Entidad | Exclude actual | Exclude necesario | ¿Cambio? |
|---------|---------------|-------------------|----------|
| `OrderEntity` | `{"bill", "products"}` | `{"bill"}` **O** `{"products"}` | ⚠️ Uno sobra* |
| `BillEntity` | `{"order"}` | ✅ Correcto | ❌ No |
| `ProductEntity` | `{"order", "catalog"}` | `{"order"}` | ✅ Quitar `catalog` |

### ⚠️ **\*Nota sobre OrderEntity**

Solo necesitas exclude en **UN lado** de cada relación:

```java
// Opción A: Exclude en OrderEntity
@ToString(exclude = {"bill", "products"})  // OrderEntity
@ToString  // BillEntity y ProductEntity sin exclude

// Opción B: Exclude en el otro lado (tu caso actual)
@ToString(exclude = {"bill"})  // OrderEntity (o sin bill)
@ToString(exclude = {"order"})  // BillEntity
@ToString(exclude = {"order"})  // ProductEntity
```

**Ambas opciones funcionan** → Tu código actual es **seguro pero redundante**.

---

## 💡 **Regla simple**

> 🔴 **`@ToString.Exclude` solo es necesario cuando AMBAS entidades se referencian mutuamente.**
>
> Si A → B pero B no tiene referencia a A, **no hay ciclo posible**.

# OTRA CONSULTA

# 🎯 **Respuesta: ¿Por qué `ProductCatalogEntity` no es bidireccional con `ProductEntity`?**

## ✅ **Exactamente, tienes razón**

`ProductCatalogEntity` **NO tiene ningún campo** que referencie a `ProductEntity`, por eso **no es bidireccional**.

---

## 🔍 **Evidencia en tu código**

### **ProductEntity.java**

```java
@ManyToOne
@JoinColumn(name = "product_catalog_id")
private ProductCatalogEntity catalog;  // ✅ SÍ tiene referencia
```

### **ProductCatalogEntity.java**

```java
// ❌ NO existe esto:
// private List<ProductEntity> products;
```

---

## 📊 **Comparación visual**

| Dirección | ¿Existe? | Código |
|-----------|----------|--------|
| `ProductEntity` → `ProductCatalogEntity` | ✅ **SÍ** | `private ProductCatalogEntity catalog` |
| `ProductCatalogEntity` → `ProductEntity` | ❌ **NO** | No hay campo |

---

## 💡 **Por eso es UNIDIRECCIONAL**

```
ProductEntity ──────────► ProductCatalogEntity
              catalog
              
              ❌ No hay flecha de vuelta
```

> 🎯 **Sin campo de vuelta = Sin relación bidireccional = Sin ciclo = Sin necesidad de `@ToString.Exclude`**

---

## ✅ **Conclusión**

Puedes quitar `"catalog"` del exclude en `ProductEntity`:

```java
@ToString(exclude = {"order"})  // ← Solo "order" necesario
public class ProductEntity {
    // ...
}
```

--- 
# OTRA CONSULA POR QUE NECESITAS JSONIGNORE SOLO EN METODO productCatalog

# 🎯 **Confirmación: Análisis de tus Controllers**

## ✅ **Tu observación es CORRECTA**

---

## 📊 **Comparación de Controllers**

| Controller | ¿Qué devuelve? | ¿Necesita `@JsonIgnore`? |
|------------|----------------|-------------------------|
| `ProductCatalogController` | `ProductCatalogEntity` | ✅ **SÍ** (en entidades relacionadas) |
| `OrderController` | `OrderDTO` | ❌ **NO** |

---

## 🔍 **Evidencia en tu código**

### **ProductCatalogController.java**
```java
@GetMapping(path = "{id}")
public ResponseEntity<ProductCatalogEntity> getById(@PathVariable String id) {
    return ResponseEntity.ok(this.productCatalogService.findById(...));
    // ↑ Devuelve ENTIDAD directamente → Necesita @JsonIgnore en relaciones
}
```

### **OrderController.java**
```java
@GetMapping(path = "{id}")
public ResponseEntity<OrderDTO> get(@PathVariable Long id) {
    return ResponseEntity.ok(ordersCrudService.read(id));
    // ↑ Devuelve DTO → NO necesita @JsonIgnore
}
```

---

## 💡 **Por eso `CategoryEntity` tiene `@JsonIgnore`**

```java
// CategoryEntity.java
@ManyToMany(mappedBy = "categories")
@ToString.Exclude
@JsonIgnore  // ← Necesario porque ProductCatalogController devuelve entidades
private List<ProductCatalogEntity> productCatalog;
```

**Flujo:**
```
ProductCatalogController.getById()
    ↓
ProductCatalogEntity (serializa a JSON)
    ↓
categories → CategoryEntity (serializa)
    ↓
productCatalog → @JsonIgnore DETIENE EL CICLO ✅
```

---

## ✅ **Por eso `OrderEntity` NO necesita `@JsonIgnore`**

```java
// OrderController devuelve OrderDTO, NO OrderEntity
// El mapeo a DTO se hace en el servicio
// → No hay serialización directa de la entidad
// → No hay ciclo posible
```

---

## 📋 **Resumen final**

| Pregunta | Respuesta |
|----------|-----------|
| ¿`OrderEntity` necesita `@JsonIgnore`? | ❌ **NO**, porque usas DTOs |
| ¿`ProductCatalogEntity` necesita `@JsonIgnore`? | ✅ **SÍ**, porque expones la entidad directamente |
| ¿Tu código está correcto? | ✅ **SÍ** |

> 🎯 **Regla confirmada**: `@JsonIgnore` solo es necesario cuando **expones entidades directamente** en controllers, no cuando usas **DTOs**.


---
# OTRA CONSULTA -> POR QUE USAR EL INDEX - INDICE EN PRODUCT_CATALOG
# 📚 **Aclaración sobre `@Index`**

---

## ❌ **No es para buscar por UUID**

El **UUID ya tiene un índice automático** porque es la clave primaria (`@Id`):

```java
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;  // ← JPA crea índice automático (PRIMARY KEY)
```

---

## ✅ **Es SOLO para buscar más rápido por `product_name`**

```java
@Index(name = "idx_product_name", columnList = "product_name")
```

Esto crea un **índice adicional** para acelerar consultas como:

```java
// Consultas que se benefician del índice:
findByName("iPhone");
findByNameContaining("Galaxy");
SELECT * FROM products_catalog WHERE product_name = 'iPhone';
```

---

## 📊 **Resumen visual**

| Columna | ¿Tiene índice? | ¿Quién lo crea? |
|---------|---------------|-----------------|
| `id` (UUID) | ✅ **SÍ** | Automático por `@Id` (PRIMARY KEY) |
| `product_name` | ✅ **SÍ** | Manual por `@Index` |
| `brand_name` | ❌ **NO** | No definido |
| `description` | ❌ **NO** | No definido |

---

## 💡 **¿Por qué agregar índice a `product_name`?**

Porque probablemente harás **muchas búsquedas por nombre** de producto, y sin índice JPA escanearía toda la tabla.

> 🎯 **Regla**: Agrega `@Index` a columnas que uses frecuentemente en cláusulas `WHERE`, `ORDER BY` o `JOIN`.

# OTRA CONSULTA SOBRE EL CREATED

# 📚 **Flujo completo del proceso CREATE Order**

---

## 🎯 **Resumen del flujo**

```
POST /orders (JSON) → Controller → Service → Mapper → Repository → BD
```

---

## 📊 **Paso a paso detallado**

### **1️⃣ Postman envía el JSON**

```json
{
    "clientName" : "Shean Omelley",
    "bill" : {
        "idBill" : "b-21",
        "clientRfc" : "ART655687JHZ"
    },
    "products" : [
        { "quantity" : 2, "name" : "Alexa large" },
        { "quantity" : 1, "name" : "Pc office" },
        { "quantity" : 3, "name" : "TV 75" }
    ]
}
```

---

### **2️⃣ Controller recibe y llama al Service**

```java
@PostMapping
public ResponseEntity<String> create(@RequestBody OrderDTO order) {
    return ResponseEntity.ok(ordersCrudService.create(order));
}
```

Spring deserializa el JSON → `OrderDTO` automáticamente.

---

### **3️⃣ Service.create() inicia el proceso**

```java
@Override
public String create(OrderDTO order) {
    final var toInsert = this.mapOrderFromDto(order); // ← Mapea DTO → Entity
    return this.orderRepository.save(toInsert).getId().toString();
}
```

---

### **4️⃣ mapOrderFromDto() - El corazón del mapeo**

```java
private OrderEntity mapOrderFromDto(OrderDTO orderDTO) {
    final var orderResponse = new OrderEntity();  // ① Crea entidad vacía
    final var modelMapper = new ModelMapper();

    // ② Mapeo personalizado para Bill
    modelMapper
        .typeMap(BillDTO.class, BillEntity.class)
        .addMappings(mapper -> mapper.map(
            BillDTO::getIdBill, BillEntity::setId));  // idBill → id

    modelMapper.map(orderDTO, orderResponse);  // ③ Mapea campos automáticamente

    this.getAndSetProducts(orderDTO.getProducts(), orderResponse);  // ④ Productos

    return orderResponse;
}
```

#### **¿Qué hace ModelMapper automáticamente?**

| Campo DTO | Campo Entity | ¿Automático? |
|-----------|--------------|--------------|
| `clientName` | `clientName` | ✅ SÍ |
| `bill.idBill` | `bill.id` | ⚠️ Necesita mapeo manual |
| `bill.clientRfc` | `bill.clientRfc` | ✅ SÍ |
| `products` | `products` | ❌ NO (se hace manual) |

---

### **5️⃣ getAndSetProducts() - Aquí está la clave que preguntas**

```java
private void getAndSetProducts(List<ProductsDTO> productsDto, OrderEntity orderEntity) {
    productsDto.forEach(product -> {
        // ① BUSCA el producto en el CATÁLOGO por nombre
        final var productFromCatalog =
            this.productCatalogRepository.findByName(product.getName()).orElseThrow();
        
        // ② CREA ProductEntity con cantidad + referencia al catálogo
        final var productEntity = ProductEntity
            .builder()
            .quantity(product.getQuantity())
            .catalog(productFromCatalog)  // ← Enlaza con ProductCatalogEntity
            .build();
        
        // ③ AGREGA a la orden (relación bidireccional)
        orderEntity.addProduct(productEntity);
        productEntity.setOrder(orderEntity);
    });
}
```

---

## 🔍 **Tu pregunta: ¿Por qué buscar por nombre?**

### **Respuesta: Porque el catálogo YA EXISTE en la BD**

```
┌─────────────────────────────────────────────────────────────────────┐
│  TABLA: products_catalog (YA EXISTE)                                │
├─────────────────────────────────────────────────────────────────────┤
│  id (UUID)          │ product_name   │ price   │ brand_name        │
├─────────────────────┼────────────────┼─────────┼───────────────────┤
│  abc-123-...        │ "Alexa large"  │ 150.00  │ Amazon            │
│  def-456-...        │ "Pc office"    │ 800.00  │ Dell              │
│  ghi-789-...        │ "TV 75"        │ 1200.00 │ Samsung           │
└─────────────────────────────────────────────────────────────────────┘
```

**El cliente NO sabe el UUID, solo el nombre del producto.**

```json
// El cliente envía:
{ "quantity": 2, "name": "Alexa large" }  // ← Solo nombre + cantidad

// NO envía:
{ "quantity": 2, "catalogId": "abc-123-..." }  // ← No conoce el UUID
```

---

## 📊 **Flujo visual completo**

```
┌─────────────────────────────────────────────────────────────────────┐
│  JSON de Postman                                                    │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │ clientName: "Shean Omelley"                                  │   │
│  │ bill: { idBill: "b-21", clientRfc: "ART655687JHZ" }         │   │
│  │ products: [                                                  │   │
│  │   { quantity: 2, name: "Alexa large" },                     │   │
│  │   { quantity: 1, name: "Pc office" },                       │   │
│  │   { quantity: 3, name: "TV 75" }                            │   │
│  │ ]                                                            │   │
│  └─────────────────────────────────────────────────────────────┘   │
└──────────────────────────────┬──────────────────────────────────────┘
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│  1. ModelMapper mapea clientName y bill automáticamente             │
└──────────────────────────────┬──────────────────────────────────────┘
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│  2. getAndSetProducts() procesa cada producto:                      │
│                                                                     │
│  Producto 1: { quantity: 2, name: "Alexa large" }                  │
│      │                                                              │
│      ▼                                                              │
│  productCatalogRepository.findByName("Alexa large")                │
│      │                                                              │
│      ▼                                                              │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │ ProductCatalogEntity (de la BD)                              │   │
│  │   id: abc-123-...                                            │   │
│  │   name: "Alexa large"                                        │   │
│  │   price: 150.00                                              │   │
│  │   brand: "Amazon"                                            │   │
│  └─────────────────────────────────────────────────────────────┘   │
│      │                                                              │
│      ▼                                                              │
│  ProductEntity.builder()                                            │
│      .quantity(2)                        ← Del DTO                  │
│      .catalog(productFromCatalog)        ← De la BD                 │
│      .build()                                                       │
│      │                                                              │
│      ▼                                                              │
│  orderEntity.addProduct(productEntity)   ← Relación bidireccional   │
└──────────────────────────────┬──────────────────────────────────────┘
                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│  3. orderRepository.save(orderEntity)                               │
│                                                                     │
│  Guarda en BD:                                                      │
│  - 1 OrderEntity                                                    │
│  - 1 BillEntity (CASCADE.ALL)                                       │
│  - 3 ProductEntity (CASCADE.ALL)                                    │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 **Respuesta directa a tu pregunta**

> *"¿Todo se basa solo en encontrar por el nombre?"*

**SÍ**, porque:

1. El **catálogo de productos ya existe** en la BD (con UUID, precio, marca, etc.)
2. El **cliente solo conoce el nombre** del producto
3. Se busca por nombre para **obtener la referencia completa** del catálogo
4. Se crea un `ProductEntity` que **enlaza** la cantidad pedida con el producto del catálogo

```java
// El ProductEntity final tiene:
ProductEntity {
    id: null,                    // ← JPA lo genera
    quantity: 2,                 // ← Del DTO
    catalog: ProductCatalogEntity { id: abc-123, name: "Alexa large", ... }, // ← De la BD
    order: OrderEntity { ... }   // ← Relación bidireccional
}
```

---

## 📋 **Tablas resultantes en BD**

```sql
-- ORDERS
| id | created_at          | client_name    | id_bill |
|----|---------------------|----------------|---------|
| 1  | 2025-01-15 10:30:00 | Shean Omelley  | 1       |

-- BILLS
| id | bill_id | client_rfc     |
|----|---------|----------------|
| 1  | b-21    | ART655687JHZ   |

-- PRODUCTS
| id | quantity | id_catalog   | id_order |
|----|----------|--------------|----------|
| 1  | 2        | abc-123-...  | 1        |  ← Alexa large
| 2  | 1        | def-456-...  | 1        |  ← Pc office
| 3  | 3        | ghi-789-...  | 1        |  ← TV 75
```

---
## ✅ **AQUI SE CREA AL FINAL UN PRODUCT ENTITY**
# ✅ **Sí, estás creando `ProductEntity` sistemáticamente**

---

## 🔍 **Flujo de creación**

```java
@Override
public String create(OrderDTO order) {
    final var toInsert = this.mapOrderFromDto(order); // ← Aquí se crean los ProductEntity
    return this.orderRepository.save(toInsert).getId().toString();
}
```

---

## 📊 **Cadena de llamadas**

```
create(OrderDTO)
    │
    ▼
mapOrderFromDto(OrderDTO)
    │
    ▼
getAndSetProducts(List<ProductsDTO>, OrderEntity)
    │
    ▼
ProductEntity.builder()...build()  ← AQUÍ SE CREA CADA ProductEntity
```

---

## 🎯 **Específicamente aquí:**

```java
private void getAndSetProducts(List<ProductsDTO> productsDto, OrderEntity orderEntity) {
    productsDto.forEach(product -> {
        // ...
        final var productEntity = ProductEntity  // ← CREAS ProductEntity
                .builder()
                .quantity(product.getQuantity())
                .catalog(productFromCatalog)
                .build();
        // ...
    });
}
```

---

## 📋 **Resumen**

| ¿Qué se crea? | ¿Dónde? | ¿Cuántos? |
|---------------|---------|-----------|
| `OrderEntity` | `mapOrderFromDto()` | 1 |
| `BillEntity` | ModelMapper (automático) | 1 |
| `ProductEntity` | `getAndSetProducts()` | N (uno por cada producto en el JSON) |

> 🎯 Con `CASCADE.ALL` en la relación, al guardar `OrderEntity`, JPA **persiste automáticamente** todos los `ProductEntity` asociados.

---

## ✅ **QUE TIENE QUE VER EL /21 EN EL CODIGO CUANDO ENVIO DESDE POSTMAN**
# 📚 **Explicación del `/21` en el header Location**

---

## ❌ **El `/21` NO viene del `idBill: "b-21"`**

Son cosas **completamente diferentes**.

---

## 🔍 **Flujo real**

```java
@PostMapping
public ResponseEntity<Void> create(@RequestBody OrderDTO orderDTO) {
    var path = "/" + this.ordersCrudService.create(orderDTO);  // ← Aquí
    return ResponseEntity.created(URI.create(path)).build();
}
```

### **¿Qué retorna `create()`?**

```java
@Override
public String create(OrderDTO order) {
    final var toInsert = this.mapOrderFromDto(order);
    return this.orderRepository.save(toInsert).getId().toString();  // ← Retorna el ID de OrderEntity
}
```

---

## 📊 **Paso a paso**

| Paso | Acción | Resultado |
|------|--------|-----------|
| 1 | `orderRepository.save(toInsert)` | JPA guarda y genera `id = 21` |
| 2 | `.getId()` | Obtiene `Long 21` |
| 3 | `.toString()` | Convierte a `"21"` |
| 4 | `"/" + "21"` | Genera `"/21"` |
| 5 | `ResponseEntity.created(URI.create("/21"))` | Header `Location: /21` |

---

## 🎯 **Diferencia entre los IDs**

| Campo | Valor | ¿Qué es? |
|-------|-------|----------|
| `idBill` | `"b-21"` | ID del **documento fiscal** (factura) |
| Header `/21` | `21` | ID de la **OrderEntity** (generado por JPA) |

```
┌─────────────────────────────────────────────────────────┐
│  OrderEntity                                            │
│    id: 21  ← ESTE es el que retorna (autogenerado)     │
│    clientName: "Shean Omelley"                          │
│    bill: BillEntity {                                   │
│        id: 1 (autogenerado)                             │
│        billId: "b-21"  ← ESTE viene del JSON           │
│        clientRfc: "ART655687JHZ"                        │
│    }                                                    │
└─────────────────────────────────────────────────────────┘
```

---

## 📋 **En la BD**

```sql
-- ORDERS
| id  | client_name    |
|-----|----------------|
| 21  | Shean Omelley  |  ← Este 21 va al header Location

-- BILLS  
| id | bill_id | client_rfc     |
|----|---------|----------------|
| 1  | b-21    | ART655687JHZ   |  ← Este "b-21" es el idBill del JSON
```

---

## 💡 **¿Por qué devolver `/21`?**

Es un patrón REST estándar. El header `Location` indica la URL del recurso recién creado:

```
POST /order → 201 Created
Header: Location: /21

// Ahora puedes consultar:
GET /order/21 → Obtiene la orden creada
```

---

# ✅ **¿POR QUE CREATEDAT SE DEBE PONER UN METODO ADICIONAL PARA DETERMINAR EL NULL?**
# 📚 **Explicación de `@PrePersist` y `createdAt`**

---

## 🔍 **¿Por qué `createdAt` es `null`?**

Cuando creas la orden, **nunca asignas valor a `createdAt`**:

```java
// En mapOrderFromDto()
final var orderResponse = new OrderEntity();  // ← createdAt = null
modelMapper.map(orderDTO, orderResponse);     // ← El DTO NO tiene createdAt
```

El JSON de Postman **no incluye** `createdAt`:

```json
{
    "clientName" : "Shean Omelley",
    "bill" : { ... },
    "products" : [ ... ]
    // ❌ No hay createdAt
}
```

---

## 🎯 **¿Qué es `@PrePersist`?**

Es un **callback de JPA** que se ejecuta **automáticamente ANTES** de insertar en la BD:

```
┌─────────────────────────────────────────────────────────────────────┐
│  orderRepository.save(orderEntity)                                  │
│       │                                                             │
│       ▼                                                             │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │ JPA detecta que es INSERT (id = null)                        │   │
│  └─────────────────────────────────────────────────────────────┘   │
│       │                                                             │
│       ▼                                                             │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │ @PrePersist → Ejecuta onCreate() ← AQUÍ SE ASIGNA LA FECHA  │   │
│  └─────────────────────────────────────────────────────────────┘   │
│       │                                                             │
│       ▼                                                             │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │ INSERT INTO orders (created_at, ...) VALUES ('2025-01-15')  │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

---

## ⚠️ **Tienes razón: Sin `@PrePersist` habría error**

```java
@Column(name = "created_at", nullable = false)  // ← NO puede ser NULL
private LocalDateTime createdAt;
```

**Sin el método `onCreate()`:**

```sql
INSERT INTO orders (created_at, client_name, ...) VALUES (NULL, 'Shean', ...)
-- ❌ ERROR: Column 'created_at' cannot be null
```

**Con el método `onCreate()`:**

```sql
INSERT INTO orders (created_at, client_name, ...) VALUES ('2025-01-15 10:30:00', 'Shean', ...)
-- ✅ OK
```

---

## 📊 **Ciclo de vida de JPA - Callbacks disponibles**

| Anotación | ¿Cuándo se ejecuta? |
|-----------|---------------------|
| `@PrePersist` | **Antes** de `INSERT` |
| `@PostPersist` | **Después** de `INSERT` |
| `@PreUpdate` | **Antes** de `UPDATE` |
| `@PostUpdate` | **Después** de `UPDATE` |
| `@PreRemove` | **Antes** de `DELETE` |
| `@PostRemove` | **Después** de `DELETE` |

---

## 💡 **Alternativa: Valor por defecto en la declaración**

```java
// Opción 1: @PrePersist (la que usas)
@PrePersist
protected void onCreate() {
    if (createdAt == null) {
        createdAt = LocalDateTime.now();
    }
}

// Opción 2: Valor directo en el campo
@Column(name = "created_at", nullable = false)
private LocalDateTime createdAt = LocalDateTime.now();

// Opción 3: Usar @CreationTimestamp de Hibernate
@CreationTimestamp
@Column(name = "created_at", nullable = false)
private LocalDateTime createdAt;
```

---

## 📋 **Resumen**

| Pregunta | Respuesta |
|----------|-----------|
| ¿Por qué `createdAt` es `null`? | Porque el DTO no lo tiene y no lo asignas manualmente |
| ¿Qué hace `@PrePersist`? | Ejecuta código **antes** de insertar en BD |
| ¿Sin `@PrePersist` hay error? | **SÍ**, porque `nullable = false` |
| ¿Cuándo se llama `onCreate()`? | Automáticamente por JPA antes del `INSERT` |

---
# OTRA CONSULTA SOBRE OTRO @JSONIGNORE ENCONTRADO EN BILLDTO?

# 📚 **Explicación de `@JsonIgnore` en BillDTO**

---

## 🎯 **Tu pregunta es muy válida**

Tienes razón en cuestionar esto. Analicemos:

---

## 🔍 **¿Cuándo hay referencia circular?**

```
OrderDTO                      BillDTO
┌─────────────┐              ┌─────────────┐
│ bill: ──────┼─────────────►│ order: ─────┼───┐
└─────────────┘              └─────────────┘   │
       ▲                                        │
       └────────────────────────────────────────┘
       
Jackson intenta serializar:
OrderDTO → bill → BillDTO → order → OrderDTO → bill → ... ∞
```

---

## ⚠️ **El problema SOLO ocurre si `OrderDTO` tiene referencia a `BillDTO`**

Revisa tu `OrderDTO`:

```java
public class OrderDTO {
    private String clientName;
    private BillDTO bill;        // ← Si esto existe
    private List<ProductsDTO> products;
}
```

**Si `OrderDTO.bill` existe Y `BillDTO.order` existe → Hay ciclo → Necesitas `@JsonIgnore`**

---

## 📊 **Escenarios**

| OrderDTO tiene bill | BillDTO tiene order | ¿Hay ciclo? | ¿Necesita @JsonIgnore? |
|---------------------|---------------------|-------------|------------------------|
| ✅ SÍ | ✅ SÍ | ✅ SÍ | ✅ SÍ |
| ✅ SÍ | ❌ NO | ❌ NO | ❌ NO |
| ❌ NO | ✅ SÍ | ❌ NO | ❌ NO |

---

## 🤔 **¿Realmente necesitas `order` en `BillDTO`?**

Probablemente **NO**. En DTOs generalmente no necesitas la referencia inversa:

```java
// Opción 1: Eliminar el campo (recomendado)
@Data
public class BillDTO {
    private String idBill;
    private String clientRfc;
    private BigDecimal amount;
    // Sin order - más limpio
}

// Opción 2: Mantenerlo con @JsonIgnore (como lo tienes)
@Data
public class BillDTO {
    private String idBill;
    private String clientRfc;
    private BigDecimal amount;
    @JsonIgnore
    private OrderDTO order;  // Se ignora al serializar/deserializar
}
```

---

## 💡 **¿Por qué existe entonces?**

Posiblemente se copió la estructura de la **Entity** donde SÍ es necesaria la relación bidireccional:

```java
// En Entity SÍ tiene sentido para JPA
@Entity
public class BillEntity {
    @OneToOne(mappedBy = "bill")
    private OrderEntity order;  // ← JPA necesita esto
}

// En DTO generalmente NO es necesario
public class BillDTO {
    private OrderDTO order;  // ← ¿Para qué?
}
```

---

## 📋 **Resumen**

| Pregunta | Respuesta |
|----------|-----------|
| ¿DTOs pueden tener ciclos? | **SÍ**, si tienen referencias cruzadas |
| ¿Por qué `@JsonIgnore`? | Evita que Jackson entre en loop infinito |
| ¿Es necesario `order` en `BillDTO`? | Probablemente **NO** |
| ¿Mejor solución? | Eliminar el campo `order` del DTO |

---
