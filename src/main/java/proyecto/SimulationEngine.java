package proyecto;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import java.util.*;

/**
 * Motor de simulación del sistema académico.
 * 
 * VALORES MODIFICABLES (marcados en azul en el documento):
 * - Parámetros de distribuciones de las asignaturas (media, varianza, lambda)
 *   Ver método initializeCourses()
 * - Cantidad de estudiantes
 *   Ver constante MAX_STUDENTS
 * 
 * VALORES FIJOS (marcados en rojo en el documento):
 * - PASSING_SCORE = 60 (puntaje mínimo para aprobar)
 */
public class SimulationEngine {
    private static final int PASSING_SCORE = 60; // VALOR FIJO - Puntaje mínimo para aprobar
    
    // ========================================
    // ⚠️ VALOR MODIFICABLE ⚠️
    // ========================================
    // Cantidad de estudiantes a simular
    // MODIFICABLE: Cambiar este valor según se necesite
    private static final int MAX_STUDENTS = 120; // MODIFICABLE - Cantidad de estudiantes
    
    private RandomGenerator random;
    private List<Student> students;
    private Map<String, Course> courses;
    private Map<String, List<Integer>> enrollmentHistory; // historial de inscripciones por semestre
    
    public SimulationEngine() {
        this.random = new Well19937c(System.currentTimeMillis());
        this.students = new ArrayList<>();
        this.courses = new HashMap<>();
        this.enrollmentHistory = new HashMap<>();
        
        initializeCourses();
        initializeStudents();
    }
    
    /**
     * Inicializa las asignaturas con sus prerrequisitos.
     * 
     * ========================================
     * ⚠️ ZONA DE VALORES MODIFICABLES ⚠️
     * ========================================
     * 
     * A continuación se definen los parámetros de cada asignatura.
     * Estos valores están marcados como MODIFICABLES (en azul) en el documento.
     * 
     * Para asignaturas con distribución NORMAL:
     *   - Parámetro 1: media (mean)
     *   - Parámetro 2: varianza (variance)
     *   - Parámetro 3: lambda (no usado para Normal, poner 0)
     * 
     * Para asignaturas con distribución POISSON:
     *   - Parámetro 1: mean (no usado, poner 0)
     *   - Parámetro 2: variance (no usado, poner 0)
     *   - Parámetro 3: lambda (parámetro de Poisson)
     * 
     * Estructura: new Course(nombre, prerrequisito, tipo, mean, variance, lambda)
     */
    private void initializeCourses() {
        // ========== PRIMER BLOQUE: A -> B -> C ==========
        
        // Asignatura A (sin prerrequisito)
        // MODIFICABLE: media=70.0, varianza=15.0
        Course A = new Course("A", null, Course.DistributionType.NORMAL, 70.0, 15.0, 0);
        
        // Asignatura B (requiere A aprobada)
        // MODIFICABLE: media=65.0, varianza=18.0
        Course B = new Course("B", A, Course.DistributionType.NORMAL, 65.0, 18.0, 0);
        
        // Asignatura C (requiere B aprobada)
        // MODIFICABLE: lambda=60.0
        Course C = new Course("C", B, Course.DistributionType.POISSON, 0, 0, 60.0);
        
        // ========== SEGUNDO BLOQUE: D -> E -> F ==========
        
        // Asignatura D (sin prerrequisito)
        // MODIFICABLE: media=72.0, varianza=14.0
        Course D = new Course("D", null, Course.DistributionType.NORMAL, 72.0, 14.0, 0);
        
        // Asignatura E (requiere D aprobada)
        // MODIFICABLE: media=68.0, varianza=16.0
        Course E = new Course("E", D, Course.DistributionType.NORMAL, 68.0, 16.0, 0);
        
        // Asignatura F (requiere E aprobada)
        // MODIFICABLE: lambda=58.0
        Course F = new Course("F", E, Course.DistributionType.POISSON, 0, 0, 58.0);
        
        courses.put("A", A);
        courses.put("B", B);
        courses.put("C", C);
        courses.put("D", D);
        courses.put("E", E);
        courses.put("F", F);
        
        // Inicializar historial de inscripciones
        for (String courseName : courses.keySet()) {
            enrollmentHistory.put(courseName, new ArrayList<>());
        }
    }
    
    private void initializeStudents() {
        for (int i = 1; i <= MAX_STUDENTS; i++) {
            students.add(new Student(i));
        }
    }
    
    /**
     * Ejecuta la simulación completa.
     */
    public void runSimulation() {
        int semester = 0;
        int maxSemesters = 30; // Límite de semestres para evitar bucles infinitos
        
        while (semester < maxSemesters) {
            semester++;
            int activeStudents = 0;
            
            // Procesar cada estudiante
            for (Student student : students) {
                if (student.hasAbandoned() || student.hasCompletedAllCourses()) {
                    continue;
                }
                
                activeStudents++;
                student.incrementSemester();
                
                // Seleccionar asignaturas disponibles para inscribir
                List<Course> availableCourses = getAvailableCourses(student);
                
                // Máximo 2 asignaturas por semestre
                int coursesToEnroll = Math.min(2, availableCourses.size());
                
                for (int i = 0; i < coursesToEnroll && i < availableCourses.size(); i++) {
                    Course course = availableCourses.get(i);
                    enrollStudentInCourse(student, course, semester);
                }
            }
            
            // Si no hay estudiantes activos, terminar
            if (activeStudents == 0) {
                break;
            }
        }
    }
    
    /**
     * Obtiene las asignaturas disponibles para un estudiante.
     */
    private List<Course> getAvailableCourses(Student student) {
        List<Course> available = new ArrayList<>();
        
        // Orden de prioridad: primero A y D (sin prerrequisitos), luego B y E, luego C y F
        String[] priorityOrder = {"A", "D", "B", "E", "C", "F"};
        
        for (String courseName : priorityOrder) {
            Course course = courses.get(courseName);
            if (student.canEnroll(courseName, course)) {
                available.add(course);
                if (available.size() >= 2) {
                    break; // Máximo 2 por semestre
                }
            }
        }
        
        return available;
    }
    
    /**
     * Inscribe un estudiante en una asignatura y genera su nota.
     */
    private void enrollStudentInCourse(Student student, Course course, int semester) {
        String courseName = course.getName();
        
        // Registrar primera inscripción
        student.setFirstEnrollmentSemester(courseName, semester);
        
        // Incrementar intentos
        student.incrementAttempt(courseName);
        course.incrementEnrollments();
        enrollmentHistory.get(courseName).add(semester);
        
        // Generar nota según la distribución
        double score = generateScore(course);
        
        // Verificar si aprueba
        if (score >= PASSING_SCORE) {
            student.passCourse(courseName);
            // Calcular tiempo de aprobación (semestres desde primera inscripción)
            int firstSemester = student.getFirstEnrollmentSemester(courseName);
            double passingTime = semester - firstSemester + 1; // +1 porque cuenta el semestre actual
            course.addPassingTime(passingTime);
        } else {
            // Si no aprueba y alcanzó el máximo de intentos, abandona
            if (student.getAttempts(courseName) >= 5) {
                student.abandon(courseName);
                course.incrementAbandonments();
            }
        }
    }
    
    /**
     * Genera una nota según la distribución de la asignatura.
     */
    private double generateScore(Course course) {
        if (course.getDistributionType() == Course.DistributionType.NORMAL) {
            NormalDistribution normal = new NormalDistribution(
                random, course.getMean(), Math.sqrt(course.getVariance()));
            return normal.sample();
        } else { // POISSON
            PoissonDistribution poisson = new PoissonDistribution(course.getLambda());
            poisson.reseedRandomGenerator(random.nextLong());
            return poisson.sample();
        }
    }
    
    public List<Student> getStudents() {
        return students;
    }
    
    public Map<String, Course> getCourses() {
        return courses;
    }
    
    public Map<String, List<Integer>> getEnrollmentHistory() {
        return enrollmentHistory;
    }
    
    /**
     * Retorna la cantidad de estudiantes configurada para la simulación.
     * @return cantidad de estudiantes
     */
    public static int getMaxStudents() {
        return MAX_STUDENTS;
    }
    
    /**
     * Permite modificar los parámetros de una asignatura.
     */
    public void setCourseParameters(String courseName, Course.DistributionType type, 
                                   double mean, double variance, double lambda) {
        Course course = courses.get(courseName);
        if (course != null) {
            // Nota: esto requeriría modificar la clase Course para permitir cambios
            // Por ahora, asumimos que los parámetros se establecen en initializeCourses
        }
    }
}

