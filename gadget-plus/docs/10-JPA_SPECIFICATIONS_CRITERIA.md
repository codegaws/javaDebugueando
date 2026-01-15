## ⃣📚**ssJPA SPECIFCATIONS Y CRITERIA⏭️⏭️ ****

### JPA CRITERIA API

![IMG](images/img_24.png)

![IMG](images/img_25.png)

![IMG](images/img_26.png)

## ⃣📚**Clase 123:EXPLICACION DEL REQUERIMIENTO DEL PDF ⏭️⏭️ ****

```sql

-- Parámetros: brand = 'Amazon', hasDiscount = true
SELECT DISTINCT pc.*
FROM products_catalog pc
WHERE pc.brand_name = 'Amazon'
  AND pc.is_discount = true
ORDER BY pc.rating DESC, pc.price ASC
LIMIT 20 OFFSET 0;
-- Resultado esperado: 2 productos
-- • Alexa super (200.89, rating: 4)
-- • Alexa small (500.89, rating: 1)
```
#### explicacion :
Exacto, estás correcto pero hay una pequeña precisión:

**LIMIT 20 OFFSET 0** significa:
- **LIMIT 20**: máximo 20 filas en el resultado
- **OFFSET 0**: saltar 0 filas desde el inicio (comenzar desde la primera fila)

**Para paginación:**
- `OFFSET 0` = página 1 (filas 1-20)
- `OFFSET 20` = página 2 (filas 21-40)
- `OFFSET 40` = página 3 (filas 41-60)

**Fórmula común:**
```sql
LIMIT [tamaño_página] OFFSET [página * tamaño_página - tamaño_página]
```

**Ejemplos con páginas de 20 elementos:**
```sql
-- Página 1
LIMIT 20 OFFSET 0

-- Página 2  
LIMIT 20 OFFSET 20

-- Página 3
LIMIT 20 OFFSET 40
```

En tu caso con `LIMIT 20 OFFSET 0`, obtienes la **primera página** con máximo 20 resultados.

---

### Explicacion sobre 128 combinaciones (2^7) en el contexto del patrón Specification


Analizando el documento, veo que el problema se refiere a un sistema de búsqueda con **7 criterios opcionales**. Te explico de dónde salen las 128 combinaciones:

## ¿De dónde sale 2^7 = 128?

Cada criterio de búsqueda puede estar en **2 estados**:
- ✅ **Presente** (se usa en la búsqueda)
- ❌ **Ausente** (no se usa en la búsqueda)

Con 7 criterios independientes: 2 × 2 × 2 × 2 × 2 × 2 × 2 = 2^7 = **128 combinaciones**

## Ejemplos de combinaciones posibles:

Supongamos que los 7 criterios son: `nombre`, `edad`, `ciudad`, `profesión`, `salario`, `experiencia`, `estado`

### Algunas de las 128 combinaciones:

1. **Ningún criterio** (búsqueda general)
   ```sql
   SELECT * FROM empleados
   ```

2. **Solo nombre**
   ```sql
   SELECT * FROM empleados WHERE nombre = ? 
   ```

3. **Solo edad**
   ```sql
   SELECT * FROM empleados WHERE edad = ?
   ```

4. **Nombre + edad**
   ```sql
   SELECT * FROM empleados WHERE nombre = ? AND edad = ?
   ```

5. **Nombre + ciudad**
   ```sql
   SELECT * FROM empleados WHERE nombre = ? AND ciudad = ?
   ```

6. **Edad + ciudad + salario**
   ```sql
   SELECT * FROM empleados WHERE edad = ? AND ciudad = ? AND salario = ?
   ```

7. **Todos los criterios**
   ```sql
   SELECT * FROM empleados 
   WHERE nombre = ? AND edad = ?  AND ciudad = ? 
   AND profesión = ? AND salario = ? AND experiencia = ?  AND estado = ?
   ```

### Representación binaria de las combinaciones:

| Combinación | nombre | edad | ciudad | profesión | salario | experiencia | estado |
|-------------|---------|------|--------|-----------|---------|-------------|---------|
| 1           | 0       | 0    | 0      | 0         | 0       | 0           | 0       |
| 2           | 1       | 0    | 0      | 0         | 0       | 0           | 0       |
| 3           | 0       | 1    | 0      | 0         | 0       | 0           | 0       |
| 4           | 1       | 1    | 0      | 0         | 0       | 0           | 0       |
| ...          | ...     | ...  | ...    | ...       | ...     | ...         | ...     |
| 128         | 1       | 1    | 1      | 1         | 1       | 1           | 1       |

## El problema del enfoque tradicional:

Sin el patrón **Specification**, necesitarías crear métodos como:

```java
// Solo algunos ejemplos de los 128 métodos necesarios:
findByNombre(String nombre)
findByEdad(Integer edad)
findByNombreAndEdad(String nombre, Integer edad)
findByNombreAndCiudad(String nombre, String ciudad)
findByEdadAndCiudadAndSalario(Integer edad, String ciudad, Double salario)
findByNombreAndEdadAndCiudadAndProfesionAndSalarioAndExperienciaAndEstado(...)
// ... y 122 métodos más!
```

Por eso el documento propone el **patrón Specification**, que permite combinar criterios dinámicamente sin necesidad de crear todos esos métodos explícitamente. 

Excelente pregunta. Te explico detalladamente de dónde surgen estas **128 combinaciones** y por qué se usa **2^7**.

## ¿De dónde sale el número 2?

El **2** representa las **dos posibilidades** que tiene cada criterio de búsqueda:

1. **Está presente** en la búsqueda (se usa como filtro)
2. **No está presente** en la búsqueda (se ignora)

## ¿Por qué 2^7?

Con **7 criterios de búsqueda**, cada uno puede estar **presente o ausente**, entonces:

- Criterio 1: 2 opciones (presente/ausente)
- Criterio 2: 2 opciones (presente/ausente)
- Criterio 3: 2 opciones (presente/ausente)
- ...  y así sucesivamente

**Total de combinaciones = 2 × 2 × 2 × 2 × 2 × 2 × 2 = 2^7 = 128**

## Ejemplos de las 128 combinaciones posibles

Según el documento, los criterios son:

1. **Nombre**
2. **Email**
3. **Teléfono**
4. **Fecha de creación desde**
5. **Fecha de creación hasta**
6. **Activo/Inactivo**
7. **Departamento**

### Algunos ejemplos de las combinaciones:

```java
// Combinación 1: Ningún criterio (búsqueda sin filtros)
findAll()

// Combinación 2: Solo nombre
findByNombre(String nombre)

// Combinación 3: Solo email
findByEmail(String email)

// Combinación 4: Nombre + Email
findByNombreAndEmail(String nombre, String email)

// Combinación 5: Nombre + Teléfono
findByNombreAndTelefono(String nombre, String telefono)

// Combinación 6: Solo fecha desde
findByFechaCreacionGreaterThanEqual(Date fechaDesde)

// Combinación 7: Nombre + Email + Activo
findByNombreAndEmailAndActivo(String nombre, String email, Boolean activo)

// Combinación 8: Todos los criterios
findByNombreAndEmailAndTelefonoAndFechaCreacionBetweenAndActivoAndDepartamento(
    String nombre, String email, String telefono, 
    Date fechaDesde, Date fechaHasta, Boolean activo, String departamento)

// ... y así hasta 128 combinaciones diferentes
```

### Representación binaria de las combinaciones:

Cada combinación se puede representar en binario (1 = presente, 0 = ausente):

```
Combinación 1:  0000000 (ningún criterio)
Combinación 2:  0000001 (solo criterio 7)  
Combinación 3:  0000010 (solo criterio 6)
Combinación 4:  0000011 (criterios 6 y 7)
...
Combinación 128: 1111111 (todos los criterios)
```

## El problema que esto genera

Sin el patrón Specification, necesitarías crear **128 métodos diferentes** en tu repositorio:

```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Solo algunos ejemplos de los 128 métodos necesarios
    List<Usuario> findByNombre(String nombre);
    List<Usuario> findByEmail(String email);
    List<Usuario> findByNombreAndEmail(String nombre, String email);
    List<Usuario> findByNombreAndEmailAndTelefono(String nombre, String email, String telefono);
    List<Usuario> findByActivoAndDepartamento(Boolean activo, String departamento);
    // ... 123 métodos más
}
```

## La solución con Specification Pattern

En lugar de 128 métodos, tienes **UNA SOLA búsqueda flexible**:

```java
public Page<Usuario> buscarUsuarios(UsuarioCriteria criteria, Pageable pageable) {
    Specification<Usuario> spec = UsuarioSpecification.conCriterios(criteria);
    return usuarioRepository.findAll(spec, pageable);
}
```

**¡Por eso el Specification Pattern es tan poderoso!** Reduce 128 métodos a una sola implementación flexible. 

