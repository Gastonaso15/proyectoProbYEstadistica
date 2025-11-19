package proyecto;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import java.util.*;

/**
 * Motor de simulación del sistema académico.
 * 
 * VALORES MODIFICABLES: Ver clase SimulationConfig.java
 * Todos los parámetros modificables están centralizados en SimulationConfig.
 */
public class SimulationEngine {
    
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
     * PARÁMETROS MODIFICABLES: Ver SimulationConfig.java
     * Los valores de media, varianza y lambda se obtienen de la clase SimulationConfig.
     */
    private void initializeCourses() {
        // ========== PRIMER BLOQUE: A -> B -> C ==========
        // PARÁMETROS MODIFICABLES: Ver SimulationConfig.java
        
        // Asignatura A (sin prerrequisito) - Distribución Normal
        Course A = new Course("A", null, Course.DistributionType.NORMAL, 
                             SimulationConfig.COURSE_A_MEAN, 
                             SimulationConfig.COURSE_A_VARIANCE, 
                             SimulationConfig.COURSE_A_LAMBDA);
        
        // Asignatura B (requiere A aprobada) - Distribución Normal
        Course B = new Course("B", A, Course.DistributionType.NORMAL, 
                             SimulationConfig.COURSE_B_MEAN, 
                             SimulationConfig.COURSE_B_VARIANCE, 
                             SimulationConfig.COURSE_B_LAMBDA);
        
        // Asignatura C (requiere B aprobada) - Distribución Poisson
        Course C = new Course("C", B, Course.DistributionType.POISSON, 
                             SimulationConfig.COURSE_C_MEAN, 
                             SimulationConfig.COURSE_C_VARIANCE, 
                             SimulationConfig.COURSE_C_LAMBDA);
        
        // ========== SEGUNDO BLOQUE: D -> E -> F ==========
        
        // Asignatura D (sin prerrequisito) - Distribución Normal
        Course D = new Course("D", null, Course.DistributionType.NORMAL, 
                             SimulationConfig.COURSE_D_MEAN, 
                             SimulationConfig.COURSE_D_VARIANCE, 
                             SimulationConfig.COURSE_D_LAMBDA);
        
        // Asignatura E (requiere D aprobada) - Distribución Normal
        Course E = new Course("E", D, Course.DistributionType.NORMAL, 
                             SimulationConfig.COURSE_E_MEAN, 
                             SimulationConfig.COURSE_E_VARIANCE, 
                             SimulationConfig.COURSE_E_LAMBDA);
        
        // Asignatura F (requiere E aprobada) - Distribución Poisson
        Course F = new Course("F", E, Course.DistributionType.POISSON, 
                             SimulationConfig.COURSE_F_MEAN, 
                             SimulationConfig.COURSE_F_VARIANCE, 
                             SimulationConfig.COURSE_F_LAMBDA);
        
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
        for (int i = 1; i <= SimulationConfig.MAX_STUDENTS; i++) {
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
        if (score >= SimulationConfig.PASSING_SCORE) {
            student.passCourse(courseName);
            // Calcular tiempo de aprobación (semestres desde primera inscripción)
            int firstSemester = student.getFirstEnrollmentSemester(courseName);
            double passingTime = semester - firstSemester + 1; // +1 porque cuenta el semestre actual
            course.addPassingTime(passingTime);
        } else {
            // Si no aprueba y alcanzó el máximo de intentos, abandona
            if (student.getAttempts(courseName) >= SimulationConfig.MAX_ATTEMPTS_PER_COURSE) {
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
        return SimulationConfig.MAX_STUDENTS;
    }
    
}

