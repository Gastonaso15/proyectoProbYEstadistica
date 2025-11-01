package proyecto;

/**
 * Clase principal que ejecuta la simulación completa del proyecto.
 */
public class Main {
    public static void main(String[] args) {
        // Obtener la cantidad de estudiantes configurada
        int cantidadEstudiantes = SimulationEngine.getMaxStudents();
        
        System.out.println("Iniciando simulación de " + cantidadEstudiantes + " estudiantes...");
        System.out.println("==========================================");
        
        // Crear y ejecutar la simulación
        SimulationEngine engine = new SimulationEngine();
        engine.runSimulation();
        
        // Generar estadísticas
        StatisticsGenerator statistics = new StatisticsGenerator(engine);
        
        // Imprimir reporte completo
        statistics.printFullReport();
        
        // Generar gráficos visuales
        GraphicsGenerator graphics = new GraphicsGenerator(statistics);
        graphics.showAllGraphics();
        
        System.out.println("\nSimulación completada exitosamente.");
    }
}

