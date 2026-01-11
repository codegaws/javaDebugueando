# 🎯 🎯 Transacciones 🎯 🎯

![image](images/70.png)
![image](images/img.png)
![image](images/img_1.png)
![image](images/img_2.png)
![image](images/img_3.png)
![image](images/img_4.png)
![image](images/img_5.png)
![image](images/img_6.png)
![image](images/img_7.png)
![image](images/img_8.png)

----

### ⃣📚**Clase 96:DEFINIENDO SERVICIOS****

```java
public interface TransactionService {
    void executeTransaction(Long id);

    void updateOrder(Long id);

    void updateBill(String id);

    void validProducts(Long id);
}
```
- Implementan en TransactionServiceImpl

---
### ⃣📚**Clase 97:Implementando Servicios****
# 📚 Análisis del Código - Transacciones en Spring Boot JPA

¡Perfecto para aprender! Te explico cada parte como si fueras principiante en JPA 🚀

---

## 🏗️ **Clase de Servicio - `TransactionServiceImpl`**

### **📋 Anotaciones de la clase**

```java
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
```

| Anotación | ¿Qué hace? | ¿Por qué se usa? |
|-----------|------------|------------------|
| `@Service` | Marca esta clase como un **servicio de Spring** | Spring la registra como un **Bean** y la puede inyectar en otros componentes |
| `@RequiredArgsConstructor` | **Lombok** crea automáticamente el constructor | Evita escribir el constructor manualmente para los campos `final` |

---

## 💉 **Inyección de Dependencias**

```java
private final OrderRepository orderRepository;
private final BillRepository billRepository;
```

### **🔍 ¿Qué significa esto?**

- **`final`**: Las variables no pueden cambiar una vez inicializadas
- **Lombok** crea este constructor automáticamente:

```java
// Esto es lo que Lombok genera internamente:
public TransactionServiceImpl(OrderRepository orderRepository, 
                             BillRepository billRepository) {
    this.orderRepository = orderRepository;
    this.billRepository = billRepository;
}
```

---

## 🔄 **Método Principal - `executeTransaction()`**

```java
@Override
public void executeTransaction(Long id) {
    this.updateOrder(id);
}
```

### **📝 ¿Qué hace?**

- Es el **punto de entrada** de la transacción
- Solo llama a `updateOrder(id)`
- **⚠️ PROBLEMA**: ¡No tiene `@Transactional`!

---

## 🛠️ **Método Core - `updateOrder()`**

```java
@Override
public void updateOrder(Long id) {
    final var order = orderRepository.findById(id).orElseThrow();
    order.setCreatedAt(LocalDateTime. now());
    orderRepository.save(order);
    this.validProducts(id);
    this.updateBill(order. getBill().getId());
}
```

### **🔍 Desglose paso a paso:**

#### **1️⃣ Buscar la orden**
```java
final var order = orderRepository.findById(id).orElseThrow();
```
- `findById(id)` devuelve un `Optional<Order>`
- `orElseThrow()` lanza excepción si no encuentra la orden
- **En JPA**:  Ejecuta `SELECT * FROM orders WHERE id = ? `

#### **2️⃣ Modificar la entidad**
```java
order.setCreatedAt(LocalDateTime.now());
```
- Cambia la fecha de creación (¿raro verdad?  🤔)
- La entidad está en estado **MANAGED** por el EntityManager

#### **3️⃣ Guardar cambios**
```java
orderRepository.save(order);
```
- **En JPA**: Ejecuta `UPDATE orders SET created_at = ?  WHERE id = ?`
- Como la entidad ya existía, hace `UPDATE` no `INSERT`

#### **4️⃣ Validar productos**
```java
this.validProducts(id);
```
- Llama a método de validación
- Si falla, lanza excepción

#### **5️⃣ Actualizar factura**
```java
this.updateBill(order.getBill().getId());
```
- Actualiza la factura relacionada
- Usa `order.getBill()` - **relación JPA**

---

## 📄 **Método de Actualización - `updateBill()`**

```java
@Override
public void updateBill(String id) {
    final var bill = billRepository. findById(id).orElseThrow();
    bill.setClientRfc("TRANS123");
    billRepository.save(bill);
}
```

### **🔍 ¿Qué hace?**
- Busca la factura por ID (tipo `String`)
- Cambia el RFC del cliente a valor fijo
- Guarda los cambios
- **En JPA**: `UPDATE bills SET client_rfc = 'TRANS123' WHERE id = ?`

---

## ✅ **Método de Validación - `validProducts()`**

```java
@Override
public void validProducts(Long id) {
    final var order = orderRepository. findById(id).orElseThrow();
    if (order.getProducts().isEmpty()) {
        throw new IllegalStateException("there are no products in the order");
    }
}
```

### **🔍 ¿Qué hace?**
- **Vuelve a buscar** la orden (¡ineficiente!)
- Verifica si tiene productos:  `order.getProducts()`
- Si está vacía, lanza `IllegalStateException`

---

## 🌐 **Controlador REST - `TransactionController`**

```java
@RestController
@RequestMapping(path = "transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Map<String, String>> startTransaction(@RequestParam Long id) {
        this.transactionService.executeTransaction(id);
        return ResponseEntity.ok(Map.of("Transaction", "ok"));
    }
}
```

### **📝 Desglose:**

| Anotación | Función |
|-----------|---------|
| `@RestController` | Combina `@Controller` + `@ResponseBody` |
| `@RequestMapping(path = "transaction")` | Todas las rutas empiezan con `/transaction` |
| `@PostMapping` | Mapea POST a `/transaction` |
| `@RequestParam Long id` | Obtiene parámetro `id` del request |

### **📡 Endpoint resultante:**
```http
POST /transaction? id=123
```

---

## ⚠️ **PROBLEMAS IMPORTANTES del código**

### **🚨 1. Sin `@Transactional`**

```java
// ❌ MALO:  Sin transacción
public void executeTransaction(Long id) {
    this. updateOrder(id); // Si falla, cambios previos NO se revierten
}

// ✅ CORRECTO: Con transacción
@Transactional
public void executeTransaction(Long id) {
    this.updateOrder(id); // Si falla, TODO se revierte
}
```

### **🚨 2. Consultas duplicadas**

```java
// ❌ INEFICIENTE:  Busca la orden 2 veces
public void updateOrder(Long id) {
    final var order = orderRepository. findById(id).orElseThrow(); // 1ra vez
    // ... 
    this.validProducts(id); // Vuelve a buscar la misma orden
}
```

### **🚨 3. Sin manejo de errores**

```java
// ❌ Sin try-catch en el controlador
@PostMapping
public ResponseEntity<Map<String, String>> startTransaction(@RequestParam Long id) {
    this.transactionService. executeTransaction(id); // ¿Y si falla? 
    return ResponseEntity. ok(Map.of("Transaction", "ok"));
}
```

---

## ✅ **Versión CORREGIDA**

### **🔧 Servicio mejorado:**

```java
@Service
@RequiredArgsConstructor
@Transactional // ← ¡Importante!
public class TransactionServiceImpl implements TransactionService {

    private final OrderRepository orderRepository;
    private final BillRepository billRepository;

    @Override
    public void executeTransaction(Long id) {
        final var order = orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        
        // Validar ANTES de modificar
        validProducts(order);
        
        // Actualizar orden
        order.setCreatedAt(LocalDateTime.now());
        
        // Actualizar factura
        updateBill(order. getBill());
        
        // Spring JPA guarda automáticamente al final de la transacción
    }
    
    private void validProducts(OrderEntity order) { // ← Recibe la entidad
        if (order.getProducts().isEmpty()) {
            throw new IllegalStateException("Order has no products");
        }
    }
    
    private void updateBill(BillEntity bill) { // ← Recibe la entidad
        bill.setClientRfc("TRANS123");
    }
}
```

### **🌐 Controlador mejorado:**

```java
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<? > startTransaction(@RequestParam Long id) {
        try {
            transactionService.executeTransaction(id);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity. notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}
```

---

## 📚 **Conceptos clave aprendidos:**

| Concepto | Explicación |
|----------|-------------|
| **`@Service`** | Marca una clase como servicio de Spring |
| **`@Transactional`** | Agrupa operaciones en una transacción |
| **EntityManager** | JPA maneja automáticamente el ciclo de vida |
| **Repository. save()** | INSERT si es nueva, UPDATE si existe |
| **Optional. orElseThrow()** | Manejo seguro de valores nulos |

---

## 🎯 **¿Qué sigue en tu aprendizaje? **

1. **Propagación de transacciones** (`REQUIRED`, `REQUIRES_NEW`)
2. **Rollback strategies**
3. **Lazy vs Eager loading**
4. **Query optimization**

¡Excelente código para aprender! 🚀 ¿Qué parte quieres que profundice más? 

### ⃣📚**Clase 97:Implementando Servicios****

```java
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {


    private final OrderRepository orderRepository;
    private final BillRepository billRepository;

    @Override
    public void executeTransaction(Long id) {
        this.updateOrder(id);
    }

    @Override
    public void updateOrder(Long id) {
        final var order = orderRepository.findById(id).orElseThrow();
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
        this.validProducts(id);
        this.updateBill(order.getBill().getId());
    }

    //CREAMOS ESTOS METODOS PARA SER LLAMADOS DENTRO DE LA TRANSACCION DE UPDATEORDER
    @Override
    public void updateBill(String id) {
        final var bill = billRepository.findById(id).orElseThrow();
        bill.setClientRfc("trc34");
        billRepository.save(bill);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void validProducts(Long id) {
        entityManager.clear();
        final var order = orderRepository.findById(id).orElseThrow();
        if (order.getProducts().isEmpty()) {
            throw new IllegalArgumentException("there are no products in the order");
        }
    }
```

- Aqui se quita el id order al id de producto 

---

### ⃣📚**Clase 98:Explicacion de Querys****

- Cambia el id order por que es aleatorio
- 
```sql
SELECT o.client_name, o.created_at, b.client_rfc
FROM orders o
    JOIN bill b ON o.id_bill = b.id
WHERE o.id = 3;

SELECT o.client_name, o.created_at, o.id AS id_order, p.id AS id_product, p.id_product_catalog id_catalog
FROM orders o JOIN products p ON o.id = p.id_order
WHERE o.id = 3;

UPDATE products set id_order = null WHERE id = 4;

UPDATE products set id_order = 3 WHERE id = 4;
```

- ejecutando el proyecto y probando en postman

![image](images/img_9.png)

### ⃣📚**Clase 99:Manejo de Excepciones en transacciones****

- Aqui pequeño detalle se agrego esto para actualizar la cache de Hibernate por que se habia quedado pegado

```java
  @PersistenceContext
    private EntityManager entityManager;
    
     entityManager.clear();
```

- queda asi

```java
    @Override
    public void validProducts(Long id) {
        entityManager.clear();
        final var order = orderRepository.findById(id).orElseThrow();
        if (order.getProducts().isEmpty()) {
            throw new IllegalArgumentException("there are no products in the order");
        }
    }
```

- enviamos en postman no sale el 505 de error y validamos que no se ha refrescaso el rfc en la tabla
- por que no se pudo realizar la validacion  solo se modifico la fecha y hora

![image](images/img_10.png)

### ⃣📚**Clase 100:Propagation Required****🛠️🛠️🛠️
- En TransactionServiceImpl -> 🛠️🛠️🛠️🛠️
```java
@Transactional
    @Override
    public void executeTransaction(Long id) {
        log.info("TRANSACTION ACTIVE {}", TransactionSynchronizationManager.isActualTransactionActive());
        log.info("TRANSACTION NAME {}", TransactionSynchronizationManager.getCurrentTransactionName());
        this.updateOrder(id);
    }
```
#### Ver como se propaga -> REQUIRED

![image](images/img_11.png)

