package com.velox.compiler;

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
        return new PerformanceMetrics(phaseDurations);
    }

    public static class PerformanceMetrics {
        private final Map<String, Long> phaseDurations;

        public PerformanceMetrics(Map<String, Long> phaseDurations) {
            this.phaseDurations = new HashMap<>(phaseDurations);
        }

        public long getPhaseDuration(String phaseName) {
            return phaseDurations.getOrDefault(phaseName, 0L);
        }

        public Map<String, Long> getAllPhaseDurations() {
            return new HashMap<>(phaseDurations);
        }
    }
} 