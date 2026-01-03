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

