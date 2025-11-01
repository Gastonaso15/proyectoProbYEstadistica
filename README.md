# Proyecto Final - Probabilidad y Estadística
## Simulación de Sistema Académico

Este proyecto simula el paso de 100 estudiantes por un grupo de 6 asignaturas con prerrequisitos.

## Requisitos

- Java 11 o superior
- Maven 3.6+

## Estructura del Proyecto

El proyecto contiene las siguientes clases principales:

- **Main.java**: Punto de entrada del programa
- **SimulationEngine.java**: Motor de simulación que ejecuta la simulación completa
- **Course.java**: Representa una asignatura con sus propiedades (distribución, parámetros, etc.)
- **Student.java**: Representa un estudiante con su progreso y estado
- **StatisticsGenerator.java**: Calcula todas las estadísticas requeridas
- **GraphicsGenerator.java**: Genera gráficos (histogramas y boxplots)

## Características

### Asignaturas
- **Primer bloque**: A → B → C (con prerrequisitos directos)
- **Segundo bloque**: D → E → F (con prerrequisitos directos)
- Cada asignatura puede usar distribución Normal o Poisson para generar notas

### Reglas de Simulación
- **100 estudiantes** (valor fijo)
- **Nota mínima para aprobar**: 60 puntos (valor fijo)
- **Máximo de intentos por asignatura**: 5 (después el estudiante abandona)
- **Máximo de asignaturas por semestre**: 2
- Se puede inscribir solo si se ha aprobado el prerrequisito (si existe)

### Resultados Generados

1. **Promedio de tiempo para completar las 6 asignaturas** (histograma y boxplot)
2. **Promedio de tiempo para aprobar cada asignatura**
3. **Total de estudiantes que completan entre 3 y 15 semestres**
4. **Clasificación de asignaturas por dificultad**
5. **Total de inscripciones por asignatura**
6. **Cantidad de abandonos por semestre**
7. **Asignatura que provoca más abandonos**

## Configuración de Asignaturas

Los parámetros de las asignaturas (distribución, media, varianza, lambda) se pueden modificar en el método `initializeCourses()` de la clase `SimulationEngine.java`. 

Por defecto:
- **A**: Normal(μ=70, σ²=15)
- **B**: Normal(μ=65, σ²=18)
- **C**: Poisson(λ=60)
- **D**: Normal(μ=72, σ²=14)
- **E**: Normal(μ=68, σ²=16)
- **F**: Poisson(λ=58)

## Compilación y Ejecución

### Compilar el proyecto:
```bash
mvn clean compile
```

### Ejecutar la simulación:
```bash
mvn exec:java -Dexec.mainClass="proyecto.Main"
```

O compilar y ejecutar con:
```bash
mvn clean package
java -cp target/classes:target/dependency/* proyecto.Main
```

## Visualizaciones en Consola

Después de ejecutar la simulación, se mostrarán en consola:
- **Histograma**: Tiempo de finalización de las 6 asignaturas (visualización ASCII)
- **Boxplot**: Tiempo de finalización con estadísticas descriptivas

Todos los resultados se imprimen directamente en la consola, no se generan archivos.

## Valores Modificables

- Los valores **modificables** (marcados en azul en el documento) se encuentran en `SimulationEngine.java`, método `initializeCourses()`
- Ver el archivo `INSTRUCCIONES_MODIFICACION.md` para instrucciones detalladas

## Valores Fijos

- **Nota mínima para aprobar**: 60 puntos (NO modificar)
- **Cantidad de estudiantes**: 100 (NO modificar)

Estos valores están claramente marcados con comentarios `// VALOR FIJO` en el código.

