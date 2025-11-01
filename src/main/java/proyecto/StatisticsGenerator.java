package proyecto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Genera todas las estadísticas requeridas de la simulación.
 */
public class StatisticsGenerator {
    private SimulationEngine engine;
    
    public StatisticsGenerator(SimulationEngine engine) {
        this.engine = engine;
    }
    
    /**
     * 1. Calcula el promedio de tiempo que le toma a cada estudiante 
     *    avanzar las 6 asignaturas.
     */
    public List<Double> getCompletionTimes() {
        List<Double> completionTimes = new ArrayList<>();
        
        for (Student student : engine.getStudents()) {
            if (student.hasCompletedAllCourses()) {
                int semesters = student.getSemesterCompleted();
                if (semesters > 0) {
                    completionTimes.add((double) semesters);
                }
            }
        }
        
        return completionTimes;
    }
    
    /**
     * 2. Calcula el promedio de tiempo para aprobar cada asignatura.
     */
    public Map<String, Double> getAveragePassingTimePerCourse() {
        Map<String, Double> averages = new HashMap<>();
        
        for (Map.Entry<String, Course> entry : engine.getCourses().entrySet()) {
            double avgTime = entry.getValue().getAveragePassingTime();
            averages.put(entry.getKey(), avgTime);
        }
        
        return averages;
    }
    
    /**
     * 3. Total de alumnos que superan todas las asignaturas entre 3 y 15 semestres.
     */
    public int getStudentsCompletedBetween3And15Semesters() {
        int count = 0;
        
        for (Student student : engine.getStudents()) {
            if (student.hasCompletedAllCourses()) {
                int semesters = student.getSemesterCompleted();
                if (semesters >= 3 && semesters <= 15) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * 4. Clasifica las asignaturas de acuerdo a la dificultad.
     */
    public List<Map.Entry<String, Double>> getCoursesByDifficulty() {
        Map<String, Double> difficultyMap = new HashMap<>();
        
        for (Map.Entry<String, Course> entry : engine.getCourses().entrySet()) {
            double difficulty = entry.getValue().getDifficultyIndex();
            difficultyMap.put(entry.getKey(), difficulty);
        }
        
        // Ordenar de mayor a menor dificultad
        return difficultyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * 5. Total de alumnos que se inscriben en cada asignatura.
     */
    public Map<String, Integer> getTotalEnrollmentsPerCourse() {
        Map<String, Integer> enrollments = new HashMap<>();
        
        for (Map.Entry<String, Course> entry : engine.getCourses().entrySet()) {
            enrollments.put(entry.getKey(), entry.getValue().getTotalEnrollments());
        }
        
        return enrollments;
    }
    
    /**
     * 6. Cantidad de abandonos por semestre.
     */
    public Map<Integer, Integer> getAbandonmentsPerSemester() {
        Map<Integer, Integer> abandonments = new HashMap<>();
        
        for (Student student : engine.getStudents()) {
            if (student.hasAbandoned()) {
                int semester = student.getAbandonmentSemester();
                abandonments.put(semester, abandonments.getOrDefault(semester, 0) + 1);
            }
        }
        
        return abandonments;
    }
    
    /**
     * 7. Asignatura que provoca más abandonos.
     */
    public String getCourseWithMostAbandonments() {
        String courseName = null;
        int maxAbandonments = -1;
        
        for (Map.Entry<String, Course> entry : engine.getCourses().entrySet()) {
            int abandonments = entry.getValue().getTotalAbandonments();
            if (abandonments > maxAbandonments) {
                maxAbandonments = abandonments;
                courseName = entry.getKey();
            }
        }
        
        return courseName != null ? courseName : "Ninguna";
    }
    
    /**
     * Genera un reporte completo con todas las estadísticas.
     */
    public void printFullReport() {
        System.out.println("=".repeat(80));
        System.out.println("REPORTE FINAL DE SIMULACIÓN");
        System.out.println("=".repeat(80));
        
        // 1. Promedio de tiempo para completar las 6 asignaturas
        List<Double> completionTimes = getCompletionTimes();
        if (!completionTimes.isEmpty()) {
            double avgCompletion = completionTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            System.out.println("\n1. PROMEDIO DE TIEMPO PARA COMPLETAR LAS 6 ASIGNATURAS:");
            System.out.printf("   Promedio: %.2f semestres\n", avgCompletion);
            System.out.printf("   Mínimo: %.2f semestres\n", Collections.min(completionTimes));
            System.out.printf("   Máximo: %.2f semestres\n", Collections.max(completionTimes));
            System.out.printf("   Total estudiantes que completaron: %d\n", completionTimes.size());
        }
        
        // 2. Promedio de tiempo para aprobar cada asignatura
        System.out.println("\n2. PROMEDIO DE TIEMPO PARA APROBAR CADA ASIGNATURA:");
        Map<String, Double> avgPassingTimes = getAveragePassingTimePerCourse();
        for (Map.Entry<String, Double> entry : avgPassingTimes.entrySet()) {
            System.out.printf("   %s: %.2f semestres\n", entry.getKey(), entry.getValue());
        }
        
        // 3. Estudiantes que completan entre 3 y 15 semestres
        int completed = getStudentsCompletedBetween3And15Semesters();
        System.out.println("\n3. ESTUDIANTES QUE COMPLETAN TODAS LAS ASIGNATURAS (3-15 semestres):");
        System.out.printf("   Total: %d estudiantes\n", completed);
        
        // 4. Clasificación por dificultad
        System.out.println("\n4. CLASIFICACIÓN DE ASIGNATURAS POR DIFICULTAD:");
        List<Map.Entry<String, Double>> difficultyRanking = getCoursesByDifficulty();
        int rank = 1;
        for (Map.Entry<String, Double> entry : difficultyRanking) {
            System.out.printf("   %d. %s (índice: %.2f)\n", rank++, entry.getKey(), entry.getValue());
        }
        
        // 5. Total de inscripciones por asignatura
        System.out.println("\n5. TOTAL DE INSCRIPCIONES POR ASIGNATURA:");
        Map<String, Integer> enrollments = getTotalEnrollmentsPerCourse();
        for (Map.Entry<String, Integer> entry : enrollments.entrySet()) {
            System.out.printf("   %s: %d inscripciones\n", entry.getKey(), entry.getValue());
        }
        
        // 6. Abandonos por semestre
        System.out.println("\n6. CANTIDAD DE ABANDONOS POR SEMESTRE:");
        Map<Integer, Integer> abandonments = getAbandonmentsPerSemester();
        if (abandonments.isEmpty()) {
            System.out.println("   No hubo abandonos");
        } else {
            abandonments.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> 
                        System.out.printf("   Semestre %d: %d abandonos\n", entry.getKey(), entry.getValue()));
        }
        
        // 7. Asignatura que causa más abandonos
        System.out.println("\n7. ASIGNATURA QUE PROVOCA MÁS ABANDONOS:");
        String courseWithMostAbandonments = getCourseWithMostAbandonments();
        Map<String, Course> courses = engine.getCourses();
        if (!courseWithMostAbandonments.equals("Ninguna")) {
            int abandonmentsCount = courses.get(courseWithMostAbandonments).getTotalAbandonments();
            System.out.printf("   %s con %d abandonos\n", courseWithMostAbandonments, abandonmentsCount);
        } else {
            System.out.println("   Ninguna");
        }
        
        System.out.println("\n" + "=".repeat(80));
    }
}

