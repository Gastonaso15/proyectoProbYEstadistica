# Instrucciones para Modificar Valores del Proyecto

## ⚠️ Ubicación Principal: `SimulationConfig.java`

**TODOS los parámetros modificables están ahora centralizados en un solo archivo:**
`src/main/java/proyecto/SimulationConfig.java`

Esto facilita la modificación de todos los valores sin tener que buscar en múltiples archivos.

---

## Parámetros Modificables

### 1. Parámetros de Distribuciones de Asignaturas (Punto 1)

**Ubicación:** `SimulationConfig.java` - Líneas 35-94

Estos parámetros controlan cómo se generan las notas de cada asignatura:

#### Para Asignaturas con Distribución NORMAL (A, B, D, E):

```java
// Asignatura A
public static final double COURSE_A_MEAN = 70.0;      // ← MODIFICAR: Media
public static final double COURSE_A_VARIANCE = 15.0;  // ← MODIFICAR: Varianza
public static final double COURSE_A_LAMBDA = 0.0;     // ← Dejar en 0 para Normal
```

#### Para Asignaturas con Distribución POISSON (C, F):

```java
// Asignatura C
public static final double COURSE_C_MEAN = 0.0;       // ← Dejar en 0 para Poisson
public static final double COURSE_C_VARIANCE = 0.0;   // ← Dejar en 0 para Poisson
public static final double COURSE_C_LAMBDA = 60.0;    // ← MODIFICAR: Lambda (parámetro de Poisson)
```

**Ejemplo:** Cambiar la asignatura A para que tenga media 75 y varianza 20:

```java
// ANTES
public static final double COURSE_A_MEAN = 70.0;
public static final double COURSE_A_VARIANCE = 15.0;

// DESPUÉS
public static final double COURSE_A_MEAN = 75.0;
public static final double COURSE_A_VARIANCE = 20.0;
```

---

### 2. Puntaje Mínimo para Aprobar (Punto 2)

**Ubicación:** `SimulationConfig.java` - Línea 19

```java
public static final double PASSING_SCORE = 60.0;  // ← MODIFICAR: Umbral de aprobación
```

**Ejemplo:** Cambiar el umbral de aprobación a 70 puntos:

```java
public static final double PASSING_SCORE = 70.0;
```

**Regla:** Un alumno solo puede tener dos estados:
- Aprobar la asignatura al obtener `PASSING_SCORE` puntos o más
- Recursar en caso contrario

---

### 3. Rango de Semestres para Completar Todas las Asignaturas (Punto 3)

**Ubicación:** `SimulationConfig.java` - Líneas 29-34

```java
public static final int MIN_SEMESTERS_COMPLETION = 3;   // ← MODIFICAR: Límite inferior
public static final int MAX_SEMESTERS_COMPLETION = 15;  // ← MODIFICAR: Límite superior
```

**Ejemplo:** Cambiar el rango a entre 5 y 20 semestres:

```java
public static final int MIN_SEMESTERS_COMPLETION = 5;
public static final int MAX_SEMESTERS_COMPLETION = 20;
```

**Uso:** Estos valores determinan el rango para contar estudiantes que superan todas las asignaturas.

---

### 4. Máximo de Intentos por Asignatura (Punto 5)

**Ubicación:** `SimulationConfig.java` - Línea 25

```java
public static final int MAX_ATTEMPTS_PER_COURSE = 5;  // ← MODIFICAR: Máximo de inscripciones
```

**Ejemplo:** Permitir hasta 7 intentos:

```java
public static final int MAX_ATTEMPTS_PER_COURSE = 7;
```

**Regla:** Cada asignatura se puede inscribir un máximo de `MAX_ATTEMPTS_PER_COURSE` veces. Después de eso, el estudiante abandona.

---

### 5. Cantidad de Estudiantes

**Ubicación:** `SimulationConfig.java` - Línea 36

```java
public static final int MAX_STUDENTS = 120;  // ← MODIFICAR: Cantidad de estudiantes
```

**Ejemplo:** Simular 200 estudiantes:

```java
public static final int MAX_STUDENTS = 200;
```

---

## Resumen de Parámetros por Asignatura

| Asignatura | Tipo Distribución | Prerrequisito | Parámetros Modificables en SimulationConfig |
|------------|-------------------|--------------|---------------------------------------------|
| A | Normal | Ninguno | `COURSE_A_MEAN`, `COURSE_A_VARIANCE` |
| B | Normal | A | `COURSE_B_MEAN`, `COURSE_B_VARIANCE` |
| C | Poisson | B | `COURSE_C_LAMBDA` |
| D | Normal | Ninguno | `COURSE_D_MEAN`, `COURSE_D_VARIANCE` |
| E | Normal | D | `COURSE_E_MEAN`, `COURSE_E_VARIANCE` |
| F | Poisson | E | `COURSE_F_LAMBDA` |

---

## Cambiar Tipo de Distribución

Si desea cambiar una asignatura de Normal a Poisson (o viceversa), debe modificar dos archivos:

1. **`SimulationConfig.java`**: Actualizar los valores de MEAN, VARIANCE y LAMBDA
2. **`SimulationEngine.java`**: Cambiar el `DistributionType` en el método `initializeCourses()`

**Ejemplo - Cambiar A de Normal a Poisson:**

**En SimulationConfig.java:**
```java
// ANTES
public static final double COURSE_A_MEAN = 70.0;
public static final double COURSE_A_VARIANCE = 15.0;
public static final double COURSE_A_LAMBDA = 0.0;

// DESPUÉS
public static final double COURSE_A_MEAN = 0.0;
public static final double COURSE_A_VARIANCE = 0.0;
public static final double COURSE_A_LAMBDA = 65.0;
```

**En SimulationEngine.java (método initializeCourses()):**
```java
// ANTES
Course A = new Course("A", null, Course.DistributionType.NORMAL, ...);

// DESPUÉS
Course A = new Course("A", null, Course.DistributionType.POISSON, ...);
```

---

## Notas Importantes

- **Después de modificar valores, debe recompilar el proyecto:**
  ```bash
  mvn clean compile
  ```

- Todos los parámetros modificables están claramente documentados en `SimulationConfig.java` con comentarios explicativos.

- Los cambios afectan la simulación completa: generación de notas, criterios de aprobación, y estadísticas.

- Los valores por defecto están configurados según las especificaciones originales del proyecto.

---

## Estructura de Archivos

```
src/main/java/proyecto/
├── SimulationConfig.java    ← TODOS los parámetros modificables aquí
├── SimulationEngine.java    ← Usa valores de SimulationConfig
├── Student.java             ← Usa MAX_ATTEMPTS_PER_COURSE
├── StatisticsGenerator.java ← Usa rango de semestres configurable
└── ...
```
