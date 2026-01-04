-- queries/database_operations.sql
-- Consultas para debugging y administración de la base de datos

-- Verificar estructura de la tabla products
SELECT column_name,
       data_type,
       character_maximum_length,
       is_nullable,
       column_default
FROM information_schema.columns
WHERE table_name = 'products_catalog';

-- Verificar relaciones de productos
SELECT p.id, p.id_order
FROM products p;

-- Join entre orders y products
SELECT *
FROM orders o
         JOIN products p ON o.id = p.id_order;

-- Actualizar relación (para testing)
UPDATE products
SET id_order=null
WHERE id = 3;

-- Limpiar datos de prueba
DELETE
FROM products;

DELETE
FROM product_join_category;

-- Verificar estado de las tablas
SELECT *
FROM products;
SELECT *
FROM orders;

-- Reiniciar secuencias (útil para testing)
TRUNCATE TABLE products;
ALTER SEQUENCE products_id_seq RESTART WITH 1;

-- Verificar join completo entre products, products_catalog y orders
SELECT *
FROM products p
         join products_catalog pc ON pc.id = p.id_product_catalog
         join orders o on o.id = p.id_order;

SELECT column_name,
       data_type,
       character_maximum_length,
       is_nullable,
       column_default
FROM information_schema.columns
WHERE table_name = 'products_catalog';

SELECT *
FROM products_catalog p -- Tabla principal: productos
         JOIN product_join_category pc ON pc.id_product = p.id -- Unir con tabla puente
         JOIN categories c ON pc.id_category = c.id;
-- Unir con categorías

-- Validacion
SELECT *
FROM product_join_category;
SELECT *
FROM products_catalog; -- desc -> home = HOME // office = OFFICE // desc = home y office = HOME //OFFICE
SELECT *
FROM categories;

-- Ver el producto segun su  categoría (salen dos categorias mismo producto HOME / OFFICE)

SELECT *
FROM products_catalog p
         join product_join_category pc ON pc.id_product = p.id
         join categories c ON pc.id_category = c.id
WHERE p.id = 'b927287f-d410-4134-a5cd-c2968b346c70';

-- Ver tabla puente
SELECT *
FROM product_join_category;

-- Ver el producto una sola vez con categorías como string
SELECT p.id,
       p.product_name,
       p.brand_name,
       STRING_AGG(c.description, ', ') AS categories
FROM products_catalog p
         JOIN product_join_category pc ON pc.id_product = p.id
         JOIN categories c ON pc.id_category = c.id
WHERE p.id = 'b927287f-d410-4134-a5cd-c2968b346c70'
GROUP BY p.id, p.product_name, p.brand_name;

-- PROBANDO
select *
from products p
         join orders o on p.id_order = o.id
         join products_catalog pc on pc.id = p.id_product_catalog;
--PROBANDO REJECT PRODUCTS

select *
from reject_products;

insert into reject_products(product_name, brand_name, quantity)
values ('Galaxy S24 plus', 'Samsung', 10);

-- CONSIDERACIONES DE LA SECCION 51
select *
from product_join_category pjc
         join public.categories c on c.id = pjc.id_category
         join public.products_catalog p on p.id = pjc.id_product;

select o.client_name, pc.product_name, p.quantity
from products p
         join orders o on p.id_order = o.id
         join products_catalog pc on pc.id = p.id_product_catalog;

-- PRUEBAS CLASE 55
select *
from products_catalog;

select *
from products_catalog
where product_name = 'Pc gamer';

-- PRUEBAS CLASE 56-57-58-59 OPERADOR LIKE
select *
from products_catalog pc
where pc.product_name like '%65';

select *
from products_catalog pc
where pc.price between 100 and 1000;

-- INDICES CLASE 61
create index idx_product_name on products_catalog (product_name);


-- CLASE 63

SELECT p.*, c.*
FROM products_catalog p
         LEFT JOIN product_join_category pjc ON p.id = pjc.id_product
         LEFT JOIN categories c ON pjc.id_category = c.id
WHERE c.id = :categoryId;

-- joins con JPQL
select *
from categories;

SELECT *
FROM products_catalog pc -- Tabla principal: productos
         JOIN product_join_category pjc ON pjc.id_product = pc.id -- Unir con tabla puente
         JOIN categories c ON pjc.id_category = c.id
where c.id = 1;
-- trae todo lo que es HOME

-- CLASE 65 TRABAJANDO CON FECHAS
select *
from products_catalog
where launching_date > '2017-01-01';

-- CLASE 66 TRABAJANDO OPERADOR AND

select *
from products_catalog
where brand_name = 'Apple'
  and rating > 5;

-- CLASE 67 OR
SELECT *
FROM products_catalog pc
WHERE pc.rating > 5
   OR pc.brand_name = 'LG';

-- CLASE 68 GROUPBY
SELECT pc.brand_name,
       AVG(pc.price) as average,
       SUM(pc.price) as totalPrice
from products_catalog pc
GROUP BY pc.brand_name;

--SONY PROMEDIO, TOTALPRECIOS
-- SAMSUNG PROMEDIO, TOTALPRECIOS
-- LG PROMEDIO, TOTALPRECIOS
-- APPLE PROMEDIO, TOTALPRECIOS

-- CLASE 70 PAGINAR LIMIT OFFSET
SELECT pc.product_name, pc.price, pc.brand_name
FROM products_catalog pc
ORDER BY pc.price desc
LIMIT 5 OFFSET 0;


SELECT *
FROM products_catalog
WHERE launching_date < '2024-01-01';

--  clase 74 procedimientos almacenados--**************************************************
CREATE OR REPLACE PROCEDURE count_total_products_by_brand(IN brand VARCHAR, OUT response INTEGER)
    LANGUAGE plpgsql
AS
$$
BEGIN
    SELECT COUNT(*)
    INTO response
    FROM products_catalog
    WHERE brand_name = brand
    GROUP BY brand_name;
END;
$$;
--llamas
CALL count_total_products_by_brand('Lenovo', null);
--**************************************************
-- esto es lo que haria el procedimiento almacenado

-- haciendo pruebas
SELECT COUNT(*) AS total
FROM products_catalog
WHERE brand_name = 'Amazon'
GROUP BY brand_name;
-- rpta 4
--**************************************************
SELECT *
FROM products_catalog;

-- clase 81 read2
select *
from orders;

SELECT *
FROM orders
         join products p on orders.id = p.id_order
where orders.id = 1

--clase 86 create
SELECT *
FROM orders o
         JOIN bill b ON o.id_bill = b.id
         JOIN products p ON o.id = p.id_order
WHERE o.id = 21;

SELECT *
FROM bill;

-- revisar estructura de la tabla products_catalog
SELECT column_name,
       data_type,
       character_maximum_length,
       is_nullable,
       column_default
FROM information_schema.columns
WHERE table_name = 'products_catalog';