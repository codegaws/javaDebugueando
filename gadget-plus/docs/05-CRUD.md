
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
---

##  ⃣📚**Clase 78:CREANDO SERVICIOS*

### ¿QUE ES UN DTO PARA QUE SIRVE?
✅ **Sí, los DTO (Data Transfer Object) son objetos diseñados para transportar datos entre procesos o capas de una aplicación.**

---

### 📦 **¿Para qué sirven los DTO?**
- Permiten **personalizar** la información que envías o recibes, mostrando solo los campos necesarios.
- Ayudan a **proteger** la estructura interna de tus entidades.
- Facilitan la **interoperabilidad** entre diferentes capas (por ejemplo, entre el backend y el frontend).

---

### 🛠️ **¿Cómo funcionan?**
- Puedes crear diferentes DTOs según lo que necesites mostrar u ocultar en cada caso.
- No tienen lógica de negocio, solo contienen datos.

---

### 🧩 **Ejemplo visual**
```java
// Entidad
public class OrderEntity {
    private Long id;
    private LocalDateTime orderDate;
    private Double total;
    // ...otros campos
}

// DTO personalizado
public class OrderDTO {
    private LocalDateTime orderDate;
    private Double total;
    // Solo los campos que necesitas
}
```

---

### 📝 **Resumen**
- Los DTO **varían** según lo que necesitas transferir.
- Te devuelven **solo los datos necesarios** para cada caso de uso.

---

🔗 **Ideal para APIs REST y separación de capas.**

---
### Resumen que pasa con los atributos de DTO ¿Se usan todos?: 

> Se puede usar solo algunos datos cuando es necesario no todos los de DTO

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

### ⚠️⚠️PASOS A SEGUIR⚠️⚠️ : 

> - Se crea en repositories public interface OrderRepository extends CrudRepository<OrderEntity, Long> { }
> - Se crea en services OrdersCrudService
> - Se crea en services ProductsCrudService

---

### 📚 Clase 79: MODEL MAPPER VS OBJECT MAPPER

visitar : https://modelmapper.org/ es mas usado en spring boot
visitar -> esto es una libreria de jackson : https://mapstruct.org/documentation/installation/



#### 🤖 **¿Qué es ModelMapper?**
ModelMapper es una librería de Java que facilita la conversión automática entre objetos de diferentes clases, por ejemplo, de entidades a DTOs y viceversa. Es útil cuando los objetos tienen estructuras similares.

- 🔄 **Ventajas:** Reduce el código manual de mapeo, es fácil de configurar y usar.
- ⚡ **Uso típico:** `modelMapper.map(source, Destination.class);`

---

#### 🧩 **¿Qué es ObjectMapper?**
ObjectMapper es una clase de la librería Jackson que permite convertir objetos Java a JSON y viceversa. Es fundamental para la serialización y deserialización en aplicaciones Spring Boot.

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
| Herramienta      | Propósito                  | Uso principal         |
|------------------|---------------------------|----------------------|
| 🤖 ModelMapper   | Mapear entre clases Java   | Entidad ↔ DTO        |
| 🧩 ObjectMapper  | JSON ↔ Objeto Java         | API REST, serializar |

---
### 📚 Clase 80: MODEL MAPPER READ PARTEI

```java
// Java
private OrderDTO mapOrderFromEntity(OrderEntity order) {
    final var mapper = new ModelMapper();
    return mapper.map(order, OrderDTO.class);
}
```

- Mediante java Reflexion agarra las propiedades de un objeto y las mapea a otro objeto similar
- sin el nombre coincide por ejemplo si tenemos un objeto OrderEntity y otro OrderDTO y ambos tienen una propiedad llamada createdAt
- si son iguales las va a mapear automaticamente.

> Agregamos al pomxml

```xml
        <dependency>
            <groupId>org.modelmapper</groupId>
            <artifactId>modelmapper</artifactId>
            <version>3.2.0</version>
        </dependency>
```
### 🧩 ¿Para qué sirve este método?

El método `mapOrderFromEntity` convierte un objeto de tipo `OrderEntity` (entidad de base de datos) a un objeto `OrderDTO` (Data Transfer Object).

- **¿Por qué es útil?**  
  Permite separar la lógica de la base de datos de la lógica de transferencia de datos, facilitando el envío de información al frontend o a otras capas de la aplicación.

- **¿Cómo lo hace?**  
  Utiliza la librería `ModelMapper` para copiar automáticamente los datos de la entidad al DTO, evitando hacerlo manualmente.

🔹 **Resumen:**  
Este método ayuda a transformar datos entre capas de la aplicación de forma sencilla y automática.

---
### 📚 Clase 81: MODEL MAPPER READ PARTEII

> #### - Si no pongo final en el atributo orderRepository no me va a funcionar la inyeccion de dependencias con @RequiredArgsConstructor

### 🛡️ ¿Por qué usar `final` aquí?

- `final` indica que la variable `orderRepository` **no puede cambiar** después de ser inicializada.
- Esto ayuda a mantener la **inmutabilidad** y hace el código más seguro y fácil de entender.

### 🏗️ ¿Qué hace `@RequiredArgsConstructor`?

- `@RequiredArgsConstructor` (de Lombok) genera automáticamente un **constructor** que recibe como parámetros todos los campos `final` (y los que son `@NonNull`).
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
Si no usas `final`, `@RequiredArgsConstructor` no incluirá ese campo en el constructor, y Spring no podrá inyectar la dependencia, causando errores al iniciar la aplicación.

