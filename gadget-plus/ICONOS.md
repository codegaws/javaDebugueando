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