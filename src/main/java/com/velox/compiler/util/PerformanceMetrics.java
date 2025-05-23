package com.velox.compiler.util;

import java.util.Map;

public class PerformanceMetrics {
    private final Map<String, Double> averages;
    private final Map<String, Long> totals;
    private final Map<String, Long> maximums;

    public PerformanceMetrics(
        Map<String, Double> averages,
        Map<String, Long> totals,
        Map<String, Long> maximums
    ) {
        this.averages = averages;
        this.totals = totals;
        this.maximums = maximums;
    }

    public double getAverageTime(String phase) {
        return averages.getOrDefault(phase, 0.0);
    }

    public long getTotalTime(String phase) {
        return totals.getOrDefault(phase, 0L);
    }

    public long getMaximumTime(String phase) {
        return maximums.getOrDefault(phase, 0L);
    }

    public Map<String, Double> getAverages() {
        return averages;
    }

    public Map<String, Long> getTotals() {
        return totals;
    }

    public Map<String, Long> getMaximums() {
        return maximums;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Performance Metrics:\n");
        
        for (String phase : averages.keySet()) {
            sb.append(String.format("%s:\n", phase));
            sb.append(String.format("  Average: %.2f ms\n", getAverageTime(phase) / 1_000_000.0));
            sb.append(String.format("  Total: %.2f ms\n", getTotalTime(phase) / 1_000_000.0));
            sb.append(String.format("  Maximum: %.2f ms\n", getMaximumTime(phase) / 1_000_000.0));
        }
        
        return sb.toString();
    }
} 