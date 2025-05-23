package com.velox.compiler.optimizer;

public enum OptimizationLevel {
    NONE(0),
    BASIC(1),
    MODERATE(2),
    AGGRESSIVE(3);

    private final int level;

    OptimizationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isAtLeast(OptimizationLevel other) {
        return this.level >= other.level;
    }
} 