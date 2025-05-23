package com.velox.compiler.util;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class PerformanceMonitor {
    private final Map<String, List<Long>> phaseTimes;
    private final Map<String, Long> phaseStarts;
    private final Map<String, Long> thresholds;

    public PerformanceMonitor() {
        this.phaseTimes = new HashMap<>();
        this.phaseStarts = new HashMap<>();
        this.thresholds = new HashMap<>();
    }

    public void startPhase(String phase) {
        phaseStarts.put(phase, System.nanoTime());
    }

    public void endPhase(String phase) {
        long startTime = phaseStarts.remove(phase);
        if (startTime > 0) {
            long duration = System.nanoTime() - startTime;
            phaseTimes.computeIfAbsent(phase, k -> new ArrayList<>()).add(duration);
            
            Long threshold = thresholds.get(phase);
            if (threshold != null && duration > threshold) {
                alert(phase, duration);
            }
        }
    }

    public void setThreshold(String phase, long thresholdNanos) {
        thresholds.put(phase, thresholdNanos);
    }

    public PerformanceMetrics getMetrics() {
        Map<String, Double> averages = new HashMap<>();
        Map<String, Long> totals = new HashMap<>();
        Map<String, Long> maximums = new HashMap<>();

        for (Map.Entry<String, List<Long>> entry : phaseTimes.entrySet()) {
            String phase = entry.getKey();
            List<Long> times = entry.getValue();
            
            if (!times.isEmpty()) {
                long total = times.stream().mapToLong(Long::longValue).sum();
                double average = total / (double) times.size();
                long maximum = times.stream().mapToLong(Long::longValue).max().orElse(0);
                
                averages.put(phase, average);
                totals.put(phase, total);
                maximums.put(phase, maximum);
            }
        }

        return new PerformanceMetrics(averages, totals, maximums);
    }

    private void alert(String phase, long duration) {
        System.err.printf("Performance alert: %s took %.2f ms%n", 
            phase, duration / 1_000_000.0);
    }

    public void clear() {
        phaseTimes.clear();
        phaseStarts.clear();
    }
} 