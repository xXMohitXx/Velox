package com.velox.compiler.error;

public class RuntimeError extends RuntimeException {
    private final int instructionIndex;

    public RuntimeError(String message, int instructionIndex) {
        super(message);
        this.instructionIndex = instructionIndex;
    }

    public int getInstructionIndex() {
        return instructionIndex;
    }

    @Override
    public String getMessage() {
        return String.format("Runtime error at instruction %d: %s", 
            instructionIndex, super.getMessage());
    }
} 