## INFORMACION

<details>
<summary><strong>SESSION 3 MAPEO DE ENTIDADES</strong></summary>
## RELACION DE LA TABLA 

![image](/images/tabla.png)

Basándome en tu esquema SQL, aquí está el flujo y relaciones de tus tablas:

## Flujo de Relaciones

### **Tablas Principales y sus Relaciones:**

**1. BILL (Factura) - Tabla Principal**
- **PK**: `id` (VARCHAR(64))
- **Campos**: `total_amount`, `client_rfc`

**2. ORDERS (Órdenes) - 1:1 con BILL**
- **PK**: `id` (BIGSERIAL)
- **FK**: `id_bill` → `bill(id)` (UNIQUE, CASCADE)
- **Relación**: Una orden tiene una factura única

**3. PRODUCTS_CATALOG (Catálogo de Productos) - Tabla de Referencia**
- **PK**: `id` (UUID)
- **Campos**: `product_name` (UNIQUE), `brand_name`, `description`, `price`, etc.

**4. PRODUCTS (Productos de Orden) - N:1 con ORDERS y CATALOG**
- **PK**: `id` (BIGSERIAL)
- **FK**: `id_order` → `orders(id)` (CASCADE)
- **FK**: `id_product_catalog` → `products_catalog(id)` (CASCADE)
- **Relación**: Una orden puede tener múltiples productos

**5. CATEGORIES (Categorías) - Tabla de Clasificación**
- **PK**: `id` (BIGSERIAL)
- **Campos**: `code` (ENUM), `description`

**6. PRODUCT_JOIN_CATEGORY (Tabla de Unión) - N:M**
- **FK**: `id_category` → `categories(id)` (CASCADE)
- **FK**: `id_product` → `products_catalog(id)` (CASCADE)
- **Relación**: Un producto puede tener múltiples categorías

### **Flujo del Proceso:**
1. **BILL** ← (1:1) → **ORDERS** ← (1:N) → **PRODUCTS**
2. **PRODUCTS** → (N:1) → **PRODUCTS_CATALOG**
3. **PRODUCTS_CATALOG** ← (N:M) → **CATEGORIES** (via tabla de unión)

### **Cardinalidades:**
- Bill → Orders: **1:1**
- Orders → Products: **1:N**
- Products_Catalog → Products: **1:N**
- Products_Catalog ↔ Categories: **N:M**
## Significado del Esquema de Base de Datos

Tu esquema representa un **sistema de comercio electrónico** con las siguientes funcionalidades:

### **Flujo del Negocio:**

**1. Gestión de Pedidos:**
- Un cliente hace un **pedido** (`orders`) que genera una **factura** (`bill`)
- Cada pedido está vinculado a una factura única (relación 1:1)
- La factura contiene el RFC del cliente y monto total

**2. Productos y Catálogo:**
- Existe un **catálogo de productos** (`products_catalog`) con información detallada
- Cada producto del catálogo puede estar en múltiples pedidos
- Los **productos específicos** (`products`) representan items en un pedido con cantidad

**3. Sistema de Categorización:**
- Los productos se clasifican en **categorías** (`categories`)
- Un producto puede pertenecer a múltiples categorías (N:M)
- Las categorías tienen códigos: `HOME`, `OFFICE`, `NONE`

### **Características Técnicas:**

**Tipos de Datos:**
- `UUID` para productos del catálogo (identificadores únicos globales)
- `BIGSERIAL` para IDs autoincrementales
- `ENUM` personalizado para códigos de categoría
- `CASCADE` en todas las relaciones para integridad referencial

**Funcionalidades:**
- **Función**: `random_date()` - genera fechas aleatorias
- **Procedimiento**: `count_total_products_by_brand()` - cuenta productos por marca

### **Propósito:**
Es un sistema para manejar ventas online donde:
- Se registran pedidos con múltiples productos
- Se generan facturas automáticamente
- Se mantiene un catálogo organizado por categorías
- Se controla inventario y precios centralizadamente

---
¡Perfecto! Te explico esta estructura de base de datos paso a paso. Es un sistema de **e-commerce/tienda online** con un diseño bastante completo.

## 🔧 Configuraciones Iniciales

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```
- Habilita la extensión para generar UUIDs automáticamente

```sql
CREATE TYPE code AS ENUM ('HOME', 'OFFICE', 'NONE');
```
- Define un tipo de dato personalizado con 3 valores posibles

```sql
CREATE OR REPLACE FUNCTION random_date(days INTEGER) RETURNS DATE
```
- Función para generar fechas aleatorias (útil para datos de prueba)

## 📊 Estructura de Tablas y Relaciones

### 1. **BILL** (Facturas)
```sql
CREATE TABLE bill (
    id VARCHAR(64) PRIMARY KEY,           -- ID único de la factura
    total_amount NUMERIC NULL,            -- Monto total (puede ser null inicialmente)
    client_rfc VARCHAR(14) NOT NULL       -- RFC del cliente (México)
);
```

### 2. **ORDERS** (Órdenes)
```sql
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,             -- ID autoincrementable
    created_at TIMESTAMP NOT NULL,        -- Fecha de creación
    client_name VARCHAR(32) NOT NULL,     -- Nombre del cliente
    id_bill VARCHAR(64) UNIQUE NOT NULL,  -- Referencia ÚNICA a la factura
    FOREIGN KEY (id_bill) REFERENCES bill(id) ON DELETE CASCADE
);
```

### 3. **PRODUCTS_CATALOG** (Catálogo de Productos)
```sql
CREATE TABLE products_catalog (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,  -- UUID automático
    product_name VARCHAR(64) NOT NULL UNIQUE,       -- Nombre único del producto
    brand_name VARCHAR(64) NOT NULL,                -- Marca
    description VARCHAR(255) NOT NULL,              -- Descripción
    price NUMERIC NOT NULL,                         -- Precio
    rating SMALLINT,                                -- Calificación (1-5)
    launching_date DATE,                            -- Fecha de lanzamiento
    is_discount BOOLEAN                             -- ¿Tiene descuento?
);
```

### 4. **PRODUCTS** (Productos en Órdenes)
```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    quantity INT DEFAULT 1,                         -- Cantidad pedida
    id_product_catalog UUID,                        -- Referencia al catálogo
    id_order BIGINT,                               -- Referencia a la orden
    FOREIGN KEY (id_order) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (id_product_catalog) REFERENCES products_catalog(id) ON DELETE CASCADE
);
```

### 5. **CATEGORIES** (Categorías)
```sql
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    code code DEFAULT 'NONE',              -- Usa el ENUM definido
    description VARCHAR(255) NOT NULL
);
```

### 6. **PRODUCT_JOIN_CATEGORY** (Tabla de Unión)
```sql
CREATE TABLE product_join_category (
    id_category BIGINT,                    -- Referencia a categoría
    id_product UUID,                       -- Referencia a producto del catálogo
    FOREIGN KEY (id_category) REFERENCES categories(id) ON DELETE CASCADE,
    FOREIGN KEY (id_product) REFERENCES products_catalog(id) ON DELETE CASCADE
);
```

## 🔄 Diagrama de Relaciones

```
BILL (1) ←→ (1) ORDERS (1) ←→ (N) PRODUCTS (N) ←→ (1) PRODUCTS_CATALOG
                                                            ↓
                                                    PRODUCT_JOIN_CATEGORY
                                                            ↓
                                                       CATEGORIES
```

## 📈 Relaciones Explicadas

### 1. **BILL ↔ ORDERS** (1:1)
- Cada factura tiene **una sola orden**
- Cada orden tiene **una sola factura**

### 2. **ORDERS ↔ PRODUCTS** (1:N)
- Una orden puede tener **múltiples productos**
- Cada producto pertenece a **una sola orden**

### 3. **PRODUCTS_CATALOG ↔ PRODUCTS** (1:N)
- Un producto del catálogo puede estar en **múltiples órdenes**
- Cada item de una orden referencia **un producto del catálogo**

### 4. **PRODUCTS_CATALOG ↔ CATEGORIES** (N:M)
- Un producto puede estar en **múltiples categorías**
- Una categoría puede tener **múltiples productos**
- Se usa la tabla intermedia `product_join_category`

## 🔧 Procedimiento Almacenado

```sql
CREATE OR REPLACE PROCEDURE count_total_products_by_brand(IN brand VARCHAR, OUT response INTEGER)
```
- Cuenta cuántos productos hay de una marca específica

## 💡 Casos de Uso Típicos

1. **Cliente hace un pedido**:
    - Se crea un `BILL`
    - Se crea un `ORDER` vinculado al bill
    - Se agregan `PRODUCTS` a la orden

2. **Gestión de inventario**:
    - `PRODUCTS_CATALOG` mantiene el catálogo
    - `CATEGORIES` organiza los productos

¿Te gustaría que profundice en alguna parte específica o tienes dudas sobre alguna relación?
</details>