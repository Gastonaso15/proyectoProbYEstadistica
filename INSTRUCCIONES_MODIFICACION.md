# Instrucciones para Modificar Valores del Proyecto

## Valores Modificables (Marcados en Azul)

### 📍 Ubicación Principal: `SimulationEngine.java`

Los valores modificables se encuentran en dos lugares:
1. **Cantidad de estudiantes**: Constante `MAX_STUDENTS` (línea ~30)
2. **Parámetros de asignaturas**: Método `initializeCourses()` (líneas ~62-89)

### 1. Modificar Cantidad de Estudiantes

**Ubicación:** `SimulationEngine.java`, línea ~30

```java
// MODIFICABLE - Cambiar este valor según se necesite
private static final int MAX_STUDENTS = 100; // MODIFICABLE - Cantidad de estudiantes
```

**Ejemplo:** Para simular 200 estudiantes:
```java
private static final int MAX_STUDENTS = 200; // MODIFICABLE - Cantidad de estudiantes
```

**Nota:** Este valor se mostrará automáticamente en el Main al ejecutar la simulación.

### 2. Modificar Parámetros de Asignaturas

**Pasos para Modificar:**

1. **Abrir el archivo:** `src/main/java/proyecto/SimulationEngine.java`**

2. **Localizar el método `initializeCourses()`** (líneas 62-89 aproximadamente)

3. **Modificar los parámetros según necesite:**

#### Para Asignaturas con Distribución NORMAL (A, B, D, E):
```java
// Ejemplo: Asignatura A
Course A = new Course("A", null, Course.DistributionType.NORMAL, 
                      70.0,  // ← MODIFICAR: Media de la distribución
                      15.0,  // ← MODIFICAR: Varianza de la distribución
                      0);    // ← Dejar en 0 para distribuciones Normal
```

#### Para Asignaturas con Distribución POISSON (C, F):
```java
// Ejemplo: Asignatura C
Course C = new Course("C", B, Course.DistributionType.POISSON, 
                      0,     // ← Dejar en 0 para distribuciones Poisson
                      0,     // ← Dejar en 0 para distribuciones Poisson
                      60.0); // ← MODIFICAR: Lambda (parámetro de Poisson)
```

### Ejemplo de Modificación:

Si desea cambiar la asignatura A para que tenga media 75 y varianza 20:

**ANTES:**
```java
Course A = new Course("A", null, Course.DistributionType.NORMAL, 70.0, 15.0, 0);
```

**DESPUÉS:**
```java
Course A = new Course("A", null, Course.DistributionType.NORMAL, 75.0, 20.0, 0);
```

### Cambiar Tipo de Distribución:

Si desea cambiar una asignatura de Normal a Poisson (o viceversa):

1. Cambiar `DistributionType.NORMAL` a `DistributionType.POISSON` (o viceversa)
2. Para Poisson: poner mean=0, variance=0, y establecer lambda
3. Para Normal: establecer mean y variance, y poner lambda=0

**Ejemplo - Cambiar A de Normal a Poisson:**
```java
// ANTES (Normal)
Course A = new Course("A", null, Course.DistributionType.NORMAL, 70.0, 15.0, 0);

// DESPUÉS (Poisson con lambda=65)
Course A = new Course("A", null, Course.DistributionType.POISSON, 0, 0, 65.0);
```

## Valores FIJOS (NO Modificar)

Los siguientes valores están marcados como FIJOS en el documento y NO deben modificarse:

1. **PASSING_SCORE = 60** (línea ~23 en SimulationEngine.java)
   - Puntaje mínimo requerido para aprobar una asignatura
   - Este valor está claramente marcado con comentario `// VALOR FIJO` en el código

## Resumen de Parámetros por Asignatura

| Asignatura | Tipo | Prerrequisito | Parámetros Modificables |
|------------|------|--------------|------------------------|
| A | Normal | Ninguno | Media, Varianza |
| B | Normal | A | Media, Varianza |
| C | Poisson | B | Lambda |
| D | Normal | Ninguno | Media, Varianza |
| E | Normal | D | Media, Varianza |
| F | Poisson | E | Lambda |

## Notas Importantes

- Después de modificar valores, debe **recompilar** el proyecto:
  ```bash
  mvn clean compile
  ```

- Los cambios solo afectan la generación de notas, no alteran la lógica de la simulación.

- Todos los resultados se imprimen en consola (no se generan archivos PNG).

