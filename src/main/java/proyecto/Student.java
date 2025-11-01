package proyecto;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa un estudiante en la simulación.
 */
public class Student {
    private int id;
    private Map<String, Integer> attempts; // intentos por asignatura
    private Map<String, Boolean> passedCourses; // asignaturas aprobadas
    private Map<String, Integer> firstEnrollmentSemester; // semestre de primera inscripción
    private boolean abandoned;
    private int abandonmentSemester;
    private int currentSemester;
    
    public Student(int id) {
        this.id = id;
        this.attempts = new HashMap<>();
        this.passedCourses = new HashMap<>();
        this.firstEnrollmentSemester = new HashMap<>();
        this.abandoned = false;
        this.abandonmentSemester = -1;
        this.currentSemester = 0;
        
        // Inicializar contadores para todas las asignaturas
        for (String courseName : new String[]{"A", "B", "C", "D", "E", "F"}) {
            attempts.put(courseName, 0);
            passedCourses.put(courseName, false);
            firstEnrollmentSemester.put(courseName, -1);
        }
    }
    
    public int getId() {
        return id;
    }
    
    public boolean hasPassed(String courseName) {
        return passedCourses.getOrDefault(courseName, false);
    }
    
    public void passCourse(String courseName) {
        passedCourses.put(courseName, true);
    }
    
    public int getAttempts(String courseName) {
        return attempts.getOrDefault(courseName, 0);
    }
    
    public void incrementAttempt(String courseName) {
        attempts.put(courseName, attempts.getOrDefault(courseName, 0) + 1);
    }
    
    public boolean canEnroll(String courseName, Course course) {
        if (abandoned) {
            return false;
        }
        
        if (hasPassed(courseName)) {
            return false; // Ya aprobada
        }
        
        if (getAttempts(courseName) >= 5) {
            abandon(courseName);
            return false; // Máximo de intentos alcanzado
        }
        
        // Verificar prerrequisito
        if (course.getPrerequisite() != null) {
            String prereqName = course.getPrerequisite().getName();
            if (!hasPassed(prereqName)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean hasAbandoned() {
        return abandoned;
    }
    
    public void abandon(String courseName) {
        if (!abandoned) {
            this.abandoned = true;
            this.abandonmentSemester = currentSemester;
        }
    }
    
    public String getAbandonmentCourse() {
        // Retorna el nombre de la asignatura que causó el abandono
        // (la que tiene 5 intentos sin aprobar)
        for (Map.Entry<String, Integer> entry : attempts.entrySet()) {
            if (entry.getValue() >= 5 && !hasPassed(entry.getKey())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public int getAbandonmentSemester() {
        return abandonmentSemester;
    }
    
    public void incrementSemester() {
        this.currentSemester++;
    }
    
    public int getCurrentSemester() {
        return currentSemester;
    }
    
    public boolean hasCompletedAllCourses() {
        return passedCourses.values().stream().allMatch(Boolean::booleanValue);
    }
    
    public int getSemesterCompleted() {
        if (hasCompletedAllCourses()) {
            return currentSemester;
        }
        return -1; // No completó todas las asignaturas
    }
    
    public void setFirstEnrollmentSemester(String courseName, int semester) {
        if (firstEnrollmentSemester.get(courseName) == -1) {
            firstEnrollmentSemester.put(courseName, semester);
        }
    }
    
    public int getFirstEnrollmentSemester(String courseName) {
        return firstEnrollmentSemester.getOrDefault(courseName, -1);
    }
}

