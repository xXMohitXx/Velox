package com.velox.compiler;

import com.velox.compiler.util.PerformanceMetrics;
import java.util.HashMap;
import java.util.Map;

/**
 * Tracks performance metrics during compilation
 */
public class PerformanceMonitor {
    private final Map<String, Long> phaseStartTimes;
    private final Map<String, Long> phaseDurations;
    private String currentPhase;

    public PerformanceMonitor() {
        this.phaseStartTimes = new HashMap<>();
        this.phaseDurations = new HashMap<>();
    }

    public void startPhase(String phaseName) {
        currentPhase = phaseName;
        phaseStartTimes.put(phaseName, System.nanoTime());
    }

    public void endPhase(String phaseName) {
        if (currentPhase != null && currentPhase.equals(phaseName)) {
            long duration = System.nanoTime() - phaseStartTimes.get(phaseName);
            phaseDurations.put(phaseName, duration);
            currentPhase = null;
        }
    }

    public PerformanceMetrics getMetrics() {
        Map<String, Double> averages = new HashMap<>();
        Map<String, Long> totals = new HashMap<>();
        Map<String, Long> maximums = new HashMap<>();

        for (Map.Entry<String, Long> entry : phaseDurations.entrySet()) {
            String phase = entry.getKey();
            long duration = entry.getValue();
            averages.put(phase, (double) duration);
            totals.put(phase, duration);
            maximums.put(phase, duration);
        }

        return new PerformanceMetrics(averages, totals, maximums);
    }
} 