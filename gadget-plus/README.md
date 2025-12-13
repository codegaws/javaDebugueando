## DETALLE DEL PROYECTO GADGETPLUS

<details>
<summary><strong>🚀DETALLE DE LA BASE DE DATOS</strong></summary>
¡Excelente pregunta! **NO, es al revés.** El `CASCADE` funciona en la **dirección de la relación**.

## 🤓**¿Cómo funciona realmente?**

```sql
CREATE TABLE orders
(
    id          BIGSERIAL PRIMARY KEY,
    created_at  TIMESTAMP          NOT NULL,
    client_name VARCHAR(32)        NOT NULL,
    id_bill     VARCHAR(64) UNIQUE NOT NULL,
    FOREIGN KEY (id_bill) REFERENCES bill (id) ON DELETE CASCADE
);
```

### **La relación es:**

- `orders.id_bill` **REFERENCIA** → `bill.id`
- `orders` es la tabla **"hija"**
- `bill` es la tabla **"padre"**

### **¿Qué pasa con CASCADE?**

#### **❌ Lo que NO pasa:**

```sql
-- Si borras una ORDER, el BILL NO se borra
DELETE
FROM orders
WHERE id = 1;
-- ↑ Solo se borra la orden, la factura queda intacta
```

#### **✅ Lo que SÍ pasa:**

```sql
-- Si borras un BILL, todas las ORDERS relacionadas SE BORRAN
DELETE
FROM bill
WHERE id = 'FACT001';
-- ↑ Se borra la factura Y todas las órdenes que referencian esa factura
```

## **Ejemplo práctico:**

### **Datos iniciales:**

```sql
-- Tabla bill (facturas)
bill
:
| id       | total_amount | client_rfc    |
|----------|--------------|---------------|
| FACT001  | 1500.00      | XAXX010101000 |
| FACT002  | 2000.00      | YBYY020202000 |

-- Tabla orders (órdenes)
orders:
| id | created_at          | client_name | id_bill |
|----|---------------------|-------------|---------|
| 1  | 2025-11-15 10:00:00 | Juan Pérez  | FACT001 |
| 2  | 2025-11-15 11:00:00 | Ana García  | FACT001 |
| 3  | 2025-11-15 12:00:00 | Luis López  | FACT002 |
```

### **Escenario 1: Borrar una ORDER**

```sql
DELETE
FROM orders
WHERE id = 1;

-- RESULTADO:
-- ✅ Se borra la orden id=1
-- ✅ La factura FACT001 sigue existiendo
-- ✅ La orden id=2 sigue existiendo (misma factura)
```

### **Escenario 2: Borrar un BILL (con CASCADE)**

```sql
DELETE
FROM bill
WHERE id = 'FACT001';

-- RESULTADO:
-- ✅ Se borra la factura FACT001
-- ✅ Se borran automáticamente las órdenes id=1 y id=2 (CASCADE)
-- ✅ La orden id=3 sigue existiendo (factura FACT002)
```

## **¿Por qué es así el diseño?**

### **Lógica de negocio:**

```sql
-- Una FACTURA puede tener varias ÓRDENES
bill
(1)
←→ (muchos) orders

-- Si cancelo una factura → cancelo todas sus órdenes
-- Si cancelo una orden → la factura puede seguir existiendo
```

## **Otros ejemplos de CASCADE en el script:**

```sql
-- products → orders (si borro orden, se borran sus productos)
FOREIGN KEY (id_order) REFERENCES orders(id) ON DELETE
CASCADE

-- products → products_catalog (si borro del catálogo, se borran de órdenes)
FOREIGN KEY (id_product_catalog) REFERENCES products_catalog(id) ON DELETE
CASCADE
```

## **¿Sin CASCADE qué pasaría?**

```sql
-- Sin CASCADE
DELETE
FROM bill
WHERE id = 'FACT001';
-- ERROR: No se puede borrar porque hay órdenes que lo referencian

-- Tendrías que borrar manualmente:
DELETE
FROM orders
WHERE id_bill = 'FACT001'; -- Primero las órdenes
DELETE
FROM bill
WHERE id = 'FACT001'; -- Después la factura
```

**¿Te queda claro ahora la dirección del CASCADE?** Es siempre **del padre hacia los hijos**. 🔄

## 💡 Nemotécnica simple:

"mappedBy = PADRE"
"@JoinColumn = HIJO"

**¡Tu regla es perfecta para identificar relaciones! ** ✨🤓

¡**EXACTO**! 🎯 **Esa es la regla más confiable.**

---

## ✅ **Regla de oro técnica:**

> **"El que tiene la Foreign Key es PROPIETARIO e HIJO"**

---

## 📊 **En todas tus tablas:**

### **1. orders ↔ products**

```sql
CREATE TABLE products (
    id_product_catalog UUID,     -- ← FK
    id_order BIGINT,             -- ← FK  
    FOREIGN KEY (id_order) REFERENCES orders(id)
);
```

**Resultado:** `products` tiene FK → `products` es HIJO

### **2. orders ↔ bill**

```sql
CREATE TABLE orders (
    id_bill VARCHAR(64),         -- ← FK
    FOREIGN KEY (id_bill) REFERENCES bill(id)
);
```

**Resultado:** `orders` tiene FK → `orders` es HIJO

### **3. products ↔ products_catalog**

```sql
CREATE TABLE products (
    id_product_catalog UUID,     -- ← FK
    FOREIGN KEY (id_product_catalog) REFERENCES products_catalog(id)
);
```

**Resultado:** `products` tiene FK → `products` es HIJO

---

## 🎯 **Traducción directa a JPA:**

### **Patrón constante:**

```java
// El que tiene FK = HIJO (propietario)
@ManyToOne  // o @OneToOne
@JoinColumn(name = "foreign_key_column")    // ← @JoinColumn = HIJO
private ParentEntity parent;

// El que NO tiene FK = PADRE (inverso) 
@OneToMany(mappedBy = "parent")             // ← mappedBy = PADRE
private List<ChildEntity> children;
```

---

## 📋 **Resumen de tu schema:**

| Tabla              | ¿Tiene FK?                          | Rol   | JPA           |
|:-------------------|:------------------------------------|:------|:--------------|
| `products`         | ✅ Sí (id_order, id_product_catalog) | HIJO  | `@JoinColumn` |
| `orders`           | ✅ Sí (id_bill)                      | HIJO  | `@JoinColumn` |
| `bill`             | ❌ No                                | PADRE | `mappedBy`    |
| `products_catalog` | ❌ No                                | PADRE | `mappedBy`    |

---

## 💡 **¿Por qué es la regla más confiable?**

### **La FK determina:**

- ✅ **Dependencia técnica:** El que tiene FK depende del otro
- ✅ **Control de relación:** El que tiene FK puede modificarla
- ✅ **Responsabilidad:** El que tiene FK "posee" la relación

### **Vs criterios conceptuales que pueden confundir:**

- 🤔 "¿Quién es más importante?" → Subjetivo
- 🤔 "¿Quién se crea primero?" → Puede variar
- 🤔 "¿Quién depende de quién?" → A veces ambiguo

---

## ✅ **Tu entendimiento es perfecto:**

**FK = PROPIETARIO = HIJO = @JoinColumn**  
**No FK = INVERSO = PADRE = mappedBy**

**Esta regla técnica nunca falla. ** 🎯✨

</details>
