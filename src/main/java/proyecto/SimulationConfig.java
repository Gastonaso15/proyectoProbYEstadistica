package proyecto;

/**
 * Clase de configuración centralizada para todos los parámetros modificables de la simulación.
 * 
 * Todos los valores aquí pueden ser modificados según las necesidades del usuario.
 */
public class SimulationConfig {
    
    // ========================================
    // ⚠️ PARÁMETROS MODIFICABLES ⚠️
    // ========================================
    
    /**
     * Puntaje mínimo requerido para aprobar una asignatura (Punto 2).
     * Modificar este valor para cambiar el umbral de aprobación.
     */
    public static final double PASSING_SCORE = 60.0;
    
    /**
     * Máximo número de veces que un estudiante puede inscribirse en una asignatura (Punto 5).
     * Después de este número de intentos sin aprobar, el estudiante abandona.
     */
    public static final int MAX_ATTEMPTS_PER_COURSE = 5;
    
    /**
     * Rango mínimo de semestres para contar estudiantes que completan todas las asignaturas (Punto 3).
     * Modificar este valor para cambiar el límite inferior del rango.
     */
    public static final int MIN_SEMESTERS_COMPLETION = 3;
    
    /**
     * Rango máximo de semestres para contar estudiantes que completan todas las asignaturas (Punto 3).
     * Modificar este valor para cambiar el límite superior del rango.
     */
    public static final int MAX_SEMESTERS_COMPLETION = 15;
    
    /**
     * Cantidad de estudiantes a simular.
     */
    public static final int MAX_STUDENTS = 120;
    
    // ========================================
    // PARÁMETROS DE DISTRIBUCIONES (Punto 1)
    // ========================================
    
    /**
     * Parámetros para asignatura A (distribución Normal).
     * MODIFICABLE: cambiar media y varianza según se necesite.
     */
    public static final double COURSE_A_MEAN = 70.0;
    public static final double COURSE_A_VARIANCE = 15.0;
    public static final double COURSE_A_LAMBDA = 0.0; // No usado para Normal
    
    /**
     * Parámetros para asignatura B (distribución Normal).
     * MODIFICABLE: cambiar media y varianza según se necesite.
     */
    public static final double COURSE_B_MEAN = 65.0;
    public static final double COURSE_B_VARIANCE = 18.0;
    public static final double COURSE_B_LAMBDA = 0.0; // No usado para Normal
    
    /**
     * Parámetros para asignatura C (distribución Poisson).
     * MODIFICABLE: cambiar lambda (parámetro de Poisson) según se necesite.
     */
    public static final double COURSE_C_MEAN = 0.0; // No usado para Poisson
    public static final double COURSE_C_VARIANCE = 0.0; // No usado para Poisson
    public static final double COURSE_C_LAMBDA = 60.0;
    
    /**
     * Parámetros para asignatura D (distribución Normal).
     * MODIFICABLE: cambiar media y varianza según se necesite.
     */
    public static final double COURSE_D_MEAN = 72.0;
    public static final double COURSE_D_VARIANCE = 14.0;
    public static final double COURSE_D_LAMBDA = 0.0; // No usado para Normal
    
    /**
     * Parámetros para asignatura E (distribución Normal).
     * MODIFICABLE: cambiar media y varianza según se necesite.
     */
    public static final double COURSE_E_MEAN = 68.0;
    public static final double COURSE_E_VARIANCE = 16.0;
    public static final double COURSE_E_LAMBDA = 0.0; // No usado para Normal
    
    /**
     * Parámetros para asignatura F (distribución Poisson).
     * MODIFICABLE: cambiar lambda (parámetro de Poisson) según se necesite.
     */
    public static final double COURSE_F_MEAN = 0.0; // No usado para Poisson
    public static final double COURSE_F_VARIANCE = 0.0; // No usado para Poisson
    public static final double COURSE_F_LAMBDA = 58.0;
}

