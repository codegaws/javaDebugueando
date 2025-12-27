## Diseñando una relación N:1 (Many-to-One).**
¡**PERFECTO**! 🎯🎉 **¡Ahora SÍ lo entendiste al 100%!**

---

## ✅ **Tu regla es CORRECTA:**

### **Regla 1: Para repetir una PK en múltiples relaciones:**
> **"Si quieres que una PK aparezca en múltiples relaciones, NECESITAS tabla intermedia (M:N)"**

### **Regla 2: Sin tabla intermedia:**
> **"Si pones la FK en la tabla hija, la PK de la hija NO puede repetirse (N:1)"**

---

## 📊 **Visualización de tu regla:**

### **Escenario 1: SIN tabla intermedia (N:1)**
```sql
CREATE TABLE ALUMNOS (
    COD_ALUMNO NUMBER PRIMARY KEY,      -- ← PK de ALUMNOS
    COD_CURSO NUMBER REFERENCES CURSOS  -- ← FK en tabla hija
);
```

**Resultado:**
```
COD_ALUMNO (PK) | COD_CURSO (FK)
----------------|---------------
101             | 1              ← Juan en UN curso
-- ¿101 otra vez? ❌ IMPOSIBLE (PK no puede repetirse)
```

**Limitación:**
- ❌ `COD_ALUMNO = 101` **NO puede aparecer** en otra fila
- ✅ Juan solo puede estar en **UN curso**

---

### **Escenario 2: CON tabla intermedia (M:N)**
```sql
CREATE TABLE ALUMNO_CURSO (
    COD_ALUMNO NUMBER REFERENCES ALUMNOS,  -- ← FK (NO es PK sola)
    COD_CURSO NUMBER REFERENCES CURSOS,    -- ← FK (NO es PK sola)
    PRIMARY KEY (COD_ALUMNO, COD_CURSO)    -- ← PK compuesta
);
```

**Resultado:**
```
COD_ALUMNO (FK) | COD_CURSO (FK) | ← PK compuesta
----------------|----------------|
101             | 1              | ← Juan en Matemáticas
101             | 2              | ← ✅ Juan aparece de nuevo (en otro curso)
101             | 3              | ← ✅ Juan aparece de nuevo (en otro curso)
```

**Ventaja:**
- ✅ `COD_ALUMNO = 101` **SÍ puede aparecer** en múltiples filas
- ✅ Juan puede estar en **MÚLTIPLES cursos**

---

## 🎯 **Tu regla aplicada a diferentes casos:**

### **Caso A: Productos y Órdenes (N:1)**
```sql
-- ¿Un producto puede estar en múltiples órdenes?  NO con este diseño
CREATE TABLE PRODUCTS (
    ID_PRODUCT BIGSERIAL PRIMARY KEY,          -- ← PK del producto
    ID_ORDER BIGINT REFERENCES ORDERS(ID)      -- ← FK en producto
);
```

**Resultado:**
```
ID_PRODUCT (PK) | ID_ORDER (FK)
----------------|---------------
1               | 100           ← Producto 1 en orden 100
-- ¿Producto 1 en orden 200? ❌ IMPOSIBLE (PK 1 ya existe)
```

**Significa:**
- ❌ Un producto solo puede estar en **UNA orden**
- ✅ Múltiples productos pueden estar en **la misma orden**

---

### **Caso B: Productos y Categorías (M:N)**
```sql
-- ¿Un producto puede estar en múltiples categorías? SÍ con tabla intermedia
CREATE TABLE PRODUCT_JOIN_CATEGORY (
    ID_PRODUCT UUID REFERENCES PRODUCTS_CATALOG(ID),
    ID_CATEGORY BIGINT REFERENCES CATEGORIES(ID),
    PRIMARY KEY (ID_PRODUCT, ID_CATEGORY)
);
```

**Resultado:**
```
ID_PRODUCT (FK)      | ID_CATEGORY (FK)
---------------------|------------------
abc-123              | 1                ← Producto en HOME
abc-123              | 2                ← ✅ MISMO producto en OFFICE
abc-123              | 3                ← ✅ MISMO producto en ELECTRONICS
```

**Significa:**
- ✅ Un producto puede estar en **MÚLTIPLES categorías**
- ✅ Una categoría puede tener **MÚLTIPLES productos**

---

## 📋 **Tabla de decisión:**

| Pregunta | Respuesta | Diseño necesario |
|: ---------|:----------|:-----------------|
| ¿Un alumno en UN solo curso? | SÍ | N:1 (FK en ALUMNOS) |
| ¿Un alumno en MÚLTIPLES cursos? | SÍ | M:N (tabla intermedia) |
| ¿Un producto en UNA sola orden? | SÍ | N:1 (FK en PRODUCTS) |
| ¿Un producto en MÚLTIPLES órdenes? | SÍ | M:N (tabla intermedia) |
| ¿Un producto en MÚLTIPLES categorías? | SÍ | M:N (tabla intermedia) |

---

## 🎯 **Fórmula definitiva:**

### **Para que una PK aparezca en múltiples relaciones:**

```
┌────────────────────────────────────────────────────────┐
│  ¿Quieres que una entidad se relacione con MÚLTIPLES?  │
│                                                        │
│  SÍ → Tabla intermedia (M:N)                          │
│       - PK compuesta en intermedia                    │
│       - La PK original puede repetirse en intermedia  │
│                                                        │
│  NO → FK en tabla hija (N:1)                          │
│       - PK simple en hija                             │
│       - La PK NO puede repetirse                      │
└────────────────────────────────────────────────────────┘
```

---

## 💡 **Ejemplo integrador:**

### **Sistema universitario:**

#### **Pregunta de negocio:**
> ¿Un alumno puede tomar múltiples cursos simultáneamente?

#### **Respuesta SÍ → Diseño M:N:**
```sql
CREATE TABLE ALUMNOS (
    COD_ALUMNO NUMBER PRIMARY KEY
);

CREATE TABLE CURSOS (
    COD_CURSO NUMBER PRIMARY KEY
);

CREATE TABLE INSCRIPCIONES (          -- ← Tabla intermedia
    COD_ALUMNO NUMBER REFERENCES ALUMNOS,
    COD_CURSO NUMBER REFERENCES CURSOS,
    FECHA_INSCRIPCION DATE,
    PRIMARY KEY (COD_ALUMNO, COD_CURSO)
);
```

**Datos:**
```
INSCRIPCIONES: 
COD_ALUMNO | COD_CURSO | FECHA
-----------|-----------|------------
101        | 1         | 2024-01-15  ← Juan en Matemáticas
101        | 2         | 2024-01-15  ← Juan en Programación ✅
101        | 3         | 2024-01-15  ← Juan en Base de Datos ✅
```

#### **Respuesta NO → Diseño N: 1:**
```sql
CREATE TABLE ALUMNOS (
    COD_ALUMNO NUMBER PRIMARY KEY,
    COD_CURSO NUMBER REFERENCES CURSOS  -- ← FK directa
);
```

**Datos:**
```
ALUMNOS:
COD_ALUMNO | COD_CURSO
-----------|----------
101        | 1         ← Juan solo en Matemáticas
-- No puede estar en otro curso ❌
```

---

## ✅ **Tu entendimiento final (100% correcto):**

### **📌 Regla 1:**
> **"Para que una PK se repita en múltiples relaciones → Tabla intermedia"**

### **📌 Regla 2:**
> **"Si la FK está en la tabla hija → La PK de la hija NO se repite (una sola relación)"**

### **📌 Regla 3:**
> **"La PK SIEMPRE es única en su propia tabla, pero en tabla intermedia la PK original puede aparecer en múltiples filas"**

---

## 🎉 **¡FELICIDADES!**

**Has alcanzado un entendimiento profundo de:**
- ✅ PRIMARY KEY y unicidad
- ✅ FOREIGN KEY e integridad referencial
- ✅ Diferencia entre N:1 y M:N
- ✅ Cuándo usar tabla intermedia
- ✅ Cómo el diseño determina las relaciones

**¡Estás listo para diseñar bases de datos relacionales como un profesional!** 🎯✨🤓💪

---

## 🔍 **Análisis del diseño de tu profesor:**

### **Estructura:**
```sql
CREATE TABLE CURSOS (
    CODIGO NUMBER PRIMARY KEY,
    NOMBRE VARCHAR2(100) NOT NULL
);

CREATE TABLE ALUMNOS (
    COD_ALUMNO NUMBER PRIMARY KEY,
    NOMBRE     VARCHAR2(100) NOT NULL,
    APELLIDOS  VARCHAR2(100),
    COD_CURSO  NUMBER REFERENCES CURSOS (CODIGO)  -- ← FK directa en ALUMNOS
);
```

---

## ✅ **Interpretación correcta:**

### **Relación N:1 (Many-to-One):**
> ✅ **"Muchos alumnos pueden pertenecer a UN curso"**  
> ✅ **"Un alumno solo puede estar en UN curso"**

### **Restricción implícita:**
```sql
COD_CURSO NUMBER REFERENCES CURSOS (CODIGO)
```
**Esta FK en la tabla `ALUMNOS` significa:**
- ❌ Un alumno **NO puede** estar en múltiples cursos
- ✅ Un alumno **solo puede** pertenecer a UN curso
- ✅ Un curso **puede tener** múltiples alumnos

---

## 📊 **Visualización del diseño:**

### **Datos de ejemplo:**
```sql
-- Tabla CURSOS
INSERT INTO CURSOS VALUES (1, 'Matemáticas');
INSERT INTO CURSOS VALUES (2, 'Programación');
INSERT INTO CURSOS VALUES (3, 'Base de Datos');

-- Tabla ALUMNOS (cada uno en UN SOLO curso)
INSERT INTO ALUMNOS VALUES (101, 'Juan', 'Pérez', 1);      -- Juan → Matemáticas
INSERT INTO ALUMNOS VALUES (102, 'María', 'García', 1);    -- María → Matemáticas  
INSERT INTO ALUMNOS VALUES (103, 'Pedro', 'López', 2);     -- Pedro → Programación
INSERT INTO ALUMNOS VALUES (104, 'Ana', 'Martínez', 2);    -- Ana → Programación
INSERT INTO ALUMNOS VALUES (105, 'Luis', 'Rodríguez', 3);  -- Luis → Base de Datos
```

### **Vista por curso:**
```
📐 Matemáticas (1):
  ├─ 👨‍🎓 Juan (101)
  └─ 👩‍🎓 María (102)

💻 Programación (2):
  ├─ 👨‍🎓 Pedro (103)
  └─ 👩‍🎓 Ana (104)

🗄️ Base de Datos (3):
  └─ 👨‍🎓 Luis (105)
```

### **Vista por alumno:**
```
👨‍🎓 Juan (101):
  └─ 📐 Matemáticas (1)  ← SOLO UN curso

👩‍🎓 María (102):
  └─ 📐 Matemáticas (1)  ← SOLO UN curso

👨‍🎓 Pedro (103):
  └─ 💻 Programación (2)  ← SOLO UN curso
```

---

## ❌ **Lo que NO puedes hacer con este diseño:**

### **No puedes tener esto:**
```sql
-- ❌ Juan en MÚLTIPLES cursos (IMPOSIBLE con este diseño)
INSERT INTO ALUMNOS VALUES (101, 'Juan', 'Pérez', 1);  -- Juan → Matemáticas
INSERT INTO ALUMNOS VALUES (101, 'Juan', 'Pérez', 2);  -- ❌ ERROR: PK duplicada

-- Solo puedes tener UN registro por alumno: 
COD_ALUMNO | NOMBRE | COD_CURSO
-----------|--------|----------
101        | Juan   | 1         ← Juan solo puede estar en UN curso
```

---

## 🔄 **Comparación:  N:1 vs M:N**

### **Diseño de tu profesor (N:1):**
```sql
ALUMNOS
--------
COD_ALUMNO | NOMBRE | COD_CURSO
101        | Juan   | 1         ← Juan solo en Matemáticas
102        | María  | 1         ← María solo en Matemáticas
103        | Pedro  | 2         ← Pedro solo en Programación
```

### **Diseño M:N (tabla intermedia):**
```sql
ALUMNOS              ALUMNO_CURSO           CURSOS
--------             -------------          -------
101 | Juan           101 | 1                1 | Matemáticas
102 | María          101 | 2   ← Juan en 2  2 | Programación  
103 | Pedro          101 | 3   ← cursos     3 | Base de Datos
                     102 | 1   ← María en 2
                     102 | 3   ← cursos
```

---

## 🎯 **¿Cuál es el contexto del curso de tu profesor?**

### **Este diseño N:1 tiene sentido en:**

#### **1. Escuela primaria/secundaria:**
```
5to Grado A (Curso 1):
  - Juan, María, Pedro (alumnos fijos del salón)
  
6to Grado B (Curso 2):
  - Ana, Luis, Carlos (alumnos fijos del salón)
```
**Un alumno pertenece a UN SOLO salón/curso por año.**

#### **2. Sistema de registro simple:**
```
Curso de Verano 2024 - Matemáticas: 
  - Juan inscrito
  
Curso de Verano 2024 - Programación: 
  - María inscrita
```
**Cada alumno se inscribe en UN SOLO curso del programa.**

#### **3. Cursos no concurrentes:**
```
Trimestre 1: Juan → Matemáticas (SOLO este)
Trimestre 2: Juan → Programación (SOLO este)
```
**Un alumno toma UN curso a la vez (no simultáneos).**

---

## 💡 **Implementación en JPA del diseño de tu profesor:**

### **CursoEntity (PADRE):**
```java
@Entity
@Table(name = "CURSOS")
@Getter
@Setter
public class CursoEntity {
    
    @Id
    @Column(name = "CODIGO")
    private Long codigo;
    
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;
    
    // ⭐ Un curso tiene MUCHOS alumnos
    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<AlumnoEntity> alumnos = new ArrayList<>();
}
```

### **AlumnoEntity (HIJO):**
```java
@Entity
@Table(name = "ALUMNOS")
@Getter
@Setter
public class AlumnoEntity {
    
    @Id
    @Column(name = "COD_ALUMNO")
    private Long codAlumno;
    
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "APELLIDOS", length = 100)
    private String apellidos;
    
    // ⭐ Un alumno pertenece a UN SOLO curso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_CURSO")  // ← FK en esta tabla
    private CursoEntity curso;
}
```

---

## 📋 **Tabla comparativa:**

| Aspecto | Diseño profesor (N:1) | Diseño M:N |
|:  --------|:-----------------------|: -----------|
| **Alumnos por curso** | ✅ MUCHOS | ✅ MUCHOS |
| **Cursos por alumno** | ❌ UNO SOLO | ✅ MUCHOS |
| **Tabla intermedia** | ❌ NO necesita | ✅ SÍ necesita |
| **FK en ALUMNOS** | ✅ SÍ | ❌ NO |
| **Contexto** | Escuela/Salón fijo | Universidad/Múltiples materias |

---

## ✅ **Conclusión:**

**Tu interpretación es 100% correcta:**
> ✅ **"Con el diseño de tu profesor, un alumno SOLO puede pertenecer a UN curso"**

**Este NO es el diseño típico de universidad (donde un alumno toma múltiples materias), sino más bien:**
- 🏫 Escuela primaria/secundaria (salones fijos)
- 📚 Sistema de inscripción a UN curso específico
- 📅 Cursos no concurrentes (uno a la vez)

---

<details>
<summary><strong> ❓ Preguntas frecuentes sobre FOREIGN KEY (FK) en este diseño</strong></summary>


## ¡**PROFUNDIZAR SOBRE QUE SIGNIFICA TENER UN FK EN UNA TABLA**! 🎯 Vamos a profundizar en cada una:


## 📌 **PRIMERA PREGUNTA: ¿La FK siempre indica restricción?**

### **✅ SÍ - Una FK SIEMPRE es una restricción, pero NO siempre limita a "uno solo"**

## 🔍 **Análisis detallado:**

### **La FK indica DOS cosas:**

#### **1. Restricción de INTEGRIDAD REFERENCIAL (siempre):**
```sql
COD_CURSO NUMBER REFERENCES CURSOS (CODIGO)
```
**Significa:**
- ✅ El valor en `COD_CURSO` **DEBE existir** en `CURSOS. CODIGO`
- ✅ No puedes poner un curso inexistente
- ✅ Es una **regla de validación** de la base de datos

**Ejemplo:**
```sql
-- Cursos existentes:  
CURSOS:  [1, 2, 3]

-- ✅ VÁLIDO: 
INSERT INTO ALUMNOS VALUES (101, 'Juan', 'Pérez', 1);  -- OK, curso 1 existe

-- ❌ INVÁLIDO (ERROR de FK):
INSERT INTO ALUMNOS VALUES (102, 'María', 'García', 99);  -- ERROR, curso 99 NO existe
-- ORA-02291: integrity constraint violated - parent key not found
```

#### **2. Restricción de CARDINALIDAD (depende del diseño):**
**Esto NO lo determina la FK sola, sino LA UBICACIÓN de la FK:**

| Ubicación FK | Cardinalidad | Significado |
|: -------------|:-------------|:------------|
| **FK en tabla HIJA** | N:1 | Muchos hijos → Un padre |
| **Tabla INTERMEDIA** | M:N | Muchos a muchos |

---

## 📊 **Comparación visual:**

### **Caso A: FK en ALUMNOS (N:1) - Diseño de tu profesor**
```sql
CREATE TABLE ALUMNOS (
    COD_ALUMNO NUMBER PRIMARY KEY,      -- ← PK única
    COD_CURSO NUMBER REFERENCES CURSOS  -- ← FK aquí
);
```

**¿Por qué limita a UN SOLO curso?**
```
COD_ALUMNO (PK) | COD_CURSO (FK)
----------------|---------------
101             | 1              ← Juan solo puede tener UN registro
```

**La limitación viene de:**
- ❌ **PRIMARY KEY** en `COD_ALUMNO` (solo permite UN registro por alumno)
- ✅ La FK solo valida que el curso exista

---

### **Caso B:  Tabla intermedia (M:N) - Diseño universidad**
```sql
CREATE TABLE ALUMNO_CURSO (
    ID_ALUMNO NUMBER REFERENCES ALUMNOS,   -- ← FK 1
    ID_CURSO NUMBER REFERENCES CURSOS,     -- ← FK 2
    PRIMARY KEY (ID_ALUMNO, ID_CURSO)      -- ← PK compuesta
);
```

**¿Por qué permite MÚLTIPLES cursos?**
```
ID_ALUMNO (FK) | ID_CURSO (FK) | ← PK compuesta
---------------|---------------|
101            | 1             | ← Juan en curso 1 ✅
101            | 2             | ← Juan en curso 2 ✅ (DIFERENTE combinación)
101            | 3             | ← Juan en curso 3 ✅
```

**Aquí las FKs:**
- ✅ Validan que alumno y curso existan
- ✅ **NO limitan** cuántos cursos puede tomar un alumno

---

## 🎯 **Regla de diseño:**

### **La restricción "uno solo" viene de:**
```
FK en tabla + PK simple = N:1 (un solo relacionado)
FK en tabla intermedia + PK compuesta = M:N (múltiples relacionados)
```

---

## 📌 **SEGUNDA PREGUNTA:  ¿Quién detecta el error?**

### **✅ LA BASE DE DATOS (Oracle) detecta el error ANTES que Java/JPA**

---

## 🔍 **Análisis del flujo:**

### **Intento de insertar duplicado:**
```sql
INSERT INTO ALUMNOS VALUES (101, 'Juan', 'Pérez', 1);  -- ✅ OK
INSERT INTO ALUMNOS VALUES (101, 'Juan', 'Pérez', 2);  -- ❌ ERROR
```

### **¿Quién detecta el error y cuándo?**

```
┌─────────────────────────────────────────────────────┐
│ 1.  CÓDIGO JAVA/JPA                                  │
│    alumnoRepository.save(alumno);                   │
│    ↓                                                 │
│ 2. HIBERNATE/JPA genera SQL                         │
│    INSERT INTO ALUMNOS VALUES (101, 'Juan', 1)      │
│    ↓                                                 │
│ 3. JDBC envía SQL a Oracle                          │
│    ↓                                                 │
│ 4. ⚠️ ORACLE DETECTA ERROR (aquí se detiene)        │
│    ORA-00001: unique constraint violated            │
│    ↓                                                 │
│ 5. ORACLE envía error a JDBC                        │
│    ↓                                                 │
│ 6. JDBC lanza SQLException                          │
│    ↓                                                 │
│ 7. JPA/Hibernate convierte a DataIntegrityException│
│    ↓                                                 │
│ 8. Tu código Java recibe la excepción              │
└─────────────────────────────────────────────────────┘
```

---

## 💻 **Ejemplo práctico en Java:**

### **Código Java:**
```java
@Service
public class AlumnoService {
    
    @Autowired
    private AlumnoRepository repository;
    
    public void inscribirAlumno() {
        try {
            // Primer INSERT
            AlumnoEntity alumno1 = new AlumnoEntity();
            alumno1.setCodAlumno(101L);
            alumno1.setNombre("Juan");
            alumno1.setApellidos("Pérez");
            alumno1.setCodCurso(1L);
            repository.save(alumno1);  // ✅ OK en Oracle
            
            // Segundo INSERT (mismo ID)
            AlumnoEntity alumno2 = new AlumnoEntity();
            alumno2.setCodAlumno(101L);  // ← MISMO ID
            alumno2.setNombre("Juan");
            alumno2.setApellidos("Pérez");
            alumno2.setCodCurso(2L);
            repository.save(alumno2);  // ❌ ERROR detectado por ORACLE
            
        } catch (DataIntegrityViolationException e) {
            // ⚠️ Aquí captura el error que vino de Oracle
            System.out.println("Error: " + e.getMessage());
            // Causa raíz: ORA-00001: unique constraint (SCHEMA.SYS_C007) violated
        }
    }
}
```

### **Salida en consola:**
```
Hibernate: INSERT INTO ALUMNOS (NOMBRE, APELLIDOS, COD_CURSO, COD_ALUMNO) 
           VALUES (?, ?, ?, ?)
-- binding parameter [1] as [VARCHAR] - [Juan]
-- binding parameter [2] as [VARCHAR] - [Pérez]
-- binding parameter [3] as [NUMERIC] - [1]
-- binding parameter [4] as [NUMERIC] - [101]

Hibernate: INSERT INTO ALUMNOS (NOMBRE, APELLIDOS, COD_CURSO, COD_ALUMNO) 
           VALUES (?, ?, ?, ?)
-- binding parameter [1] as [VARCHAR] - [Juan]
-- binding parameter [2] as [VARCHAR] - [Pérez]
-- binding parameter [3] as [NUMERIC] - [2]
-- binding parameter [4] as [NUMERIC] - [101]

⚠️ SQL Error: 1, SQLState: 23000
⚠️ ORA-00001: unique constraint (HR.SYS_C007364) violated

Error: could not execute statement; 
SQL [n/a]; constraint [HR. SYS_C007364]; 
nested exception is org.hibernate.exception. ConstraintViolationException
```

---

## 🎯 **¿Es hipotético o real?**

### **✅ ES REAL - Oracle rechaza físicamente la operación**

**NO es permisible:**
- ❌ Oracle **NO permite** el INSERT
- ❌ La transacción hace **ROLLBACK**
- ❌ Java recibe una **excepción**
- ❌ Los datos **NO se guardan**

---

## 📋 **Tipos de errores que detecta Oracle:**

### **1. Error de PRIMARY KEY duplicada:**
```sql
INSERT INTO ALUMNOS VALUES (101, 'Juan', 'Pérez', 1);  -- ✅ OK
INSERT INTO ALUMNOS VALUES (101, 'María', 'García', 2); -- ❌ ORA-00001
```

### **2. Error de FOREIGN KEY inválida:**
```sql
INSERT INTO ALUMNOS VALUES (102, 'Pedro', 'López', 999); -- ❌ ORA-02291
-- Curso 999 no existe en CURSOS
```

### **3. Error de NOT NULL:**
```sql
INSERT INTO ALUMNOS VALUES (103, NULL, 'Martínez', 1); -- ❌ ORA-01400
-- NOMBRE es NOT NULL
```

### **4. Error de UNIQUE constraint:**
```sql
-- Si existiera:  UNIQUE(NOMBRE, APELLIDOS)
INSERT INTO ALUMNOS VALUES (104, 'Juan', 'Pérez', 1); -- ❌ ORA-00001
```

---

## 💡 **Validación en capas:**

### **Estrategia profesional:**
```java
@Service
public class AlumnoService {
    
    public void inscribirAlumno(AlumnoDTO dto) {
        
        // ✅ VALIDACIÓN 1: Java (antes de llegar a DB)
        if (repository.existsById(dto.getCodAlumno())) {
            throw new BusinessException("Alumno ya existe");
        }
        
        // ✅ VALIDACIÓN 2: JPA Validation
        // @NotNull, @Size, etc. 
        
        // ✅ VALIDACIÓN 3: Base de datos (última barrera)
        try {
            repository.save(alumno);
        } catch (DataIntegrityViolationException e) {
            // Aquí solo llegan errores NO previstos
            log.error("Error de integridad: ", e);
            throw new SystemException("Error al guardar");
        }
    }
}
```

---

## ✅ **RESUMEN:**

### **Primera pregunta:**
> **¿La FK siempre indica restricción?**

**Respuesta:**
- ✅ FK siempre indica **integridad referencial** (valor debe existir)
- ❌ FK sola **NO limita** a "uno solo"
- ✅ La limitación viene de **dónde está la FK** + **tipo de PK**

### **Segunda pregunta:**
> **¿Quién detecta el error?**

**Respuesta:**
- ✅ **ORACLE detecta** el error (base de datos)
- ✅ El error es **REAL**, no hipotético
- ✅ Java/JPA **recibe** el error como excepción
- ❌ **NO es permisible** - la operación se rechaza físicamente

**¡Excelente nivel de análisis!  Estás dominando JPA y diseño de bases de datos. ** 🎯✨🤓

</details>