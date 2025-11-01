package proyecto;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una asignatura del sistema académico.
 */
public class Course {
    private String name;
    private Course prerequisite;
    private DistributionType distributionType;
    private double mean;
    private double variance;
    private double lambda; // para distribución Poisson
    private int totalEnrollments;
    private List<Double> passingTimes; // tiempos para aprobar esta asignatura
    private int totalAbandonments;
    
    public enum DistributionType {
        NORMAL, POISSON
    }
    
    public Course(String name, Course prerequisite, DistributionType distributionType, 
                  double mean, double variance, double lambda) {
        this.name = name;
        this.prerequisite = prerequisite;
        this.distributionType = distributionType;
        this.mean = mean;
        this.variance = variance;
        this.lambda = lambda;
        this.totalEnrollments = 0;
        this.passingTimes = new ArrayList<>();
        this.totalAbandonments = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public Course getPrerequisite() {
        return prerequisite;
    }
    
    public DistributionType getDistributionType() {
        return distributionType;
    }
    
    public double getMean() {
        return mean;
    }
    
    public double getVariance() {
        return variance;
    }
    
    public double getLambda() {
        return lambda;
    }
    
    public void incrementEnrollments() {
        this.totalEnrollments++;
    }
    
    public int getTotalEnrollments() {
        return totalEnrollments;
    }
    
    public void addPassingTime(double semesters) {
        this.passingTimes.add(semesters);
    }
    
    public List<Double> getPassingTimes() {
        return passingTimes;
    }
    
    public double getAveragePassingTime() {
        if (passingTimes.isEmpty()) {
            return 0.0;
        }
        return passingTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    
    public void incrementAbandonments() {
        this.totalAbandonments++;
    }
    
    public int getTotalAbandonments() {
        return totalAbandonments;
    }
    
    public double getDifficultyIndex() {
        // Índice de dificultad basado en tiempo promedio y tasa de aprobación
        if (totalEnrollments == 0) {
            return 0.0;
        }
        double passRate = passingTimes.size() / (double) totalEnrollments;
        double avgTime = getAveragePassingTime();
        // Mayor tiempo promedio y menor tasa de aprobación = mayor dificultad
        return avgTime * (1.0 - passRate) * 100;
    }
}

