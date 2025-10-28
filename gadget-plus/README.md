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
> CASCADE TYPE SE ACTIVA CUANDO SE REALIZA UNA OPERACION DE ELIMINACION EN LA ENTIDAD PADRE. AQUI SE ELIMINA TODO TANTO ENTIDAD PADRE
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
@Entity
@Table(name="orders")
@Data
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "client_name", length = 32, nullable = false)
    private String clientName;//no es necesario mapear el guion bajo
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
> 
</details>

