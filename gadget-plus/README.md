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

</details>

