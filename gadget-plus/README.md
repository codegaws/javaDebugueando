## DETALLE DEL PROYECTO GADGETPLUS

<details>
    <summary><strong>SESSION 3</strong></summary>
- En pomxml

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

</dependencies>
```

- En properties

```java

spring.application.name=gadget-plus

#
En tu
application.properties o
application.yml
spring.datasource.url=jdbc:postgresql://172.28.151.240:5432/gadget_plus
spring.datasource.username=debuggeandoideas
spring.datasource.password=secret
spring.datasource.driver-class-name=org.postgresql.Driver

spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.maximum-pool-size=5

        #
enabled logs
spring.jpa.show-sql=true

        #
show best
format
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

        #
configure logs
logging.level.com.baeldung.testloglevel=DEBUG
logging.level.org.springframework.orm.jpa=DEBUG
logging.level.org.springframework.transaction=DEBUG
logging.level.org.springframework.data.jpa=DEBUG
logging.level.org.hibernate.SQL=DEBUG


```

---

# CLASE 21 -> ENTIDADES JPA

- Query para ver como esta estructurado nuestra base de datos

````sql
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_name = 'orders';

````

# # CLASE 22 -> MAPEO DE ENTIDADES

## FETCH TYPE :

> FETCHTYPE.EAGER : Carga inmediata de datos relacionados carga ansiosa
> Su valor por defecto es @OneToOne y @ManyToOne entonces si no especificas el tipo de FETCHTYPE
> su valor por defecto son estas dos. sin embargo cuando quieres usar en fetchtype.lazy @OneToOne y @ManyToOne
> es bien comun la excepcion que se llama lazy InitializationException.Esta excepcion ocurre debido a que en JPA
> necesita crear un proxy para implementar la carga perezosa, osea LazyLoading y en las relaciones one to one
> no siempre es posible crear este proxy.Entonces tener cuidado cuando tengas un tipo lazy y una asociacion
> @OneToOne y @ManyToOne.
>
> ---
>
> FETCHTYPE.LAZY : Carga diferida de datos relacionados., es lo contratio de la carga perezosa imaginate que tienes
> departamento con empleados primero carga departamento y cuando necesites los empleados ahi si los carga.
> Su valor por defecto es @OneToMany y @ManyToMany.,¿Cuando cargas a empleados ? solo cuando se lo indiques en la query
>
---

## CASCADE TYPE:

> CASCADE TYPE es una opcion que le indicas a JPA que cuando realices una operacion en una entidad
> se propague a las entidades relacionadas. Por ejemplo si tienes una entidad padre y una entidad hijo
> y quieres que cuando elimines el padre se elimine el hijo tambien, entonces usas cascade type remove.
>
> Existen varios tipos de cascade type:
> - ALL: Propaga todas las operaciones (persistir, fusionar, eliminar, refrescar, desaprobar).
> - PERSIST: Propaga la operación de persistencia (guardar).
> - MERGE: Propaga la operación de fusión (actualizar).
> - REMOVE: Propaga la operación de eliminación.
> - REFRESH: Propaga la operación de refresco (sincronizar con la base de datos).
> - DETACH: Propaga la operación de desaprobar (desvincular de la sesión de persistencia).
> - NONE: No propaga ninguna operación.
>
> Es importante usar cascade type con precaución, ya que puede tener implicaciones en el rendimiento y la integridad de
> los datos.
>

## ORPHAN REMOVAL:

> ORPHAN REMOVAL es una opcion que le indicas a JPA que cuando una entidad hija ya no este asociada a su entidad padre
> se elimine automaticamente de la base de datos. Por ejemplo si tienes una entidad padre y una entidad hijo
> y quieres que cuando elimines la referencia del hijo en el padre se elimine el hijo tambien, entonces usas orphan
> removal.
>
> Es importante usar orphan removal con precaucion, ya que puede tener implicaciones en la integridad de los datos.
> Proposito especifico es ORPHAN REMOVAL se aplica en relaciones One to Many y One to one.
> Cuando se configura como true , JPA elimina automaticamente las entidades hijas que ya no estan asociadas a su entidad
> padre.
---

## ¿DIFERENCIA ENTRE EL ORPHAN REMOVAL Y EL CASCADETYPE?:

>
> ORPHAN REMOVAL SE ACTIVA CUANDO SE ELIMINA LA REFERENCIA A LA LLAVE FORANEA DE LA ENTIDAD HIJA EN LA ENTIDAD PADRE.
> CASCADE TYPE SE ACTIVA CUANDO SE REALIZA UNA OPERACION DE ELIMINACION EN LA ENTIDAD PADRE. AQUI SE ELIMINA TODO TANTO
> ENTIDAD PADRE
> COMO HIJO

# CLASE 23 -> CRUD REPOSITORY

- public interface OrderRepository extends CrudRepository<OrderEntity, Long> { }
- Recuerda que CrudRepository ya tiene los metodos basicos para hacer un CRUD
- necesita dos parametros. el tipo de entidad y el tipo de dato de la llave primaria
- Ejemplo:
- OrderEntity es la entidad
- Long es el tipo de dato de la llave primaria
- Los metodos basicos son:
- save(S entity): Guarda una entidad en la base de datos.
- findById(ID id): Busca una entidad por su ID.
- findAll(): Devuelve todas las entidades.
- deleteById(ID id): Elimina una entidad por su ID.
- delete(S entity): Elimina una entidad.
- count(): Devuelve el numero de entidades.
- existsById(ID id): Verifica si una entidad existe por su ID.

# ¿consulta mis repositorios BillRepository y OrderRepository son 
# llamados en mi clase main como @Autowired entonces por que en mis 
# repositorios no estan como @component o @Repository???

 **No necesitas** `@Component` o `@Repository` en tus interfaces de repositorio porque:

## Spring Data JPA maneja automáticamente los repositorios

Cuando una interface extiende `CrudRepository` (o cualquier repositorio de Spring Data):

1. **Spring Boot detecta automáticamente** todas las interfaces que extienden repositorios de Spring Data
2. **Crea implementaciones proxy** en tiempo de ejecución
3. **Los registra como beans** en el contenedor de Spring automáticamente

## ¿Por qué funciona el @Autowired?

```java
@Autowired
private BillRepository billRepository;

@Autowired  
private OrderRepository orderRepository;
```

Funciona porque Spring ya creó e inyectó estos beans automáticamente, **sin necesidad de anotaciones**.

## ¿Cuándo usar @Repository?

Solo necesitas `@Repository` cuando:
- Implementas tu propio repositorio personalizado
- Usas clases concretas en lugar de interfaces de Spring Data

```java
// Esto SÍ necesitaría @Repository
@Repository
public class CustomBillRepositoryImpl {
    // implementación manual
}
```

## Resumen

Tus repositorios están **correctamente configurados**. Spring Data JPA se encarga de todo automáticamente cuando detecta las interfaces que extienden `CrudRepository`.

---

## nota :

- Mapeamos solo lo que necesitamos.

```sql
@
Entity
@Table(name="orders")
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column
(name = "client_name", length = 32, nullable = false)
    private String clientName;
//no es necesario mapear el guion bajo
}
```

- Hemos agregado un comandLine runner para probar el repositorio

```java

@SpringBootApplication
public class GadgetPlusApplication implements CommandLineRunner {

    @Autowired
    private OrderRepository orderRepository;

    public static void main(String[] args) {
        SpringApplication.run(GadgetPlusApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        this.orderRepository.findAll().forEach(System.out::println);
    }
}
```

![imagen](/images/1.png)

# CLASE 24 -> ONETOONE

> VAMOS A UNIR LA TABLA ORDERS CON LA TABLA BILL A TRAVES DE LO QUE ES EL ID Y EL ID_BILL

![imagen](/images/2.png)

- Creamos un BillEntity

```java

@Entity
@Table(name = "bill")
@Data
public class BillEntity {

    @Id
    @Column(nullable = false, length = 64)
    private String id;

    @Column
    private BigDecimal totalAmount;

    @Column(name = "client_rfc", length = 14, nullable = false)
    private String rfc;

}
```

---

- En OrderEntity agregamos la relacion one to one

![imagen](/images/3.png)

# CLASE 25 -> FETCH TYPE LAZY

SI PONEMOS FETCH TYPE LAZY EN LA RELACION ONE TO ONE NOS VA A DAR UNA EXCEPCION
>
>![imagen](/images/4.png)
>
> LazyInitializationException.Esta excepcion ocurre debido a que en JPA
> necesita crear un proxy para implementar la carga perezosa, osea LazyLoading y en las relaciones one to one
> no siempre es posible crear este proxy.Entonces tener cuidado cuando tengas un tipo lazy y una asociacion
> @OneToOne y @ManyToOne.
>
> ![imagen](/images/5.png)
>
> solucion: cambiar a fetch type eager o usar DTOs para evitar este problema, en este ejemplo hemos accedido solo
> a los nombres con fetch type lazy
>
> ![imagen](/images/6.png)
>
> ## Resultado en consola
> ![imagen](/images/7.png)
>
> ## RESUMEN
> El EAGER trae todo OrderEntity y BillEntity
> - this.orderRepository.findAll().forEach(o -> System.out.println(o.toString()));// aqui te trae todo el objeto order
    con bill incluido
    > El LAZY no puede traer el Bill por eso falla si tratas de imprimir todo el objeto order con bill incluido.
>
> - this.orderRepository.findAll().forEach(o -> System.out.println(o.getClientName()));// aqui solo te trae el nombre
    del cliente y no falla
    > otra solucion es para que no truene usamos el metodo de lombok ### @ToString.Exclude() ###
> - y asi evitamos que se imprima el objeto bill
> - @ToString.Exclude -> quedaria asi

```java

@ToString.Exclude
@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
@JoinColumn(name = "id_bill", nullable = false, unique = true)
private BillEntity bill;

```

---
La anotación `@ToString.Exclude` excluye el campo `bill` del método `toString()` generado automáticamente por Lombok.

**¿Por qué se usa?**

Cuando tienes relaciones JPA con `FetchType.LAZY`, si intentas imprimir el objeto completo (usando `toString()`), puede
causar:

1. **LazyInitializationException** - Si la sesión de Hibernate ya está cerrada
2. **Consultas SQL no deseadas** - Hibernate intentará cargar la relación lazy cuando acceda al campo `bill` en el
   `toString()`
3. **Recursión infinita** - Si `BillEntity` también tiene una referencia de vuelta a `OrderEntity`

**Ejemplo de lo que sucede:**

Sin `@ToString.Exclude`:

```java
// Esto podría fallar con LazyInitializationException
System.out.println(order.toString()); // Intenta acceder a order.bill
```

Con `@ToString.Exclude`:

```java
// Esto funciona sin problemas
System.out.println(order.toString()); // No accede a order.bill
```

**Resultado:**

- El `toString()` generado incluirá `id`, `createdAt` y `clientName`
- **NO** incluirá el campo `bill`, evitando los problemas mencionados

Es una práctica común usar `@ToString.Exclude` en relaciones JPA, especialmente con `LAZY` loading.

# CLASE 27 -> ONETOONE CIRCULAR

## LO QUE SE DESEA HACER ES UN JOIN orders y bill

![image](/images/9.png)

```sql

SELECT *
FROM orders o
         join bill b on b.id = o.id_bill;

```

### En Order Entity se mapea el Bill este esta realizando el JOIN y en BillEntity se mapea la orden pero esta es la parte inversa de la relacion

### no es necesario hacer el JOIN desde BillEntity es redundante.

### En `OrderEntity`:

```java

@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
@JoinColumn(name = "id_bill", nullable = false, unique = true)
private BillEntity bill;

```

- `@OneToOne`: Define la relación uno a uno.
- `fetch = FetchType.LAZY`: No carga la factura (bill) automáticamente, solo cuando la necesitas.
- `cascade = CascadeType.ALL`: Si guardas/borras una orden, también afecta a su factura asociada.
- `@JoinColumn(name = "id_bill", ...)`: Especifica la columna en la tabla `orders` que guarda el ID de la factura.

---

### En `BillEntity`:

```java

@OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
private OrderEntity order;
```

- `mappedBy = "bill"`: Indica que esta es la parte **inversa** de la relación, y que la clave foránea vive en la otra
  entidad (`OrderEntity`).

---

> Le vamos a dar a la entidad OrderEntity mas importancia y vamos a excluir la relacion inversa en BillEntity
> para evitar que no me aparezca en el toString de BillEntity la relacion con OrderEntity y evitar problemas de
> recursividad infinita
> StackOverflowError

```java

@ToString.Exclude
@OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
private OrderEntity order;

```

- Con esto se soluciona todo e imprime correctamente el JOIN con la entidad OrderEntity

```java

OrderEntity(id=1, createdAt=2025-10-28T02:20:18.193608, clientName=Ronda Rousey, bill=BillEntity(id=b-1, totalAmount=8101.76, rfc=ERT655687JHY))

OrderEntity(id=2, createdAt=2025-10-28T02:20:18.193608, clientName=Amanda Nunes, bill=BillEntity(id=b-2, totalAmount=4301.88, rfc=AZ45NM78BC79))

```
---

## Nota explicacion por que existen estas relaciones

> Las asociaciones que ves en las entidades `OrderEntity` y `BillEntity` son relaciones de **mapeo de objetos a tablas**
> usando JPA (Jakarta Persistence API) para reflejar cómo los datos se relacionan en la base de datos. Te explico el
> propósito y motivo de cada parte:

---

## ¿Por qué se usan estas asociaciones?

### 1. **Relación @OneToOne**

- La anotación `@OneToOne` significa que **cada entidad de un lado de la relación se asocia con exactamente una entidad
  del otro lado**.
- En este caso, cada `OrderEntity` está asociada a una sola `BillEntity` y viceversa.

### 2. **¿Por qué existen estas asociaciones?**

- **Representan reglas del negocio.** Por ejemplo, si en tu sistema **cada orden tiene exactamente un
  comprobante/factura (bill), y cada factura pertenece a una sola orden**, entonces una relación uno a uno es la forma
  correcta de modelar esto, tanto en la base de datos como en el modelo de objetos Java.
- **Facilitan operaciones CRUD y navegación entre entidades.** Puedes acceder fácilmente desde una orden a su factura, y
  desde una factura a su orden usando los getters/setters generados por Lombok.

## ¿Por qué no usar @OneToMany o @ManyToOne?

- Si una factura pudiera tener **muchas órdenes** asociadas, usarías `@OneToMany` o `@ManyToOne`.
- Si un pedido pudiera tener **muchas facturas**, también usarías una opción diferente.
- Pero en tu modelo, la relación es **estrictamente uno a uno**.

---

## Resumen

- **Motivo principal**: Reflejar la realidad del dominio del negocio (una orden solo tiene una factura y viceversa).
- **Ventaja**: Permite integridad referencial, navegación sencilla entre entidades y un modelo de datos claro y
  mantenible.

Si tienes un caso de negocio donde esto no se cumple, deberías reconsiderar la relación. Pero si cada orden/factura es
única y está emparejada, ¡este es el patrón correcto!

# SI LE METEMOS O COMPLIMOS CON ESTAS RELACIONES PODEMOS TENER PROBLEMAS DE RECUSION INFINITA AL MOMENTO DE IMPRIMIR LOS OBJETOS

![imagen](/images/8.png)

# EN RESUMEN POR ESTO ES QUE USAMOS @ToString.Exclude PARA EVITAR ESTOS PROBLEMAS DE RECUSION INFINITA

¡Excelente pregunta! El problema de **recursividad infinita** en JPA es muy común cuando trabajas con relaciones bidireccionales. Te lo explico con tu caso específico:

## 🔄 ¿Por qué ocurre la recursividad infinita?

Tienes una relación **bidireccional** `@OneToOne` entre `OrderEntity` y `BillEntity`:

```
OrderEntity → bill (tiene referencia a BillEntity)
     ↓
BillEntity → order (tiene referencia de vuelta a OrderEntity)
```

### El problema ocurre cuando llamas a `toString()`:

1. **OrderEntity.toString()** → imprime sus campos + llama a `bill.toString()`
2. **BillEntity.toString()** → imprime sus campos + llama a `order.toString()`
3. **OrderEntity.toString()** → imprime sus campos + llama a `bill.toString()`
4. ... ♾️ **CICLO INFINITO** → StackOverflowError

## ✅ Soluciones

### 1. **Usar `@ToString.Exclude` en AMBOS lados** (Recomendado)

Ya lo tienes en `BillEntity`, pero debes asegurarte de tenerlo también en `OrderEntity`:

```java
// En OrderEntity
@ToString.Exclude
@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
@JoinColumn(name = "id_bill", nullable = false, unique = true)
private BillEntity bill;

// En BillEntity (ya lo tienes)
@ToString.Exclude
@OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
private OrderEntity order;
```

### 2. **Usar `@JsonIgnore` o `@JsonBackReference`** (para serialización JSON)

Si además trabajas con APIs REST, también necesitas evitar la recursividad al serializar a JSON:

```java
// En BillEntity (lado NO dueño)
@JsonBackReference
@ToString.Exclude
@OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
private OrderEntity order;
```

## 🎯 Análisis de tu esquema SQL

Según tu tabla, **OrderEntity es el dueño** de la relación porque:

```sql
CREATE TABLE orders (
    ...
    id_bill VARCHAR(64) UNIQUE NOT NULL,
    FOREIGN KEY (id_bill) REFERENCES bill(id) ON DELETE CASCADE
);
```

La columna `id_bill` está en la tabla `orders`, por eso en JPA:
- ✅ **OrderEntity** usa `@JoinColumn` (dueño de la relación)
- ✅ **BillEntity** usa `mappedBy = "bill"` (lado inverso)

## ⚠️ Advertencias adicionales

### 1. **EAGER fetching bidireccional es peligroso**
```java
// Ambos tienen FetchType.EAGER
fetch = FetchType.EAGER
```

Esto puede causar problemas de rendimiento. Considera usar `LAZY`:

```java
// En OrderEntity
@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
@JoinColumn(name = "id_bill", nullable = false, unique = true)
private BillEntity bill;

// En BillEntity
@ToString.Exclude
@OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private OrderEntity order;
```

### 2. **CascadeType.ALL puede ser peligroso**
Con `CascadeType.ALL` en ambos lados, cualquier operación se propaga. Ten cuidado con operaciones de borrado.

## 📝 Ejemplo completo corregido

```java
// OrderEntity (dueño de la relación)
@Entity
@Table(name = "orders")
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "client_name", nullable = false, length = 32)
    private String clientName;
    
    @ToString.Exclude // 👈 IMPORTANTE
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_bill", nullable = false, unique = true)
    private BillEntity bill;
}

// BillEntity (lado inverso)
@Entity
@Table(name = "bill")
@Data
public class BillEntity {
    @Id
    @Column(length = 64)
    private String id;
    
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    
    @Column(name = "client_rfc", nullable = false, length = 14)
    private String clientRfc;
    
    @JsonBackReference // 👈 Para APIs REST
    @ToString.Exclude  // 👈 IMPORTANTE
    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrderEntity order;
}
```
¡Con estos cambios ya no tendrás recursividad infinita! 🎉

---

## #️ ⃣📚**Clase 28: CASCADE PERSIST`**

### SI TIENES DUDAS DE POR QUE BIGDECIMAL 
La diferencia es por el tipo de dato que espera cada atributo:

- Los atributos `String` (como `id` o `rfc`) esperan cadenas de texto, por eso les asignas valores entre comillas, por ejemplo: `"AS537GD7D"`.
- El atributo `totalAmount` es de tipo `BigDecimal`, que es una clase especial de Java para manejar números decimales con precisión (ideal para dinero).

Cuando escribes `.totalAmount(BigDecimal.TEN)`, no estás poniendo un número directamente, sino que le estás pasando un objeto `BigDecimal` que representa el número 10.  
No puedes poner simplemente `.totalAmount(10.0)` porque eso sería un `double`, y Java no lo convierte automáticamente a `BigDecimal` (por precisión y seguridad).

Si quieres asignar otro valor, puedes hacerlo así:

```java
.totalAmount(new BigDecimal("8101.76"))
```

Esto crea un objeto `BigDecimal` con el valor exacto que necesitas, igual que el que tienes en tu base de datos.  
En resumen: usas `BigDecimal` para mantener la precisión en los valores decimales, no números primitivos ni cadenas.

---
## EXCEPTION CUANDO QUIERES GENERAR DATOS NUEVOS EXCEPTION TRANSIENT

Si no pones el cascade = CascadeType.ALL te va a salir ese error por que recuerda que estas usando 
```java
@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
@JoinColumn(name = "id_bill", nullable = false, unique = true)
private BillEntity bill;
```
- El detalle es que estas tratando de persistir un bill que aun no esta creado en la base de datos y por eso te sale el error
`org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance before flushing: com.debuggeandoideas.gadgetplus.entity.BillEntity`
- La solucion es usar cascade = CascadeType.ALL para que cuando guardes la orden tambien se guarde el bill asociado
- 
```java
// SETEAMOS
        var bill = BillEntity.builder()
                .rfc("AS537GD7X")
                .totalAmount(BigDecimal.TEN)
                .id("b-18")
                .build();

        var order = OrderEntity.builder()
                .createdAt(LocalDateTime.now())
                .clientName("Alex Martinez")
                .bill(bill)
                .build();
        this.orderRepository.save(order);
```
## Se agrego dos nuevos registros a la bd

![images](/images/10.png)
---

## #️ ⃣📚**Clase 29: CASCADE MERGE`**

cascade : Si no pusiera cascade type merge me va a salir la exception, o no actualiza el bill asociado a la orden

```java

 CascadeType.PERSIST,CascadeType.MERGE -> PERSIST PARA EL SAVE Y MERGE PARA EL UPDATE

 @OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
 @JoinColumn(name = "id_bill", nullable = false, unique = true)
 private BillEntity bill;

```
---
## #️ ⃣📚**Clase 30: CASCADE DELETE-DETACH`**

```java
//EN MAIN ->
// ************* Ejercicio CASCADE.DELETE AUNQUE MEJOR ES CASCADE.ALL *************
        var order = this.orderRepository.findById(17L).get();
        this.orderRepository.delete(order);
        //borramos el order y el bill asociado con cascade delete con id 17L
//********************************************************************************************************************************************
//EN ORDER ->
// Relación uno a uno con BillEntity DELETE.TYPE.MERGE y PERSIST
//con DETACH BORRAMOS TANTO EL HIJO COMO EL PADRE OSEA DEL ORDER Y DEL BILL
@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)// lo menos comun es ver esto -> cascade = {CascadeType.DETACH, CascadeType.REMOVE}
@JoinColumn(name = "id_bill", nullable = false, unique = true)
private BillEntity bill;

```
---
## #️ ⃣📚**Clase 31: LOMBOK DATA EN ENTIDADES`**

- agregamos @setter , @Getter y hashcode equals en BillEntity y OrderEntity

### NOTA ¿POR QUE ES NECESARIO?
Agregar los métodos `equals` y `hashCode` en las entidades JPA como `OrderEntity` y `BillEntity` es importante por estas razones:

- 🟢 **Identidad de entidad:** Permite comparar correctamente si dos instancias representan el mismo registro en la base de datos (usualmente por el campo `id`).
- 🔄 **Colecciones:** Es necesario para que funcionen bien en colecciones como `Set` o como claves en un `Map`, evitando duplicados y asegurando búsquedas correctas.
- 🛡️ **Integridad en JPA:** Hibernate y JPA usan estos métodos internamente para gestionar el estado de las entidades (persistencia, caché, sincronización).

**Resumen:**  
Implementar `equals` y `hashCode` garantiza que las entidades se comporten correctamente al ser comparadas, almacenadas o gestionadas por JPA y colecciones de Java.

```java

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BillEntity that = (BillEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    
    //--------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    

```
---
## #️ ⃣📚**Clase 32: MANYTOONE ONETOMANY`**

La relación está bien configurada:

## Relación Many-to-One
- **Muchos productos** pueden pertenecer a **una sola orden**
- Desde la perspectiva de `ProductEntity`: `@ManyToOne` hacia `OrderEntity`
- Es la entidad "propietaria" de la relación porque tiene la clave foránea

## JoinColumn
```java
@JoinColumn(name = "id_order")
```
- Crea la columna `id_order` en la tabla `products`
- Esta columna almacena el `id` de la orden a la que pertenece cada producto

## Cascade = CascadeType.ALL
**Cuidado aquí**: Esta configuración puede ser problemática porque:
- Si guardas un `ProductEntity`, también guardará/actualizará la `OrderEntity`
- Si eliminas un `ProductEntity`, también eliminará la `OrderEntity` asociada

## Recomendación
Para esta relación, considera usar:
```java
@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
@JoinColumn(name = "id_order")
private OrderEntity order;
```

Esto evita que eliminar un producto elimine accidentalmente toda la orden y sus otros productos.

## Flujo típico
1. Una orden puede tener múltiples productos
2. Cada producto pertenece a una sola orden
3. La clave foránea `id_order` en `products` referencia el `id` en `orders`

---

### Del lado de order hacemos el mapeo inverso desde OrderEntity mandamos al mappedBy ->

```java
   // Relación uno a muchos con ProductEntity
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProductEntity> products = new ArrayList<>();
```
---
El `mappedBy` en JPA **NO** se refiere al nombre de la tabla, sino al **nombre del atributo** en la entidad del lado opuesto de la relación.

## 🔍 Explicación del `mappedBy = "order"`

En tu código:

```java
@OneToMany(mappedBy = "order", ...)
private List<ProductEntity> products = new ArrayList<>();
```

El `"order"` se refiere al atributo `order` que está definido en la clase `ProductEntity`:

```java
@ManyToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "id_order")
private OrderEntity order; // ← Este es el atributo referenciado
```

## 📋 Resumen de la relación:

- **`OrderEntity`**: Lado propietario de la relación `@OneToMany`
- **`ProductEntity`**: Lado inverso con `@ManyToOne`
- **`mappedBy = "order"`**: Apunta al campo `order` de `ProductEntity`
- **`name = "id_order"`**: Este sí es el nombre de la columna en la tabla `products`

## 🎯 Flujo de la relación:
1. `OrderEntity` tiene muchos `ProductEntity`
2. Cada `ProductEntity` pertenece a un `OrderEntity`
3. La columna `id_order` en la tabla `products` almacena la clave foránea
4. El `mappedBy` conecta ambos lados de la relación bidireccional

--- 

## ¿COMO SE QUIEN ES EL LADO PROPIETARIO?
Excelente pregunta. Te explico cómo identificar el **lado propietario** de una relación JPA:

## 🔍 **Cómo identificar el lado propietario**

### 1. **El lado SIN `mappedBy` es el PROPIETARIO**
```java
// ✅ PROPIETARIO - NO tiene mappedBy
@ManyToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "id_order")
private OrderEntity order;

// ❌ NO PROPIETARIO - SÍ tiene mappedBy  
@OneToMany(mappedBy = "order", ...)
private List<ProductEntity> products;
```

### 2. **El lado con `@JoinColumn` es el PROPIETARIO**
```java
// ✅ PROPIETARIO - tiene @JoinColumn
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "id_bill")
private BillEntity bill;

// ❌ NO PROPIETARIO - tiene mappedBy
@OneToOne(mappedBy = "bill", ...)
private OrderEntity order;
```

## 📋 **Reglas para identificar el propietario**

| Anotación | Lado Propietario | Lado NO Propietario |
|-----------|------------------|---------------------|
| `@OneToOne` | Tiene `@JoinColumn` | Tiene `mappedBy` |
| `@OneToMany` | N/A (raro caso) | Tiene `mappedBy` |
| `@ManyToOne` | **Siempre propietario** | N/A |
| `@ManyToMany` | Sin `mappedBy` | Con `mappedBy` |

## 🎯 **En tu caso específico:**

```java
// ProductEntity - PROPIETARIO
@ManyToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "id_order")  // ← Crea la columna física
private OrderEntity order;

// OrderEntity - NO PROPIETARIO  
@OneToMany(mappedBy = "order", ...)  // ← Solo mapea, no crea columna
private List<ProductEntity> products;
```

## 💡 **Lógica simple:**
- **PROPIETARIO** = Quien tiene la **clave foránea física** en su tabla
- **NO PROPIETARIO** = Quien solo **referencia** la relación usando `mappedBy`

## ⚡ **Truco rápido:**
Si ves `mappedBy = "..."` → Ese lado **NO es propietario**
Si ves `@JoinColumn` → Ese lado **SÍ es propietario**

En relaciones `@ManyToOne`, siempre el lado "Many" es el propietario (tiene la clave foránea).

---

## ¿POR QUE USAR LIST EN ORDERENTITY?
Excelente pregunta. Se usa una **Lista** (`List<ProductEntity>`) en `OrderEntity` porque estamos modelando una relación **uno a muchos** (`@OneToMany`):

## 🔍 **Razón principal:**

```java
// UNA orden puede tener MUCHOS productos
@OneToMany(mappedBy = "order", ...)
private List<ProductEntity> products = new ArrayList<>();
```

## 📋 **Explicación del modelo de negocio:**

- **1 OrderEntity** → **N ProductEntity**
- Una orden puede contener múltiples productos con diferentes cantidades
- Cada producto pertenece a una sola orden

## 🎯 **¿Por qué `List` y no otras colecciones?**

### ✅ **Ventajas de `List`:**
- Permite **duplicados** (si necesitas el mismo producto múltiples veces)
- Mantiene el **orden** de inserción
- **Indexado** por posición
- Compatible con `@OrderColumn` si quieres orden específico

### 🔄 **Alternativas y cuándo usarlas:**

```java
// Set - Sin duplicados, sin orden garantizado
private Set<ProductEntity> products = new HashSet<>();

// List - Con duplicados, con orden
private List<ProductEntity> products = new ArrayList<>();
```

## 💡 **En tu caso específico:**

```java
// ProductEntity tiene quantity, entonces List es perfecto
var product1 = ProductEntity.builder()
    .quantity(BigInteger.ONE)  // ← Cantidad controlada aquí
    .build();
var product2 = ProductEntity.builder()
    .quantity(BigInteger.TWO)  // ← No en la colección
    .build();

var products = List.of(product1, product2); // ← Lista de productos
```

## 🎯 **Resumen:**
Se usa `List` porque una orden puede tener **múltiples productos**,
cada uno con su propia cantidad, y queremos mantener el orden y permitir 
flexibilidad en el manejo de los productos asociados a la orden.

---

## #️ ⃣📚**Clase 33: PROBANDO RELACIONES ONETOMANY**

# ¿PORQUE BUILDER?

```java
 var product1 = ProductEntity.builder()
                .quantity(BigInteger.ONE)
                .build();

```
El patrón **Builder** se usa aquí por varias razones prácticas:

## Ventajas del patrón Builder

### 1. **Flexibilidad en la construcción**
```java
// Con Builder - puedes omitir campos opcionales
var product1 = ProductEntity.builder()
    .quantity(BigInteger.ONE)
    .build();

// Sin Builder - necesitarías constructor con todos los parámetros
var product2 = new ProductEntity(null, BigInteger.ONE, null, null, null);
```

### 2. **Legibilidad del código**
El Builder hace el código **más legible** porque:
- Especificas explícitamente qué campo estás asignando
- No dependes del orden de los parámetros del constructor
- Evitas constructores con muchos parámetros

### 3. **Inmutabilidad parcial**
```java
// Puedes crear objetos paso a paso
var productBuilder = ProductEntity.builder()
    .quantity(BigInteger.ONE);

// Agregar más campos según condiciones
if (someCondition) {
    productBuilder.name("Producto especial");
}

var product = productBuilder.build();
```

### 4. **Integración con Lombok**
Tu entidad probablemente usa `@Builder` de Lombok:
```java
@Entity
@Builder
public class ProductEntity {
    // campos...
}
```

## Alternativas sin Builder
```java
// Constructor tradicional (menos legible)
var product = new ProductEntity();
product.setQuantity(BigInteger.ONE);

// O constructor con parámetros (menos flexible)
var product = new ProductEntity(BigInteger.ONE);
```

El Builder es especialmente útil cuando tienes **entidades con muchos campos** y solo necesitas inicializar algunos de ellos.

---
## explicacion del codigo  PROBANDO RELACIONES OneToMany

```java
// ************* PROBANDO RELACIONES OneToMany *************
        var order = this.orderRepository.findById(1L).orElseThrow();

        var product1 = ProductEntity.builder()
                .quantity(BigInteger.ONE)
                .build();
        var product2 = ProductEntity.builder()
                .quantity(BigInteger.TWO)
                .build();

        var products = List.of(product1, product2);

        order.setProducts(products);

        products.forEach(product -> product.setOrder(order));

        this.orderRepository.save(order);
```

---

## Explicación del código
El código muestra cómo trabajar con una relación **OneToMany bidireccional** entre `OrderEntity` y `ProductEntity`. Aquí está la explicación paso a paso:

## 1. **Buscar la orden existente**
```java
var order = this.orderRepository.findById(1L).orElseThrow();
```
- Busca una orden con ID = 1 en la base de datos
- `orElseThrow()` lanza excepción si no la encuentra

## 2. **Crear productos usando Builder**
```java
var product1 = ProductEntity.builder()
        .quantity(BigInteger.ONE)
        .build();
var product2 = ProductEntity.builder()
        .quantity(BigInteger.TWO)
        .build();
```
- Crea dos productos con cantidades 1 y 2 respectivamente
- Solo se especifica `quantity`, otros campos quedan por defecto

## 3. **Crear lista de productos**
```java
var products = List.of(product1, product2);
```
- Agrupa los productos en una lista inmutable

## 4. **Establecer relación bidireccional**
```java
order.setProducts(products);  // Lado OrderEntity (OneToMany)
products.forEach(product -> product.setOrder(order));  // Lado ProductEntity (ManyToOne)
```

**Es crucial hacer ambas asignaciones** porque:
- `order.setProducts(products)` → Establece la relación desde la orden hacia los productos
- `product.setOrder(order)` → Establece la relación desde cada producto hacia la orden

### IMPORTANTE COMO ES QUE TIENE setProducts SI NO TENEMOS SETTER EN ORDERENTITY
- Aunque no veas explícitamente un método `setProducts` en `OrderEntity`, es
- porque  utilizando **Lombok** en tu proyecto. Lombok es una biblioteca que genera 
- automáticamente métodos como getters, setters, constructores, etc., en tiempo de 
- compilación mediante anotaciones.

### es como si tuvieras esto implicitamente pero como estamos usando @Setter y @Getter de lombok no es necesario escribirlo manualmente

```java
 public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
```
### Explicacion desde Claud

¡Exacto! Has entendido perfectamente cómo funciona la **generación automática de métodos** con **Lombok** y los principios de **POO**.

## 🔍 **Explicación detallada:**

### 1. **Lombok genera automáticamente los métodos**

```java
@Setter  // ← Esta anotación genera todos los setters
private List<ProductEntity> products = new ArrayList<>();
```

**Lombok automáticamente crea:**
```java
public void setProducts(List<ProductEntity> products) {
    this.products = products;
}
```

### 2. **Convención de nombres Java**

La convención de **JavaBeans** dice:
- Campo: `products` → Método: `setProducts()`
- Campo: `clientName` → Método: `setClientName()`
- Campo: `id` → Método: `setId()`

### 3. **Por eso aparece `setProducts()` en tu IDE**

```java
// Tu campo privado
private List<ProductEntity> products = new ArrayList<>();

// Lombok genera automáticamente (invisible para ti)
public void setProducts(List<ProductEntity> products) { ... }

// Por eso puedes usar:
order.setProducts(products); // ✅ Funciona perfectamente
```

## 🎯 **Otros métodos que Lombok genera automáticamente:**

### Con `@Setter`:
- `setId(Long id)`
- `setClientName(String clientName)`
- `setProducts(List<ProductEntity> products)`
- `setBill(BillEntity bill)`

### Con `@Getter`:
- `getId()`
- `getClientName()`
- `getProducts()`
- `getBill()`

## 💡 **Verificación en tu IDE:**

Cuando escribes `order.set` y presionas **Ctrl+Espacio**, el IDE te muestra todos los métodos
**set** que Lombok generó automáticamente basándose en tus campos privados.

## 🎯 **Resumen:**
Sí, **`setProducts()`** existe porque **Lombok** lo generó automáticamente a partir del campo 
**`private List<ProductEntity> products`** usando la anotación **`@Setter`**. Es pura **magia de Lombok** + **convenciones Java**.

### Explicacion de por que usar el forEach para setear la orden en cada producto

Excelente pregunta. Necesitas el `forEach` porque estás trabajando con una **relación bidireccional** y debes **sincronizar ambos lados** de la relación.

## 🔍 **¿Por qué es necesario el forEach?**

### 1. **Relación bidireccional requiere sincronización manual**

```java
// ✅ LADO 1: OrderEntity → ProductEntity (OneToMany)
order.setProducts(products);

// ✅ LADO 2: ProductEntity → OrderEntity (ManyToOne) 
products.forEach(product -> product.setOrder(order));
```

### 2. **Sin el forEach, la relación queda incompleta**

```java
// ❌ PROBLEMA: Solo un lado conoce la relación
order.setProducts(products);
// Los products NO saben que pertenecen a esta order

// ✅ SOLUCIÓN: Ambos lados conocen la relación
order.setProducts(products);
products.forEach(product -> product.setOrder(order));
```

## 🎯 **¿Por qué JPA/Hibernate necesita esto?**

### Sin sincronización:
```java
order.setProducts(products);
// products[0].getOrder() = null ❌
// products[1].getOrder() = null ❌
```

### Con sincronización:
```java
order.setProducts(products);
products.forEach(product -> product.setOrder(order));
// products[0].getOrder() = order ✅
// products[1].getOrder() = order ✅
```

## 🔄 **Flujo de persistencia:**

1. **JPA verifica el lado propietario** (`ProductEntity` con `@ManyToOne`)
2. **Debe encontrar la referencia `order`** en cada `ProductEntity`
3. **Crea la clave foránea `id_order`** en la tabla `products`

## 💡 **Método helper recomendado:**

```java
// En OrderEntity
public void addProduct(ProductEntity product) {
    this.products.add(product);
    product.setOrder(this);  // ← Sincronización automática
}

// Uso:
order.addProduct(product1);
order.addProduct(product2);
```

## 🎯 **Resumen:**
El `forEach` es necesario porque **JPA requiere que el lado propietario
** (`ProductEntity`) **tenga la referencia completa** (`order`) para crear correctamente 
las claves foráneas en la base de datos. Sin esto, la relación existe solo en un lado y 
JPA no puede persistirla correctamente.

---
¡Exactamente! Has captado perfectamente el problema. Sin el `forEach`, tendrías que hacer el mapeo **manualmente uno por uno**, lo cual es muy tedioso y propenso a errores.

## 🔍 **Comparación práctica:**

### ❌ **Sin forEach (manual y tedioso):**
```java
// Con 2 productos - ya es molesto
order.setProducts(products);
product1.setOrder(order);  // ← Manual
product2.setOrder(order);  // ← Manual

// Con 1000 productos - ¡IMPOSIBLE de mantener!
order.setProducts(products);
product1.setOrder(order);
product2.setOrder(order);
product3.setOrder(order);
// ... 997 líneas más 😱
product1000.setOrder(order);
```

### ✅ **Con forEach (automático y escalable):**
```java
// Funciona igual para 2, 100 o 1000 productos
order.setProducts(products);
products.forEach(product -> product.setOrder(order)); // ← Una sola línea
```

## 🎯 **Ventajas del forEach:**

### 1. **Escalabilidad automática**
```java
// No importa si son 10 o 10,000 productos
var products = createThousandsOfProducts();
order.setProducts(products);
products.forEach(product -> product.setOrder(order)); // ← Siempre una línea
```

### 2. **Menos propenso a errores**
```java
// ❌ Fácil olvidar algún producto manualmente
product1.setOrder(order);
product2.setOrder(order);
// ¿Olvidaste product3? ← Bug silencioso

// ✅ El forEach nunca se olvida de ninguno
products.forEach(product -> product.setOrder(order)); // ← Garantizado
```

### 3. **Código más limpio y mantenible**
```java
// Una línea vs. N líneas
// Fácil de leer y entender
// Fácil de modificar si cambia la lógica
```

## 💡 **Método helper aún mejor:**

Para hacer esto más elegante, puedes crear un método helper:

```java
// En OrderEntity
public void addProducts(List<ProductEntity> products) {
    this.products.addAll(products);
    products.forEach(product -> product.setOrder(this));
}

// Uso súper limpio:
order.addProducts(products); // ← Una sola línea hace todo
```

## 🎯 **Resumen:**
Sí, sin el `forEach` tendrías que setear **cada producto individualmente**, lo cual es:
- ⛔ Tedioso para muchos productos
- 🐛 Propenso a errores (olvidar algunos)
- 📝 Código repetitivo e imposible de mantener

El `forEach` es la **solución elegante y escalable** para relaciones bidireccionales 
con muchos elementos.

## 5. **Persistir cambios**
```java
this.orderRepository.save(order);
```
- Guarda la orden y, debido a `CascadeType.ALL`, también persiste los productos automáticamente

## ⚠️ Punto importante
Sin el paso 4 completo, la relación bidireccional no funcionaría correctamente y podrías tener 
inconsistencias entre el modelo de objetos y la base de datos.

## Resultado
- La orden ID=1 ahora tendrá 2 productos asociados
- En la tabla `products` se crearán registros con `id_order = 1`

![image](/images/11.png)

# ¿THIS?

El `this` en `this.orderRepository.save(order)` se refiere a la **instancia actual de la clase `GadgetPlusApplication`**.

## 🔍 **Explicación detallada:**

### 1. **Contexto de la clase:**
```java
@SpringBootApplication
public class GadgetPlusApplication implements CommandLineRunner {
    
    @Autowired
    private OrderRepository orderRepository; // ← Campo de esta clase
    
    @Override
    public void run(String... args) throws Exception {
        // this = instancia de GadgetPlusApplication
        this.orderRepository.save(order); // ← this.orderRepository
    }
}
```

### 2. **`this` = instancia de `GadgetPlusApplication`:**
```java
// Equivalencia:
this.orderRepository.save(order);
// Es lo mismo que:
GadgetPlusApplication.this.orderRepository.save(order);
```

### 3. **Podrías omitir el `this`:**
```java
// Ambas líneas son equivalentes:
this.orderRepository.save(order); // ✅ Explícito
orderRepository.save(order);      // ✅ Implícito (this se asume)
```

## 🎯 **¿Por qué usar `this` aquí?**

### 1. **Claridad y buenas prácticas:**
```java
// Más claro que estás accediendo a un campo de la clase
this.orderRepository.save(order);
this.billRepository.findAll();
```

### 2. **Evita ambigüedad:**
```java
// Si tuvieras un parámetro con el mismo nombre
public void someMethod(OrderRepository orderRepository) {
    this.orderRepository.save(order); // ← Campo de la clase
    orderRepository.findAll();        // ← Parámetro del método
}
```

## 💡 **Comparación con otros `this`:**

### En `GadgetPlusApplication`:
```java
this.orderRepository // ← this = GadgetPlusApplication
```

### En método helper de `OrderEntity` (si existiera):
```java
// Dentro de OrderEntity
public void addProducts(List<ProductEntity> products) {
    this.products.addAll(products); // ← this = OrderEntity
}
```

## 🎯 **Resumen:**
El `this` en `this.orderRepository.save(order)` se refiere a la **instancia actual 
de `GadgetPlusApplication`** y está accediendo al campo `orderRepository` que fue inyectado 
por Spring con `@Autowired`.

## #️ ⃣📚**Clase 34: `**


</details>

