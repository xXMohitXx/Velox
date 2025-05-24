package com.velox.compiler;

/**
 * Defines the optimization levels available for the compiler
 */
public enum OptimizationLevel {
    NONE(0),
    BASIC(1),
    AGGRESSIVE(2);

    private final int level;

    OptimizationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
} 