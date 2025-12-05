# 🚀 Título con emoji
## 💡 Subtítulo

- ✅ Tarea completada
- ❌ Error detectado
- ⚠️ Atención
- 🔧 Configuración
- 🔍 Buscar
- 🐛 Bug
- 🛠️ Reparar
- 📦 Paquete
- 📝 Nota
- ⭐ Favorito
- 🔗 Enlace importante
- 🎯 Objetivo
- 🧪 Pruebas
- ⏳ En progreso
- 🔒 Seguro

Inline: `Fix applied ⚡` → Fix applied ⚡

Usando shortcodes (GitHub):
- `:rocket:` → :rocket:
- `:white_check_mark:` → :white_check_mark:
- `:warning:` → :warning:

Badges con shields.io:
- `![build](https://img.shields.io/badge/build-passing-brightgreen)`
- `![license](https://img.shields.io/badge/license-MIT-blue)`

Icono como imagen (control de tamaño):
- `<img src="https://img.icons8.com/ios-glyphs/30/000000/settings.png" alt="settings" width="18"/> Configuración`

Tabla con iconos:
| Estado | Icono |
|---|---|
| OK | ✅ |
| Fallo | ❌ |
| Pendiente | ⏳ |

Consejo: usa emojis Unicode para simplicidad o shortcodes en GitHub; para control de estilo usa imágenes o badges.

```sql
-- queries/database_operations.sql
-- Consultas para debugging y administración de la base de datos

-- Verificar estructura de la tabla products
SELECT column_name,
       data_type,
       is_nullable
FROM information_schema.columns
WHERE table_name = 'products';

-- Verificar relaciones de productos
SELECT p.id, p.id_order
FROM PRODUCTS p;

-- Join entre orders y products
SELECT *
FROM orders o
         JOIN products p ON o.id = p.id_order;

-- Actualizar relación (para testing)
UPDATE products
SET id_order=null
WHERE id = 3;

-- Limpiar datos de prueba
DELETE FROM products;

-- Verificar estado de las tablas
SELECT * FROM products;
SELECT * FROM orders;

-- Reiniciar secuencias (útil para testing)
TRUNCATE TABLE products;
ALTER SEQUENCE products_id_seq RESTART WITH 1;


```

**Correcto**, es una **muy buena práctica** evitar `CascadeType.ALL` en la relación `@ManyToOne` (lado hijo/no propietario).

## **¿Por qué no usar `CascadeType.ALL` en `@ManyToOne`?**

### **Problema principal: Eliminación en cascada peligrosa**

```java
// Con cascade = CascadeType.ALL en @ManyToOne
@ManyToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "id_order")
private OrderEntity order;
```

**Consecuencias peligrosas:**

```java
// Si eliminas UN producto...
productRepository.delete(product);

// Se elimina la ORDEN COMPLETA y TODOS sus productos
// ¡Perdiste datos que no querías perder!
```

### **Regla general recomendada:**

- **`@OneToMany` (lado padre):** ✅ Usa `cascade` cuando sea necesario
- **`@ManyToOne` (lado hijo):** ❌ Evita `cascade`, especialmente `ALL` y `REMOVE`

## **Configuración recomendada:**

```java
// En OrderEntity (lado padre) - SÍ usar cascade
@OneToMany(mappedBy = "order", 
           cascade = CascadeType.ALL, 
           orphanRemoval = true)
private List<ProductEntity> products;

// En ProductEntity (lado hijo) - NO usar cascade
@ManyToOne
@JoinColumn(name = "id_order")
private OrderEntity order;
```

## **¿Cuándo sí usar cascade en `@ManyToOne`?**

Solo en casos muy específicos como `PERSIST`:

```java
@ManyToOne(cascade = CascadeType.PERSIST)
@JoinColumn(name = "id_order")
private OrderEntity order;
```

Esto permite guardar la orden automáticamente al guardar el producto, pero **sin** eliminar la orden al eliminar el producto.

## **Resumen:**
- ✅ **Lado padre (`@OneToMany`):** Controla el ciclo de vida de los hijos
- ❌ **Lado hijo (`@ManyToOne`):** No debe controlar el ciclo de vida del padre
- Tu comentario `"NO ES BUENO ALL por que borra todo"` es **100% correcto**