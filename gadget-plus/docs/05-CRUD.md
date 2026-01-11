## <strong>🎯SECCION 5 CRUD</strong>

## #️ ⃣📚**Clase 77:MODELANDO CON DTOS**

> - Se crea OrderDTO

```java
public class OrderDTO {

    private Long id;
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")// nos brinda un formato JsonFormat
    private LocalDateTime orderDate;
    private String clientName;
    private BillDTO bill;
    private List<ProductsDTO> products;
    
}

```

```java
 @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")// nos brinda un formato JsonFormat
    private LocalDateTime orderDate;
```

```text
La anotación `@JsonFormat` se utiliza para definir cómo se serializa y deserializa el campo `orderDate` (de tipo `LocalDateTime`) al trabajar con JSON en Spring Boot (usando Jackson).

- `shape = JsonFormat.Shape.STRING`: Indica que el valor se representará como una cadena de texto en el JSON.
- `pattern = "yyyy-MM-dd HH:mm:ss"`: Especifica el formato de fecha y hora que se usará al convertir el objeto a JSON y viceversa.

🔹 **¿Para qué sirve?**  
Permite que al enviar o recibir datos en JSON, el campo `orderDate` tenga siempre el formato `"2024-06-10 15:30:00"`, facilitando la interoperabilidad y evitando errores de formato.
```

> - Se crea ProductsDTO
> - Se crea BillDTO

## 📝📝 IMPORTANTE Y POR QUE USAMOS UNA LISTA EN ORDER Y NO EN BILL

### 📝📝 RESPUESTA -> ✅

- ✅ En tu código, la diferencia en el uso de `List` vs objeto simple se debe a la relación entre las entidades:

**✅En `BillDTO`:**

```java
private OrderDTO order; // Una factura pertenece a UNA sola orden
```

**✅En `OrderDTO`:**

```java
private List<ProductsDTO> products; // Una orden puede tener MÚLTIPLES productos
```

### ✅✅Esto representa diferentes tipos de relaciones:

- **Bill → Order**: Relación **uno a uno** (1:1). Una factura está asociada a una única orden.
- **Order → Products**: Relación **uno a muchos** (1:N). Una orden puede contener múltiples productos.

Si cambiaras `OrderDTO order` a `List<OrderDTO>` significaría que una factura puede estar asociada a múltiples órdenes,
lo cual generalmente no es el caso en un modelo de negocio típico donde cada factura corresponde a una sola orden.

La estructura actual es correcta según las reglas de negocio habituales:

- Una orden puede tener múltiples productos
- Una factura pertenece a una sola orden

## 🎯🎯¿ DEBE HABER MISMOS ATRIBUTOS QUE HAY EN UNA ENTIDAD EN MI DTO POR EJEMPLO BILLDTO Y BILLENTITY?

> En tu caso, `OrderDTO order` no es una lista porque una factura (`BillDTO`) tiene una relación
> **uno a uno** con una orden. Una factura se genera para una sola orden específica.

> La diferencia con productos es que una orden puede contener **múltiples productos**, por eso necesitas
`List<ProductsDTO> products`.

### 🎯 **Relaciones en tu modelo:**

> - `Bill` → `Order`: 1:1 (una factura para una orden)
> - `Order` → `Products`: 1:N (una orden puede tener varios productos)

### Respecto a tu segunda pregunta sobre los atributos del DTO vs Entity:

**No, no necesitas los mismos atributos.** Los DTOs son para transferencia de datos y deben contener solo lo que
necesites exponer:

```java
// Ejemplo de diferencias comunes
public class BillEntity {
    private Long id;
    private String clientRfc;
    private BigDecimal amount;
    private LocalDateTime createdAt;    // No necesario en DTO
    private LocalDateTime updatedAt;    // No necesario en DTO
    private String internalNotes;       // Campo interno, no exponer
    private OrderEntity order;
}

public class BillDTO {
    private String id;                 
    private String clientRfc;
    private BigDecimal amount;
    private OrderDTO order;            // Sin campos internos de la orden
}
```

🎯🎯**Principios para DTOs:**

- Solo incluye datos que el cliente necesite
- Omite campos sensibles o internos
- Puede tener diferentes tipos de datos (Long → String)
- Evita dependencias circulares con `@JsonIgnore`
- Incluir menos campos (omitir campos internos)
- Cambiar nombres (rfc → clientRfc para mayor claridad)
- Transformar tipos (aunque en tu caso el String id ya está bien)
- Aplanar estructuras o anidar datos según la necesidad del frontend

### ¿Podria no incluir OrderDTO order en mi BILLDTO?

--Sí, **podrías no incluir `private OrderDTO order;`** en tu `BillDTO` si no necesitas esa información en el frontend o
en las respuestas de tu API.

**Depende de tu caso de uso:**

**Opción 1: Sin OrderDTO (más simple)**

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillDTO {
    private String id;
    private String clientRfc;
    private BigDecimal amount;
    // Sin OrderDTO - solo datos básicos de la factura
}
```

**Opción 2: Con OrderDTO (más información)**

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillDTO {
    private String id;
    private String clientRfc;
    private BigDecimal amount;
    @JsonIgnore
    private OrderDTO order;  // Incluyes la orden relacionada
}
```

**¿Cuándo incluir OrderDTO?**

- Si necesitas mostrar **detalles de la orden** junto con la factura
- Si el frontend requiere **navegar** entre factura y orden
- Si necesitas **validaciones** que requieren datos de la orden

**¿Cuándo NO incluirlo?**

- Si solo necesitas **datos básicos** de la factura
- Para **simplificar** las respuestas de la API
- Para **evitar problemas** de serialización/deserialización

En muchos casos, especialmente para listados de facturas, es común **omitir
** relaciones complejas y solo incluir lo esencial.
---

### 📝 **Resumen**

- Los DTO **varían** según lo que necesitas transferir.
- Te devuelven **solo los datos necesarios** para cada caso de uso.

---

🔗 **Ideal para APIs REST y separación de capas.**

---

### Resumen que pasa con los atributos de DTO ¿Se usan todos?:

> Se puede usar solo algunos datos cuando es necesario no todos los de DTO


---

## ⃣📚**Clase 78:CREANDO SERVICIOS*

### ⚠️⚠️PASOS A SEGUIR⚠️⚠️ :

### 🔧Se crea en services OrdersCrudService ->

```java
public interface OrdersCrudService {

    String create(OrderDTO order);//retornar el ID de la orden creada como String
    OrderDTO read(Long id);
    OrderDTO update(OrderDTO order, Long id);
    void delete(Long id);
}

```

### 🔧Se crea la implementacion-> OrdersCrudServiceImpl

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersCrudServiceImpl implements OrdersCrudService {

    private final OrderRepository orderRepository;
    .
    ..
    ...
}
```

---

### 📚 Clase 79: MODEL MAPPER VS OBJECT MAPPER

#### 🔒 ¿Por qué no enviamos entidades al front end y preferimos usar DTO?

> OrderEntity es un mapeo a la tabla de datos es una buena practica que no enviemos estas entidades al front end
> por eso creamos los DTOs (Data Transfer Object) que son objetos que solo tienen los atributos que queremos enviar al
> front end
> y no toda la informacion de la entidad.
> Para mapear de una entidad a un DTO y viceversa podemos hacerlo manualmente o usar librerias que nos facilitan esta
> tarea
> como ModelMapper y ObjectMapper.
>
> Ademas usar DTOS evita que usemos muchos getters y setters en las entidades y tambien nos permite tener un mejor
> control sobre
> que datos enviamos al front end, asi no caemos en un antipatron llamado Anemic Domain Model.

#### 🔗 Para esto usamos estas librerias: ->

![build](https://img.shields.io/badge/build-passing-brightgreen)

#### ✅visitar -> https://modelmapper.org/ es mas usado en spring boot

#### ✅visitar -> esto es una libreria de jackson : https://mapstruct.org/documentation/installation/

#### 🤖 **¿Qué es ModelMapper?**

> ModelMapper es una librería de Java que facilita la conversión automática entre objetos de diferentes clases, por
> ejemplo, de entidades a DTOs y viceversa. Es útil cuando los objetos tienen estructuras similares.

- 🔄 **Ventajas:** Reduce el código manual de mapeo, es fácil de configurar y usar.
- ⚡ **Uso típico:** `modelMapper.map(source, Destination.class);`

---

#### 🧩 **¿Qué es ObjectMapper?**

ObjectMapper es una clase de la librería Jackson que permite convertir objetos Java a JSON y viceversa. Es fundamental
para la serialización y deserialización en aplicaciones Spring Boot.

- 📦 **Ventajas:** Permite personalizar el formato de salida, soporta anotaciones como `@JsonFormat`.
- ⚡ **Uso típico:**
    - Serializar: `objectMapper.writeValueAsString(obj);`
    - Deserializar: `objectMapper.readValue(json, Clase.class);`

---

#### 🆚 **Diferencias clave**

- ModelMapper se usa para transformar objetos entre clases Java.
- ObjectMapper se usa para transformar objetos Java a JSON y viceversa.

---

#### 💡 **¿Cuándo usar cada uno?**

- Usa **ModelMapper** para convertir entre entidades y DTOs.
- Usa **ObjectMapper** para trabajar con JSON en APIs REST.

---

#### 📌 **Ejemplo visual**

```java
// ModelMapper
ModelMapper modelMapper = new ModelMapper();
OrderDTO dto = modelMapper.map(orderEntity, OrderDTO.class);

// ObjectMapper
ObjectMapper objectMapper = new ObjectMapper();
String json = objectMapper.writeValueAsString(orderDTO);
OrderDTO dto = objectMapper.readValue(json, OrderDTO.class);
```

---

#### 📝 **Resumen**

| Herramienta     | Propósito                | Uso principal        |
|-----------------|--------------------------|----------------------|
| 🤖 ModelMapper  | Mapear entre clases Java | Entidad ↔ DTO        |
| 🧩 ObjectMapper | JSON ↔ Objeto Java       | API REST, serializar |

---

### 📚 Clase 80: MODEL MAPPER READ PARTEI

#### ➡️ Agregamos al pomxml

```xml
        <dependency>
            <groupId>org.modelmapper</groupId>
            <artifactId>modelmapper</artifactId>
            <version>3.2.0</version>
        </dependency>
```

#### ➡️ Agregamos en OrdersCrudServiceImpl el siguiente metodo:

```java

private OrderDTO mapOrderFromEntity(OrderEntity order) {
    final var mapper = new ModelMapper();
    return mapper.map(order, OrderDTO.class);
}
```

#### 📍 **IMPORTANTE**

- Mediante java Reflexion agarra las propiedades de un objeto y las mapea a otro objeto similar
- sin el nombre coincide por ejemplo si tenemos un objeto OrderEntity y otro OrderDTO y ambos tienen una propiedad
  llamada createdAt si son iguales las va a mapear automaticamente.

### 🧩 ¿Para qué sirve este método?

El método `mapOrderFromEntity` convierte un objeto de tipo `OrderEntity` (entidad de base de datos) a un objeto
`OrderDTO` (Data Transfer Object).

- **¿Por qué es útil?**  
  Permite separar la lógica de la base de datos de la lógica de transferencia de datos, facilitando el envío de
  información al frontend o a otras capas de la aplicación.

- **¿Cómo lo hace?**  
  Utiliza la librería `ModelMapper` para copiar automáticamente los datos de la entidad al DTO, evitando hacerlo
  manualmente.

🔹 **Resumen:**  
Este método ayuda a transformar datos entre capas de la aplicación de forma sencilla y automática.

#### ➡️ Implementando el metodo read 
```java
    @Override
    public OrderDTO read(Long id) {
        return this.mapOrderFromEntity(this.orderRepository.findById(id).orElseThrow());//aqui le paso como argumento el orderEntity
    }

```
#### ➡️ Pide que lo castees con mapOrderFromEntity por que en si el metodo findById retorna un Optional<OrderEntity>

```java

@NoRepositoryBean
public interface CrudRepository<T, ID> extends Repository<T, ID> {
    Optional<T> findById(ID id);
```
#### ➡️➡Este metodo esta en OrdersCrudServiceImpl

```java

private OrderDTO mapOrderFromEntity(OrderEntity order) {
    final var mapper = new ModelMapper();
    return mapper.map(order, OrderDTO.class);
}
```
#### ➡️ OrderController -> 
```java
@RestController
@RequestMapping(path = "order")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersCrudService ordersCrudService;
    
    @GetMapping(path = "{id}")
    public ResponseEntity<OrderDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(ordersCrudService.read(id));
    }
}
```
---

### 📚 Clase 81: MODEL MAPPER READ PARTEII

> #### - Si no pongo final en el atributo orderRepository no me va a funcionar la inyeccion de dependencias con @RequiredArgsConstructor

### 🛡️ ¿Por qué usar `final` aquí?

- `final` indica que la variable `orderRepository` **no puede cambiar** después de ser inicializada.
- Esto ayuda a mantener la **inmutabilidad** y hace el código más seguro y fácil de entender.

### 🏗️ ¿Qué hace `@RequiredArgsConstructor`?

- `@RequiredArgsConstructor` (de Lombok) genera automáticamente un **constructor** que recibe como parámetros todos los
  campos `final` (y los que son `@NonNull`).
- Así, Spring puede **inyectar** automáticamente la dependencia `OrderRepository` al crear el servicio.

---

```java
@RequiredArgsConstructor
public class OrdersCrudServiceImpl implements OrdersCrudService {
    private final OrderRepository orderRepository;
    // ...
}
```

---

🔹 **Resumen:**  
Si no usas `final`, `@RequiredArgsConstructor` no incluirá ese campo en el constructor, y Spring no podrá inyectar la
dependencia, causando errores al iniciar la aplicación.

#### ➡️ ABRIMOS EN POSTMAN : pero en postman en name no sale el nombre del producto vamos a solucionarlo.

![img](images/64.png)

- Aqui viene nulo el nombre por que eran distintos y lo solucionamos igualando
- el nombre del atributo en BillEnrity y BillDTO ""clientRfc"
- asi funciona el ObjectMapper por java reflexion.

![img](images/66.png)

- Ahora si sale el nombre del producto en postman.

![img](images/67.png)

### 📚 Clase 83: ModelMapper map custom  ↩️ ↩️
#### ➡️ Indicaciones:

- Vamos hacer un mapeo personalizado para mapear lo que recibimos de la base de datos y la vamos a mapear
- a un objeto DTO sin embargo esto funciona solo para el GET✅

#### ➡️ En OrdersCrudServiceImpl agregamos el siguiente metodo:

- Este es el metodo original:
```java
  //CLASE 80 READ PARTE1 CREAMOS EL METODO PARA MAPEAR DE ENTITY A DTO
    private OrderDTO mapOrderFromEntity(OrderEntity orderEntity) {
        final var mapper = new ModelMapper();
        return mapper.map(orderEntity, OrderDTO.class);
    }
```
- ➡️ Lo modificamos a:

```java
       private OrderDTO mapOrderFromEntity(OrderEntity orderEntity) {
        final var modelMapper = new ModelMapper();

        modelMapper
                .typeMap(ProductEntity.class, ProductsDTO.class)
                .addMappings(mapper -> mapper.map(
                        entity -> entity.getCatalog().getName(), ProductsDTO::setName
                ));

        return modelMapper.map(orderEntity, OrderDTO.class);
    }
```
- 🚀 Aqui no lo va  hacer por reflexion sino que le estamos diciendo que tome el
- nombre del catalogo

## Respuesta a tus preguntas

### Pregunta 1: ¿Es permitida la coma en la expresión lambda?

**Sí, es completamente permitido.** La coma separa los dos parámetros del método `map()`:

```java
mapper.map(
    entity -> entity.getCatalog().getName(),  // Primer parámetro (getter)
    ProductsDTO::setName                      // Segundo parámetro (setter)
)
```

### Pregunta 2: Explicación detallada del método

## 🔄 Método `mapOrderFromEntity`

### 📝 Descripción
Este método convierte una entidad `OrderEntity` en un DTO `OrderDTO` utilizando **ModelMapper** con mapeo personalizado.

### ⚙️ Funcionamiento paso a paso

#### 1️⃣ **Creación del ModelMapper**
```java
final var modelMapper = new ModelMapper();
```
➡️ Crea una nueva instancia de ModelMapper para realizar las conversiones.

#### 2️⃣ **Configuración de mapeo personalizado**
```java
modelMapper
    .typeMap(ProductEntity.class, ProductsDTO.class)
    .addMappings(mapper -> mapper.map(
        entity -> entity.getCatalog().getName(), 
        ProductsDTO::setName
    ));
```

🔍 **¿Qué hace esta configuración?**
- **`.typeMap(ProductEntity.class, ProductsDTO.class)`**: Define una regla específica para mapear de `ProductEntity` a `ProductsDTO`
- **`.addMappings()`**: Añade mapeos personalizados
- **`entity -> entity.getCatalog().getName()`**: 📥 **Getter** - Extrae el nombre del catálogo desde la entidad
- **`ProductsDTO::setName`**: 📤 **Setter** - Asigna ese valor al campo `name` del DTO

#### 3️⃣ **Conversión final**
```java
return modelMapper.map(orderEntity, OrderDTO.class);
```
🎯 Convierte la `OrderEntity` completa a `OrderDTO` aplicando todas las reglas configuradas.

### 🚀 **Flujo de datos**
```
OrderEntity → ProductEntity → ProductCatalogEntity.name → ProductsDTO.name
```

### 💡 **¿Por qué se necesita este mapeo personalizado?**
Porque el nombre del producto está anidado en `ProductEntity.catalog.name`, pero en el DTO se quiere directamente como `ProductsDTO.name`.

---
## 🤔 Explicación de la estructura del método `map()`

La confusión es comprensible. **No es un stream con lambda**, es el método `map()` de ModelMapper que requiere **exactamente 2 parámetros**.

### 📋 **Estructura del método `map()`**

```java
mapper.map(
    // Parámetro 1: Function<S, ?> getter
    entity -> entity.getCatalog().getName(),
    // Parámetro 2: Setter del destino  
    ProductsDTO::setName
)
```

### ❌ **Por qué tu propuesta no funciona**

```java
// ❌ INCORRECTO - No es válido
entity -> entity.getCatalog().getName()
entity -> entity.ProductsDTO::setName
```

**Razones:**
- Son **dos expresiones separadas**, no parámetros de un método
- `ProductsDTO::setName` necesita ir **sin lambda** (es method reference)
- Falta la **coma** que separa los parámetros del método

### ✅ **Estructura correcta**

```java
// ✅ CORRECTO
mapper.map(
    parametro1,  // ← getter (lambda)
    parametro2   // ← setter (method reference)
)
```

### 🔍 **Comparación visual**

| Elemento | Tipo | Función |
|----------|------|---------|
| `entity -> entity.getCatalog().getName()` | Lambda expression | 📥 Extrae el valor |
| `,` | Separador | Divide parámetros |
| `ProductsDTO::setName` | Method reference | 📤 Asigna el valor |

### 💡 **La coma es obligatoria**
La coma **debe estar ahí** porque separa dos parámetros diferentes del método `map()`. 
Sin ella, Java no sabría dónde termina el primer parámetro y dónde empieza el segundo.

![img](images/68.png)

---

### 📚 Clase 84: MAPEO DE ENTIDADES PARTE I

#### ➡️ Indicaciones:
> En el mapOrderFromEntity se hacer un mapeo personalizado para mapear lo que recibimos de la base de datos
> y la vamos a mapear a un objeto DTO sin embargo esto funciona solo para el GET✅
> ¿Pero que pasa con los UPDATE✅ o INSERT✅ ? en este caso es el proceso inverso yo voy a recibir del frontend
> un OrderDTO y lo tengo que mapear a un Entidad entonces hago el proceso inverso.

- #### Osea esto es el DTO

![img](images/69.png)

- Entonces tenemos que transformar esto en una lista de productEntity
- para hacer esto vamos a usar el findByName actualizamos el nombre o lo agregamos .

#### ➡️

```java
 private void getAndSetProducts(List<ProductsDTO> productsDto, OrderEntity orderEntity) {

        productsDto.forEach(product -> {
            final var productFromCatalog =
                    this.productCatalogRepository.findByName(product.getName()).orElseThrow();

            final var productEntity = ProductEntity
                    .builder()
                    .quantity(product.getQuantity())
                    .catalog(productFromCatalog)
                    .build();
            orderEntity.addProduct(productEntity);
            productEntity.setOrder(orderEntity);
        });
    }
```

## 📚 Explicación detallada del método `getAndSetProducts`

### 📋 **¿Por qué recibe esos dos parámetros?**

```java
private void getAndSetProducts(List<ProductsDTO> productsDto, OrderEntity orderEntity)
```

| Parámetro | Propósito |
|-----------|-----------|
| `List<ProductsDTO> productsDto` | 📥 **Entrada del frontend** \- Lista de productos que el usuario quiere agregar |
| `OrderEntity orderEntity` | 🎯 **Destino** \- La orden donde se guardarán los productos |

---

### 🔄 **¿Por qué usa `forEach`?**

```java
productsDto.forEach(product -> { ... });
```

Porque necesita **procesar cada producto** de la lista individualmente:
- Recibe 3 productos → itera 3 veces
- Cada iteración crea un `ProductEntity` y lo asocia a la orden

---

### 🔍 **¿Por qué busca por nombre?**

```java
final var productFromCatalog =
    this.productCatalogRepository.findByName(product.getName()).orElseThrow();
```

**Razón:** El frontend envía solo el **nombre** del producto:

```json
{ "quantity": 2, "name": "Macbook pro" }
```

Pero necesitas el **catálogo completo** de la base de datos (con id, precio, etc.) para asociarlo al `ProductEntity`.

---

### 🏗️ **¿Qué hace el patrón Builder?**

```java
final var productEntity = ProductEntity
    .builder()
    .quantity(product.getQuantity())
    .catalog(productFromCatalog)
    .build();
```

El patrón **Builder** permite construir objetos de forma legible:

| Método | Acción |
|--------|--------|
| `.builder()` | Inicia la construcción |
| `.quantity(...)` | Asigna la cantidad |
| `.catalog(...)` | Asigna el catálogo encontrado |
| `.build()` | Crea el objeto final |

---

### 🔁 **Flujo completo: DTO → Entity**

```
📥 Frontend envía:
{
    "products": [
        { "quantity": 2, "name": "Macbook pro" }
    ]
}

⬇️ El método transforma:

1️⃣ forEach → Toma cada ProductsDTO
2️⃣ findByName → Busca "Macbook pro" en ProductCatalogRepository
3️⃣ Builder → Crea ProductEntity con cantidad + catálogo
4️⃣ addProduct → Agrega a la orden
5️⃣ setOrder → Establece relación bidireccional

📤 Resultado: OrderEntity con List<ProductEntity> listos para guardar
```

---

### 💡 **Tu profesor tiene razón**

| Operación | Dirección | Método |
|-----------|-----------|--------|
| **GET** (leer) | Entity → DTO | `mapOrderFromEntity` |
| **POST/PUT** (crear/actualizar) | DTO → Entity | `getAndSetProducts` |

El método `getAndSetProducts` hace el **proceso inverso**: convierte lo que viene del 
frontend (DTO) en entidades que JPA puede guardar en la base de datos.

#### ⚠️⚠️⚠️RECORDAR EL METODO ADDPRODUCT DE ORDERENTITY⚠️⚠️⚠️

## 🔍 ¿Qué pasaría sin `product.setOrder(this)` en `addProduct`?

**Sí, habría fallas** si no existiera esa línea en el método `addProduct`.

### ⚠️ **Escenario hipotético: `addProduct` sin `setOrder`**

```java
// ❌ Método incompleto (hipotético)
public void addProduct(ProductEntity product) {
    products.add(product);
    // Falta: product.setOrder(this);
}
```

### 💥 **Problemas que ocurrirían**

| Problema | Descripción |
|----------|-------------|
| 🔴 **Error de persistencia** | JPA no sabría qué `order_id` guardar en la tabla `products` |
| 🔴 **Foreign Key NULL** | La columna `order_id` quedaría vacía o lanzaría excepción |
| 🔴 **Relación unidireccional rota** | Solo `Order → Products` funcionaría, pero no `Product → Order` |

### 📊 **Ejemplo visual**

```
SIN setOrder():
┌─────────────────┐         ┌─────────────────┐
│   OrderEntity   │────────→│  ProductEntity  │
│   id: 1         │         │  order_id: NULL │ ← ❌ PROBLEMA
└─────────────────┘         └─────────────────┘

CON setOrder():
┌─────────────────┐         ┌─────────────────┐
│   OrderEntity   │←───────→│  ProductEntity  │
│   id: 1         │         │  order_id: 1    │ ← ✅ CORRECTO
└─────────────────┘         └─────────────────┘
```

### 🗄️ **Error típico en base de datos**

```sql
-- Sin setOrder(), JPA intentaría insertar:
INSERT INTO products (quantity, catalog_id, order_id) 
VALUES (2, 5, NULL);  -- ❌ order_id es NULL

-- Con setOrder(), JPA inserta correctamente:
INSERT INTO products (quantity, catalog_id, order_id) 
VALUES (2, 5, 1);  -- ✅ order_id tiene valor
```

### 💡 **Conclusión**

El método `addProduct` **debe tener** `product.setOrder(this)` porque:

1. **JPA necesita ambos lados** de la relación bidireccional para persistir correctamente
2. El `@ManyToOne` en `ProductEntity` define la **foreign key** (`order_id`)
3. Sin esa línea, la columna `order_id` no tendría valor

**Por eso tu código actual es correcto** al tener `setOrder` dentro de `addProduct`. 
Lo que es **redundante** es llamarlo **otra vez** después de `addProduct()`.

---
## 🔍 ¿Pasa algo si lo mantienes productEntity.setOrder(orderEntity);?
- en el metodo getAndSetProducts de OrdersCrudServiceImpl
**No, no pasa nada malo.** El código funcionará correctamente.

### ✅ **Lo que sucede**

```java
orderEntity.addProduct(productEntity);      // 1️⃣ Setea la relación
productEntity.setOrder(orderEntity);        // 2️⃣ La setea OTRA VEZ (mismo valor)
```

| Aspecto | Resultado |
|---------|-----------|
| **¿Funciona?** | ✅ Sí |
| **¿Errores?** | ❌ No |
| **¿Problema de rendimiento?** | ❌ No (es solo una asignación) |
| **¿Es código limpio?** | ⚠️ No ideal (redundante) |

### 💡 **Conclusión**

Es solo **código innecesario**, pero no causa problemas. Lo recomendable es eliminarlo para mantener el código limpio, pero si lo dejas, todo seguirá funcionando igual.

### 📚 Clase 85: MODEL MAPPER READ PARTEII

#### ➡️ Indicaciones:
## 📚 Explicación detallada del flujo POST completo

### 1️⃣ **Controller: `@PostMapping create()`**

```java
@PostMapping
public ResponseEntity<Void> create(@RequestBody OrderDTO orderDTO) {
    var path = "/" + this.ordersCrudService.create(orderDTO);
    return ResponseEntity.created(URI.create(path)).build();
}
```

| Elemento | Explicación |
|----------|-------------|
| `@PostMapping` | Indica que este método responde a peticiones HTTP POST |
| `@RequestBody OrderDTO orderDTO` | Deserializa el JSON del body en un objeto `OrderDTO` |
| `ResponseEntity<Void>` | Respuesta HTTP sin cuerpo (solo headers y status) |
| `ordersCrudService.create(orderDTO)` | Llama al servicio y retorna el ID creado |

#### 🔍 **¿Por qué `"/" + ...`?**

```java
var path = "/" + this.ordersCrudService.create(orderDTO);
// Si create() retorna "5", entonces path = "/5"
```

El `/` es necesario porque:
- **URI requiere una ruta válida** que empiece con `/`
- Es una **convención REST** para indicar la ubicación del recurso creado
- Ejemplo: si el ID es `5`, la ubicación sería `/5` (relativa a `/order`)

#### 🔍 **¿Qué es `URI.create(path)`?**

```java
URI.create(path)  // Crea un objeto URI a partir del String "/5"
```

- **URI** = Uniform Resource Identifier
- Es una clase de Java (`java.net.URI`) que representa una dirección de recurso
- `URI.create()` es un factory method que parsea el String a URI

#### 🔍 **¿Qué hace `ResponseEntity.created(uri).build()`?**

```java
ResponseEntity.created(URI.create(path)).build();
```

| Método | Acción |
|--------|--------|
| `.created(uri)` | Establece status **201 CREATED** + header `Location: /5` |
| `.build()` | Construye la respuesta sin body |

**Respuesta HTTP resultante:**
```
HTTP/1.1 201 Created
Location: /5
Content-Length: 0
```

---

### 2️⃣ **Service: `create()`**

```java
@Override
public String create(OrderDTO order) {
    final var toInsert = this.mapOrderFromDto(order); // 1️⃣ DTO → Entity
    return this.orderRepository.save(toInsert).getId().toString(); // 2️⃣ Guardar y retornar ID
}
```

| Paso | Acción |
|------|--------|
| `mapOrderFromDto(order)` | Convierte el DTO a Entity para JPA |
| `orderRepository.save(toInsert)` | Guarda en BD y retorna la entidad con ID generado |
| `.getId().toString()` | Extrae el ID y lo convierte a String |

---

### 3️⃣ **Método: `mapOrderFromDto()`**

```java
private OrderEntity mapOrderFromDto(OrderDTO orderDTO) {

    final var orderResponse = new OrderEntity();  // 1️⃣
    final var modelMapper = new ModelMapper();    // 2️⃣

    log.info("Before{}", orderResponse);          // 3️⃣
    modelMapper.map(orderDTO, orderResponse);     // 4️⃣
    log.info("After{}", orderResponse);           // 5️⃣

    this.getAndSetProducts(orderDTO.getProducts(), orderResponse);  // 6️⃣
    log.info("After with products{}", orderResponse);               // 7️⃣

    return orderResponse;  // 8️⃣
}
```

| Paso | Código | Explicación |
|------|--------|-------------|
| 1️⃣ | `new OrderEntity()` | Crea entidad vacía como destino |
| 2️⃣ | `new ModelMapper()` | Instancia el mapeador automático |
| 3️⃣ | `log.info("Before...")` | Log muestra entidad vacía `OrderEntity(id=null, ...)` |
| 4️⃣ | `modelMapper.map(orderDTO, orderResponse)` | Copia campos coincidentes (client, date, etc.) |
| 5️⃣ | `log.info("After...")` | Log muestra entidad con datos básicos |
| 6️⃣ | `getAndSetProducts(...)` | Mapea y asocia los productos (ya lo explicamos antes) |
| 7️⃣ | `log.info("After with products...")` | Log muestra entidad completa con productos |
| 8️⃣ | `return orderResponse` | Retorna la entidad lista para guardar |

---

### 📊 **Flujo visual completo**

```
📱 Frontend (POST /order)
    │
    ▼ JSON Body
┌────────────────────────────────────┐
│ {                                  │
│   "client": "John",                │
│   "products": [                    │
│     {"name": "Macbook", "qty": 2}  │
│   ]                                │
│ }                                  │
└────────────────────────────────────┘
    │
    ▼ @RequestBody
┌────────────────────────────────────┐
│         OrderController            │
│   create(OrderDTO orderDTO)        │
└────────────────────────────────────┘
    │
    ▼ ordersCrudService.create()
┌────────────────────────────────────┐
│     OrdersCrudServiceImpl          │
│   1. mapOrderFromDto() ──────────┐ │
│      ├─ new OrderEntity()        │ │
│      ├─ modelMapper.map()        │ │
│      └─ getAndSetProducts()      │ │
│   2. orderRepository.save()      │ │
│   3. return ID = "5"             │ │
└────────────────────────────────────┘
    │
    ▼ path = "/5"
┌────────────────────────────────────┐
│ ResponseEntity.created(URI)        │
│ Status: 201                        │
│ Header: Location: /5               │
└────────────────────────────────────┘
    │
    ▼
📱 Frontend recibe: 201 Created
```

## 📚 Clase 86: create  🚀 🚀 🚀 🚀 🚀 🚀 🚀 🚀

### ➡️ Indicaciones:  🚀 🚀 🚀 🚀 🚀
#### RESUMEN :

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

## 📚 Clase 87: CALCULANDO SUMATORIA DE LA FACTURA  🚀✅✅

### ➡️ INFORMACION :

# COMPLEMENTO DE LA PREGUNTA JSONIGNORE EN BILLDTO
# 📚 **Análisis de tus clases - Referencias Circulares**

---

## ✅ **Confirmación: SÍ hay referencia circular en los DTOs**

Revisando tus archivos:

```java
// OrderDTO.java
public class OrderDTO {
    private BillDTO bill;  // ← OrderDTO tiene BillDTO
}

// BillDTO.java
public class BillDTO {
    @JsonIgnore
    private OrderDTO order;  // ← BillDTO tiene OrderDTO
}
```

**Ciclo confirmado:**

```
OrderDTO.bill → BillDTO.order → OrderDTO.bill → BillDTO.order → ... ∞
```

---

## 🔍 **¿Cuándo ocurre el problema?**

Cuando Jackson intenta **serializar** (convertir a JSON) un `OrderDTO`:

```java
// En un @GetMapping por ejemplo
return ResponseEntity.ok(orderDTO);  // ← Jackson serializa a JSON
```

Sin `@JsonIgnore`:
```
Jackson: OrderDTO → bill → BillDTO → order → OrderDTO → bill → ... 
❌ StackOverflowError
```

Con `@JsonIgnore`:
```
Jackson: OrderDTO → bill → BillDTO → (order ignorado) → FIN
✅ OK
```

---

## 🤔 **Pregunta importante: ¿Realmente necesitas `order` en `BillDTO`?**

Analizando tu código, probablemente **NO**:

| Caso de uso | ¿Necesitas `BillDTO.order`? |
|-------------|----------------------------|
| Crear orden (POST) | ❌ NO - El JSON no lo envía |
| Leer orden (GET) | ❌ NO - Ya tienes la orden, el bill viene dentro |
| Actualizar bill | ❌ NO - Buscas por ID de orden, no al revés |

---

## 💡 **Recomendación: Eliminar el campo**

```java
// BillDTO.java - Versión limpia
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillDTO {
    private String idBill;
    private String clientRfc;
    private BigDecimal amount;
    // ❌ Eliminar: private OrderDTO order;
}
```

---

## 📊 **Comparación: Entity vs DTO**

| Clase | ¿Necesita relación bidireccional? | ¿Por qué? |
|-------|-----------------------------------|-----------|
| `BillEntity` | ✅ SÍ | JPA necesita `mappedBy` para la relación |
| `BillDTO` | ❌ NO | Solo transporta datos, no gestiona relaciones |

```java
// BillEntity.java - CORRECTO tener la relación
@OneToOne(mappedBy = "bill", cascade = CascadeType.ALL)
private OrderEntity order;  // ← JPA lo necesita

// BillDTO.java - NO es necesario
@JsonIgnore
private OrderDTO order;  // ← ¿Para qué lo usas?
```

---

## 📋 **Resumen final**

| Pregunta | Respuesta |
|----------|-----------|
| ¿Hay ciclo en tus DTOs? | ✅ SÍ |
| ¿`@JsonIgnore` lo soluciona? | ✅ SÍ |
| ¿Es la mejor solución? | ⚠️ Funciona, pero mejor eliminar el campo |
| ¿Por qué existe ese campo? | Probablemente se copió del Entity sin necesidad |

---

# OTRA CONSULTA SOBRE EL TOTAL
# 📚 **Explicación detallada de `AtomicReference` y `updateAndGet`**

---

## 🤔 **¿Por qué necesitas `AtomicReference`?**

El problema es que **no puedes modificar una variable local dentro de un lambda**:

```java
// ❌ ESTO NO COMPILA
BigDecimal total = BigDecimal.ZERO;
productsDto.forEach(product -> {
    total = total.add(price);  // Error: variable must be final or effectively final
});
```

---

## 🎯 **¿Qué es `AtomicReference`?**

Es un **contenedor** que envuelve un objeto y permite modificar su contenido:

```java
┌─────────────────────────────────────┐
│  AtomicReference<BigDecimal>        │
│  ┌─────────────────────────────┐    │
│  │  valor interno: BigDecimal  │ ←─── Puedes cambiar ESTO
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
      ↑
      │
La referencia al contenedor es FINAL (no cambia)
Pero el contenido SÍ puede cambiar
```

---

## 🔍 **Flujo paso a paso con tu código**

Supongamos estos productos:

| Producto | Precio |
|----------|--------|
| Alexa large | $100 |
| Pc office | $500 |
| TV 75 | $200 |

```
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 1: Inicialización                                                   │
│                                                                          │
│   var total = new AtomicReference<>(BigDecimal.ZERO);                   │
│                                                                          │
│   ┌─────────────────────┐                                               │
│   │ AtomicReference     │                                               │
│   │   valor: 0.00       │                                               │
│   └─────────────────────┘                                               │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 2: Primer producto (Alexa large - $100)                            │
│                                                                          │
│   total.updateAndGet(bigDecimal -> bigDecimal.add(100))                 │
│                                                                          │
│   1. Obtiene valor actual: 0.00                                         │
│   2. Ejecuta lambda: 0.00 + 100 = 100.00                                │
│   3. Guarda nuevo valor: 100.00                                         │
│   4. Retorna: 100.00                                                    │
│                                                                          │
│   ┌─────────────────────┐                                               │
│   │ AtomicReference     │                                               │
│   │   valor: 100.00     │                                               │
│   └─────────────────────┘                                               │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 3: Segundo producto (Pc office - $500)                             │
│                                                                          │
│   total.updateAndGet(bigDecimal -> bigDecimal.add(500))                 │
│                                                                          │
│   1. Obtiene valor actual: 100.00                                       │
│   2. Ejecuta lambda: 100.00 + 500 = 600.00                              │
│   3. Guarda nuevo valor: 600.00                                         │
│                                                                          │
│   ┌─────────────────────┐                                               │
│   │ AtomicReference     │                                               │
│   │   valor: 600.00     │                                               │
│   └─────────────────────┘                                               │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 4: Tercer producto (TV 75 - $200)                                  │
│                                                                          │
│   total.updateAndGet(bigDecimal -> bigDecimal.add(200))                 │
│                                                                          │
│   1. Obtiene valor actual: 600.00                                       │
│   2. Ejecuta lambda: 600.00 + 200 = 800.00                              │
│   3. Guarda nuevo valor: 800.00                                         │
│                                                                          │
│   ┌─────────────────────┐                                               │
│   │ AtomicReference     │                                               │
│   │   valor: 800.00     │                                               │
│   └─────────────────────┘                                               │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 5: Obtener el total                                                │
│                                                                          │
│   return total.get();  // Retorna 800.00                                │
└──────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 **Métodos de `AtomicReference`**

| Método | ¿Qué hace? |
|--------|------------|
| `new AtomicReference<>(valor)` | Crea el contenedor con valor inicial |
| `get()` | Obtiene el valor actual |
| `set(nuevoValor)` | Reemplaza el valor |
| `updateAndGet(función)` | Aplica función y retorna el **nuevo** valor |
| `getAndUpdate(función)` | Aplica función y retorna el valor **anterior** |

---

## ⚠️ **Nota: Tu código no multiplica por cantidad**

```java
// Tu código actual suma solo el precio unitario
total.updateAndGet(bigDecimal -> bigDecimal.add(productFromCatalog.getPrice()));

// Probablemente deberías multiplicar por cantidad:
total.updateAndGet(bigDecimal -> bigDecimal.add(
    productFromCatalog.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()))
));
```

---

## 💡 **Alternativa sin AtomicReference (usando Stream)**

```java
private BigDecimal getAndSetProductsAndTotal(List<ProductsDTO> productsDto, OrderEntity orderEntity) {
    
    return productsDto.stream()
        .map(product -> {
            final var productFromCatalog = 
                this.productCatalogRepository.findByName(product.getName()).orElseThrow();
            
            final var productEntity = ProductEntity.builder()
                .quantity(product.getQuantity())
                .catalog(productFromCatalog)
                .build();
            
            orderEntity.addProduct(productEntity);
            productEntity.setOrder(orderEntity);
            
            return productFromCatalog.getPrice()
                .multiply(BigDecimal.valueOf(product.getQuantity()));
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
}
```
## 📚 Clase 88: UPDATE PARTE I 🚀✅✅

### 📚 **Explicación del `return` en el método `update`**

---

## 🔍 **Flujo completo paso a paso**

```
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 1: Cliente envía PUT /order/21                                      │
│                                                                          │
│   {                                                                      │
│     "clientName": "Nuevo Nombre",                                        │
│     "bill": { "clientRfc": "NUEVO123RFC" }                              │
│   }                                                                      │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 2: orderRepository.findById(21)                                     │
│                                                                          │
│   SELECT * FROM orders WHERE id = 21                                     │
│   → Retorna OrderEntity con datos ANTIGUOS                              │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 3: Modificar el Entity                                              │
│                                                                          │
│   toUpdate.setClientName("Nuevo Nombre");                               │
│   toUpdate.getBill().setClientRfc("NUEVO123RFC");                       │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 4: orderRepository.save(toUpdate)                                   │
│                                                                          │
│   UPDATE orders SET client_name = 'Nuevo Nombre' WHERE id = 21          │
│   UPDATE bills SET client_rfc = 'NUEVO123RFC' WHERE order_id = 21       │
│                                                                          │
│   → Retorna el OrderEntity ACTUALIZADO y guardado                       │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 5: mapOrderFromEntity(...)                                          │
│                                                                          │
│   Convierte OrderEntity → OrderDTO                                       │
│   (Para devolver al cliente en formato JSON)                            │
└──────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────┐
│ PASO 6: ResponseEntity.ok(orderDTO)                                      │
│                                                                          │
│   HTTP 200 OK                                                            │
│   {                                                                      │
│     "clientName": "Nuevo Nombre",                                        │
│     "bill": { "clientRfc": "NUEVO123RFC", ... }                         │
│   }                                                                      │
└──────────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 **¿Por qué retornar el objeto actualizado?**

| Razón | Explicación |
|-------|-------------|
| **Confirmación** | El cliente sabe exactamente qué se guardó |
| **Datos generados** | Puede incluir campos calculados (timestamps, totales) |
| **Patrón REST** | Es una práctica estándar en APIs RESTful |
| **Evitar segunda llamada** | El cliente no necesita hacer un GET después |

---

## 📊 **Desglose de la línea**

```java
return this.mapOrderFromEntity(this.orderRepository.save(toUpdate));
//     │                       │                        │
//     │                       │                        └─ 1. Entity modificado
//     │                       │
//     │                       └─ 2. save() guarda y retorna Entity actualizado
//     │
//     └─ 3. Convierte Entity → DTO para la respuesta JSON
```

---

## 💡 **Alternativa: No retornar nada**

Podrías hacerlo así, pero es **menos informativo**:

```java
// Opción sin retorno
@Override
public void update(OrderDTO order, Long id) {
    final var toUpdate = this.orderRepository.findById(id).orElseThrow();
    toUpdate.setClientName(order.getClientName());
    this.orderRepository.save(toUpdate);
    // No retorna nada
}

// Controller retornaría 204 No Content
@PutMapping(path = "{id}")
public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
    this.ordersCrudService.update(orderDTO, id);
    return ResponseEntity.noContent().build();
}
```

Tu implementación actual es **mejor práctica** porque confirma los datos guardados.

# 📚 **Análisis del comportamiento inesperado en el UPDATE**

---

## 🔍 **El problema identificado**

Estás enviando:
```json
{
    "clientName": "Brandon Moreno",
    "bill": { "id": "b-9", "clientRfc": "UPDATERFC000" },
    "products": [{ "quantity": 3, "name": "Pc gamer" }]
}
```

Pero recibes productos **que no enviaste**:
```json
"products": [
    { "quantity": 3, "name": "Pc gamer" },
    { "quantity": 3, "name": "backpack A" },        // ← ¿De dónde salió?
    { "quantity": 3, "name": "Piano digital - home" } // ← ¿De dónde salió?
]
```

---

## 🎯 **Causa raíz: Tu método `update` NO actualiza los productos**

```java
@Override
public OrderDTO update(OrderDTO order, Long id) {
    final var toUpdate = this.orderRepository.findById(id).orElseThrow();

    toUpdate.setClientName(order.getClientName());
    toUpdate.getBill().setClientRfc(order.getBill().getClientRfc());
    // ❌ FALTA: Actualizar los productos

    return this.mapOrderFromEntity(this.orderRepository.save(toUpdate));
}
```

---

## 📊 **Flujo visual del problema**

```
┌─────────────────────────────────────────────────────────────────────┐
│ BD ANTES del UPDATE (Order id=1)                                    │
│   products: [backpack A, Piano digital - home]  ← YA EXISTÍAN      │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│ Tu JSON envía:                                                      │
│   products: [Pc gamer]  ← QUIERES SOLO ESTE                        │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│ Tu método update() IGNORA los productos del JSON                   │
│ Solo actualiza: clientName y clientRfc                             │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│ Resultado: Los productos ORIGINALES se mantienen                    │
│   products: [backpack A, Piano digital - home, ???]                │
└─────────────────────────────────────────────────────────────────────┘
```

---

## ⚠️ **También el `idBill` no se actualiza**

Enviaste `"id": "b-9"` pero se mantiene `"idBill": "b-1"`:

```java
// Tu código solo actualiza el RFC, NO el idBill
toUpdate.getBill().setClientRfc(order.getBill().getClientRfc());
// ❌ FALTA: toUpdate.getBill().setBillId(order.getBill().getIdBill());
```

---

## ✅ **Solución: Actualizar productos correctamente**

```java
@Override
public OrderDTO update(OrderDTO order, Long id) {
    final var toUpdate = this.orderRepository.findById(id).orElseThrow();

    // Actualizar datos básicos
    toUpdate.setClientName(order.getClientName());
    toUpdate.getBill().setClientRfc(order.getBill().getClientRfc());
    toUpdate.getBill().setBillId(order.getBill().getIdBill()); // ← Agregar

    // ✅ LIMPIAR productos existentes y agregar los nuevos
    toUpdate.getProducts().clear();
    
    // Reutilizar tu método existente
    final var total = this.getAndSetProductsAndTotal(order.getProducts(), toUpdate);
    toUpdate.getBill().setTotalAmount(total);

    return this.mapOrderFromEntity(this.orderRepository.save(toUpdate));
}
```

---

## 📋 **Resumen de lo que falta en tu UPDATE**

| Campo | ¿Se actualiza? | ¿Debería? |
|-------|---------------|-----------|
| `clientName` | ✅ SÍ | ✅ |
| `clientRfc` | ✅ SÍ | ✅ |
| `idBill` | ❌ NO | ✅ Agregar |
| `products` | ❌ NO | ✅ Agregar |
| `totalAmount` | ❌ NO | ✅ Recalcular |

---

## 💡 **¿Por qué aparece "Pc gamer" en la respuesta?**

Probablemente hiciste un **POST antes** que agregó ese producto. El UPDATE solo modificó `clientName` y `clientRfc`, dejando los productos intactos.

## 🎯 **Respuesta: ¿Por qué aparecen `id` y `createdAt` en la respuesta?**

---

### 📊 **Flujo del UPDATE**

```
┌────────────────────────────────────────────────────────────────────┐
│ 1. findById(1) → Carga OrderEntity COMPLETO desde BD              │
│    (incluye: id, createdAt, clientName, bill, products)           │
└────────────────────────────────────────────────────────────────────┘
                                    ↓
┌────────────────────────────────────────────────────────────────────┐
│ 2. Modificas SOLO algunos campos:                                  │
│    - setClientName("Brandon Moreno")                               │
│    - setBill().setClientRfc("UPDATERFC000")                        │
│    ❌ id y createdAt NO se tocan (se mantienen)                   │
└────────────────────────────────────────────────────────────────────┘
                                    ↓
┌────────────────────────────────────────────────────────────────────┐
│ 3. save(toUpdate) → Guarda y retorna Entity COMPLETO              │
└────────────────────────────────────────────────────────────────────┘
                                    ↓
┌────────────────────────────────────────────────────────────────────┐
│ 4. mapOrderFromEntity() → Convierte TODOS los campos a DTO        │
│    (incluye: id, createdAt, clientName, bill, products)           │
└────────────────────────────────────────────────────────────────────┘
```

---

### 🔍 **¿De dónde vienen?**

| Campo | Origen | ¿Por qué aparece? |
|-------|--------|-------------------|
| `id: 1` | Base de datos | Se cargó con `findById(1)`, nunca se modificó |
| `createdAt: "2025-12-09 00:48:44"` | Base de datos | Se cargó con `findById`, es inmutable |

---

### 💡 **Es comportamiento CORRECTO**

```java
// Tu mapOrderFromEntity probablemente hace:
orderDTO.setId(entity.getId());           // ← Viene del entity cargado
orderDTO.setCreatedAt(entity.getCreatedAt()); // ← Viene del entity cargado
orderDTO.setClientName(entity.getClientName());
// ... etc
```

Cuando haces **UPDATE**, JPA:
1. **Carga** la entidad existente (con todos sus campos)
2. **Modifica** solo lo que tú cambias
3. **Guarda** y retorna la entidad **completa**

> 🎯 El `id` y `createdAt` **nunca deberían cambiar** en un UPDATE \- es el comportamiento esperado.
>
# ✅ **Exacto, lo entendiste correctamente**

---

## 📊 **Resumen visual**

```
┌─────────────────────────────────────────────────────────────────────┐
│ OrderDTO tiene TODOS estos campos:                                  │
│                                                                     │
│   - id              ← Viene del Entity cargado de BD               │
│   - createdAt       ← Viene del Entity cargado de BD               │
│   - clientName      ← Modificado por tu UPDATE                     │
│   - bill            ← Parcialmente modificado (solo clientRfc)     │
│   - products        ← Vienen del Entity (NO los modificaste)       │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 **El mapeo convierte TODO**

```java
// mapOrderFromEntity hace algo como:
OrderDTO dto = new OrderDTO();
dto.setId(entity.getId());              // ← Se mapea
dto.setCreatedAt(entity.getCreatedAt()); // ← Se mapea
dto.setClientName(entity.getClientName());
dto.setBill(mapBill(entity.getBill()));
dto.setProducts(mapProducts(entity.getProducts()));
return dto;  // ← Retorna DTO COMPLETO
```

---

## 💡 **Por eso Jackson serializa todos los campos a JSON**

| Campo DTO | ¿Tiene valor? | ¿Aparece en JSON? |
|-----------|---------------|-------------------|
| `id` | ✅ Sí (de BD) | ✅ Sí |
| `createdAt` | ✅ Sí (de BD) | ✅ Sí |
| `clientName` | ✅ Sí (actualizado) | ✅ Sí |
| `bill` | ✅ Sí | ✅ Sí |
| `products` | ✅ Sí (de BD) | ✅ Sí |

> 🎯 Jackson serializa **todo lo que no sea `null`** (a menos que uses `@JsonIgnore`).
<<<<<<< HEAD


=======
---
### ⃣📚**Clase 90:DELETE PARTE I**

> - 📚 **Explicacion**
> - Delete con Find son muy parecidos cuando usamos un Api Restful estan muy relacionados con el ID

- ### En OrderRepository  ➡️ 

La anotación `@Modifying` y el método que has seleccionado tienen un problema de sintaxis. Te explico qué significa y cómo corregirlo:

## ¿Qué significa @Modifying?

La anotación `@Modifying` se usa en Spring Data JPA para indicar que el método realizará una **operación de modificación** 
(INSERT, UPDATE o DELETE) en la base de datos, no una consulta de solo lectura.


```java
@Modifying
void deleteAllByClientName(String clientName);
```

## ¿Qué hace este método?

- **Elimina** todas las órdenes (`OrderEntity`) que tengan el `clientName` especificado
- Es un **Query Method** de Spring Data JPA que se genera automáticamente
- La convención `deleteAllBy` + `ClientName` le dice a Spring que:
  - `deleteAllBy`: eliminar todos los registros que coincidan esto se usa mayormente cuando hay varias coincidencias
  - `ClientName`: filtrar por el campo `clientName` de la entidad

## Uso recomendado

Para operaciones de eliminación también necesitas `@Transactional`:

```java
@Modifying
@Transactional
void deleteAllByClientName(String clientName);
```

Sin `@Transactional`, podrías obtener errores al ejecutar la operación de eliminación.

#### En OrderRepository -> 
- Vamos a usar sin ALL por que el nombre es unico ➡️
- Ademas vamos a crear un metodo para validar si existe un nombre de cliente ya registrado ➡️ de tipo Boolean.

```java
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    // haciendo un Query Method que borre por el nombre
    @Modifying
    void deleteByClientName(String clientName);// sin ALL por que el nombre es unico solo habra una sola coincidencia no varias para eso es mejor ALL

    Boolean existsByClientName(String clientName);// este metodo me sirve para validar si existe un nombre de cliente ya registrado
}
```

- ### En OrdersCrudService ➡️ 

```java
    void delete(Long id);
    void delete(String clientName);
```

- ### En OrdersCrudServiceImpl ➡️
- Implementacion del metodo delete ➡️

```java
 @Override
    public void delete(String clientName) {

        if (orderRepository.existsByClientName(clientName)) {
            orderRepository.deleteByClientName(clientName);
        } else {
            throw new IllegalArgumentException("Client name not found");
        }
    }
```
- ### En OrderController ➡️

```java
    @DeleteMapping
    public ResponseEntity<Void> deleteByName(@RequestParam String name) {
        this.ordersCrudService.delete(name);
        return ResponseEntity.noContent().build();//retornamos un 204 cuando borramos
    }
```

La línea `ResponseEntity.noContent().build()` crea una respuesta HTTP con código de estado **204 No Content**.

## ¿Qué significa esto?

- **204 No Content**: Es un código de estado HTTP que indica que la operación fue exitosa, pero no hay contenido que devolver en el cuerpo de la respuesta
- Es el código estándar para operaciones DELETE exitosas
- Le dice al cliente que la eliminación se completó correctamente

## Desglose del código:

```java
ResponseEntity.noContent()  // Establece el código 204
              .build();     // Construye la respuesta sin cuerpo
```

## ¿Por qué se usa en DELETE?

Cuando eliminas un recurso:
1. **Éxito**: El recurso fue eliminado → código 204
2. **Sin contenido**: No necesitas devolver datos después de eliminar
3. **Estándar REST**: Es la práctica recomendada para operaciones DELETE

La respuesta HTTP que recibe el cliente será:
- **Status**: `204 No Content`
- **Body**: Vacío
- **Headers**: Los headers básicos de Spring Boot

Esto confirma al cliente que la eliminación fue exitosa sin necesidad de datos adicionales.

### ⃣📚**Clase 91:DELETE PARTE II Probando el codigo**

###  - 📚 **Explicacion**

La anotación `@Transactional(propagation = Propagation.REQUIRES_NEW)` a nivel de clase define el comportamiento transaccional para todos los métodos del controlador.

## ¿Qué hace `REQUIRES_NEW`?

**Siempre crea una nueva transacción**, independientemente de si ya existe una transacción activa en el contexto actual.

## Comportamiento detallado:

```java
@Transactional(propagation = Propagation.REQUIRES_NEW)
```

- **Nueva transacción**: Cada método del controlador ejecuta en su propia transacción
- **Suspende existentes**: Si ya hay una transacción activa, la suspende temporalmente
- **Independencia**: La nueva transacción puede hacer commit/rollback sin afectar la transacción padre
- **Aislamiento**: Los cambios son independientes de otras operaciones

## ¿Por qué en un controlador?

En tu caso específico puede ser para:

1. **Operaciones críticas**: Cada request debe tener su propia transacción
2. **Rollback independiente**: Si falla una operación, no afecta otras transacciones
3. **Control granular**: Cada endpoint maneja su propia consistencia de datos

## Ejemplo práctico:

Si tienes una transacción activa y llamas a un método del controlador:
- Se suspende la transacción actual
- Se crea una nueva transacción para el método
- Al finalizar, se restaura la transacción original

## Consideraciones:

- **Performance**: Crear nuevas transacciones tiene overhead
- **Conexiones BD**: Puede requerir múltiples conexiones simultáneas
- **Deadlocks**: Mayor riesgo con transacciones concurrentes

Es una estrategia para garantizar que cada operación del controlador sea completamente independiente a nivel transaccional.

### ⃣📚**Clase 92:DELETE PARTE III Probando el codigo**
> 49921df3ecdd22f937a0aa7b31cac5d4bc7c90b1

### ⃣📚**Clase 93:DINSERTS Y DELETES EN BACH PARTE I**

El método `saveAllAndFlush()` sirve para **insertar múltiples entidades en una sola operación y forzar la escritura inmediata** a la base de datos.

## ¿Qué hace específicamente?

1. **`saveAll()`**: Guarda todas las entidades de la lista en una sola transacción (más eficiente que guardar una por una)

2. **`flush()`**: Fuerza la **sincronización inmediata** con la base de datos, ejecutando todas las operaciones SQL pendientes

## ¿Por qué usar `flush()` aquí?

En tu código necesitas el `flush()` porque estás **midiendo el tiempo real** de inserción:

```java
final var start = System.currentTimeMillis();
this.productCatalogRepository.saveAllAndFlush(products);
final var end = System.currentTimeMillis();
```

Sin `flush()`:
- Los datos se quedarían en el contexto de persistencia (caché)
- Las operaciones SQL se ejecutarían después (lazy)
- **El tiempo medido sería incorrecto** (no incluiría la escritura real a BD)

Con `saveAllAndFlush()`:
- Se ejecutan inmediatamente las operaciones SQL
- **El tiempo medido es real** (incluye la escritura a BD)
- Obtienes métricas precisas de rendimiento

## Alternativas:

- `saveAll()` - Guarda pero no sincroniza inmediatamente
- `save()` individual - Menos eficiente para lotes grandes
- `flush()` manual después de `saveAll()`

En operaciones batch como la tuya, `saveAllAndFlush()` es ideal para medición de rendimiento y garantizar persistencia inmediata.

# 🗑️ Método `delteBatch()` - Eliminación en Lote

## 📋 ¿Qué hace este código?

Este método implementa **eliminación masiva** de productos del catálogo con medición de rendimiento.

## 🔍 Desglose paso a paso:

### 1️⃣ **Obtener todos los IDs**
```java
var ids = this.productCatalogRepository.findAll()
        .stream()
        .map(ProductCatalogEntity::getId)
        .toList();
```

- 📥 `findAll()` - Obtiene **todas** las entidades de la BD
- 🔄 `.stream()` - Convierte la lista en un stream para procesamiento
- 🏷️ `.map(ProductCatalogEntity::getId)` - **Extrae solo el ID** de cada entidad
- 📝 `.toList()` - Convierte el stream de IDs en una lista

### 2️⃣ **Medición de tiempo**
```java
final var start = System.currentTimeMillis();
// ... operación de eliminación ...
final var end = System.currentTimeMillis();
```

- ⏱️ Captura el tiempo **antes y después** de la eliminación
- 📊 Permite medir el **rendimiento** de la operación

### 3️⃣ **Eliminación en lote**
```java
this.productCatalogRepository.deleteAllById(ids);
```

- 🚀 **Elimina múltiples registros** en una sola operación SQL
- ⚡ Más eficiente que eliminar uno por uno

## 🎯 ¿Para qué sirve?

- **🧹 Limpieza masiva**: Elimina todos los productos del catálogo
- **📈 Medición de rendimiento**: Evalúa qué tan rápido se ejecuta la eliminación
- **⚡ Optimización**: Usa eliminación en lote (más eficiente)
- **📝 Logging**: Registra el proceso y tiempo de ejecución

## ⚠️ Consideraciones importantes:

- **🔴 Peligroso**: Elimina **TODOS** los productos
- **💾 Sin transacción explícita**: Podría necesitar `@Transactional`
- **🐌 Ineficiente**: Primero carga todas las entidades solo para obtener IDs

## 🚀 Alternativa más eficiente:

```java
this.productCatalogRepository.deleteAll(); // Elimina todo directamente
```

---
### Notas con respecto a la lista que se agregara:
El patrón Builder está disponible gracias a **Lombok**. Cuando usas la anotación `@Builder` en tu entidad `ProductCatalogEntity`, Lombok genera automáticamente en tiempo de compilación:

1. **El método estático `builder()`** que retorna una instancia del builder
2. **La clase Builder interna** con métodos para cada campo
3. **El método `build()`** que construye el objeto final

## Cómo funciona:

```java
@Entity
@Builder  // Esta anotación genera el patrón Builder
@Data     // Genera getters, setters, etc.
public class ProductCatalogEntity {
    private String name;
    private String brand;
    private BigDecimal price;
    // ... otros campos
}
```

## Lo que Lombok genera internamente:

```java
// Lombok genera algo similar a esto:
public static ProductCatalogEntityBuilder builder() {
    return new ProductCatalogEntityBuilder();
}

public static class ProductCatalogEntityBuilder {
    private String name;
    private String brand;
    
    public ProductCatalogEntityBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    public ProductCatalogEntityBuilder brand(String brand) {
        this.brand = brand;
        return this;
    }
    
    public ProductCatalogEntity build() {
        return new ProductCatalogEntity(name, brand, ...);
    }
}
```

## Respecto a `List.of()`:

Sí es perfectamente viable usar `List.of()` con múltiples objetos creados con builder. `List.of()` acepta elementos separados por comas y crea una lista inmutable con esos elementos.

El código funciona porque:
1. Cada `ProductCatalogEntity.builder()...build()` crea un objeto completo
2. `List.of()` toma esos objetos como argumentos separados
3. Se crea una lista inmutable con todos los productos

---
El método `.builder()` **NO está declarado en tu clase actual** `CatalogBatchImpl`. Está declarado en la **clase `ProductCatalogEntity`**.

## Lo que sucede:

1. **En `ProductCatalogEntity`** (que importas con `import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;`) debe tener la anotación `@Builder`

2. **Lombok genera automáticamente** el método estático `builder()` en esa entidad

3. **En tu clase actual** simplemente **usas** ese método que ya existe en `ProductCatalogEntity`

## Ejemplo de cómo debe estar `ProductCatalogEntity`:

```java
@Entity
@Builder  // Esta anotación genera el método builder()
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCatalogEntity {
    private String name;
    private String brand;
    private BigDecimal price;
    private LocalDate launchingDate;
    private Boolean isDiscount;
    private String description;
    private Short rating;
}
```

## Por eso funciona:

- `ProductCatalogEntity.builder()` - Llama al método estático generado por Lombok en la entidad
- No necesitas declararlo en `CatalogBatchImpl` porque **usas** el método de otra clase
- Es como usar `String.valueOf()` o `LocalDate.now()` - métodos que existen en otras clases

La clave está en que **Lombok genera el builder en la clase de la entidad**, no en donde lo usas.

### ⃣📚**Clase 94:DINSERTS Y DELETES EN BACH PARTE II**

> No se realizo por que habria que ejecutar todo denuevo

- Con esto ejecutamos el borrado en lote y las inserciones

```java
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GadgetPlusApplication_Batch {//implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GadgetPlusApplication_Batch.class, args);
    }

 /*   @Autowired
    private CatalogBatch catalogBatch;

    @Override
    public void run(String... args) throws Exception {
        this.catalogBatch.insertBatch();

        Thread.sleep(1000);

        this.catalogBatch.delteBatch();

    }*/
}

```
# 🗑️ Método `delteBatch()` - Eliminación en Lote

## 📋 ¿Qué hace este código?

Este método implementa **eliminación masiva** de productos del catálogo con medición de rendimiento.

## 🔍 Desglose paso a paso:

### 1️⃣ **Obtener todos los IDs**
```java
var ids = this.productCatalogRepository.findAll()
        .stream()
        .map(ProductCatalogEntity::getId)
        .toList();
```

- 📥 `findAll()` - Obtiene **todas** las entidades de la BD
- 🔄 `.stream()` - Convierte la lista en un stream para procesamiento
- 🏷️ `.map(ProductCatalogEntity::getId)` - **Extrae solo el ID** de cada entidad
- 📝 `.toList()` - Convierte el stream de IDs en una lista

### 2️⃣ **Medición de tiempo**
```java
final var start = System.currentTimeMillis();
// ... operación de eliminación ...
final var end = System.currentTimeMillis();
```

- ⏱️ Captura el tiempo **antes y después** de la eliminación
- 📊 Permite medir el **rendimiento** de la operación

### 3️⃣ **Eliminación en lote**
```java
this.productCatalogRepository.deleteAllById(ids);
```

- 🚀 **Elimina múltiples registros** en una sola operación SQL
- ⚡ Más eficiente que eliminar uno por uno

## 🎯 ¿Para qué sirve?

- **🧹 Limpieza masiva**: Elimina todos los productos del catálogo
- **📈 Medición de rendimiento**: Evalúa qué tan rápido se ejecuta la eliminación
- **⚡ Optimización**: Usa eliminación en lote (más eficiente)
- **📝 Logging**: Registra el proceso y tiempo de ejecución

## ⚠️ Consideraciones importantes:

- **🔴 Peligroso**: Elimina **TODOS** los productos
- **💾 Sin transacción explícita**: Podría necesitar `@Transactional`
- **🐌 Ineficiente**: Primero carga todas las entidades solo para obtener IDs

## 🚀 Alternativa más eficiente:

```java
this.productCatalogRepository.deleteAll(); // Elimina todo directamente
```
