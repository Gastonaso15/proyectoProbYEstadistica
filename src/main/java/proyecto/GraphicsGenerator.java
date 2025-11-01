package proyecto;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.*;

/**
 * Genera gráficos visuales (histogramas) usando JFreeChart.
 */
public class GraphicsGenerator {
    private StatisticsGenerator statistics;
    
    public GraphicsGenerator(StatisticsGenerator statistics) {
        this.statistics = statistics;
    }
    
    /**
     * 1. Genera un histograma visual del tiempo que le toma a cada estudiante
     *    avanzar las 6 asignaturas.
     */
    public void showCompletionTimeHistogram() {
        List<Double> completionTimes = statistics.getCompletionTimes();
        
        if (completionTimes.isEmpty()) {
            System.out.println("\nNo hay datos para generar el histograma de tiempos de finalización.");
            return;
        }
        
        // Calcular estadísticas
        Collections.sort(completionTimes);
        double min = completionTimes.get(0);
        double max = completionTimes.get(completionTimes.size() - 1);
        double promedio = completionTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        
        System.out.println("\nGenerando histograma visual...");
        System.out.printf("  Promedio: %.2f semestres\n", promedio);
        System.out.printf("  Estudiantes que completaron: %d\n", completionTimes.size());
        
        // Crear dataset para el histograma
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Crear bins
        int numBins = 15;
        double binWidth = (max - min) / numBins;
        
        int[] bins = new int[numBins];
        for (Double time : completionTimes) {
            int binIndex = Math.min((int) ((time - min) / binWidth), numBins - 1);
            bins[binIndex]++;
        }
        
        // Agregar datos al dataset
        for (int i = 0; i < numBins; i++) {
            double binStart = min + i * binWidth;
            double binEnd = min + (i + 1) * binWidth;
            String label = String.format("%.1f-%.1f", binStart, binEnd);
            dataset.addValue(bins[i], "Estudiantes", label);
        }
        
        // Crear el gráfico
        JFreeChart chart = ChartFactory.createBarChart(
            "Histograma: Tiempo que le toma a cada estudiante avanzar las 6 asignaturas",
            "Tiempo (Semestres)",
            "Cantidad de Estudiantes",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        // Personalizar el gráfico
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinesVisible(true);
        
        // Mostrar la ventana
        ChartFrame frame = new ChartFrame("Histograma - Tiempo de Finalización", chart);
        frame.pack();
        frame.setVisible(true);
        
        System.out.println("  Ventana del histograma mostrada.");
    }
    
    /**
     * Muestra el histograma visual del tiempo que le toma a cada estudiante
     * avanzar las 6 asignaturas.
     */
    public void showAllGraphics() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("GENERANDO GRÁFICO VISUAL");
        System.out.println("=".repeat(80));
        
        showCompletionTimeHistogram();
        
        System.out.println("\nNota: La ventana gráfica permanecerá abierta.");
        System.out.println("      Cierre la ventana para continuar o finalizar el programa.");
        System.out.println("=".repeat(80));
    }
}
