package com.velox.compiler.error;

public class RuntimeError extends RuntimeException {
    private final int instructionIndex;

    public RuntimeError(String message, int instructionIndex) {
        super(message);
        this.instructionIndex = instructionIndex;
    }

    public RuntimeError(String message) {
        super(message);
        this.instructionIndex = -1;
    }

    public RuntimeError(String message, Throwable cause) {
        super(message, cause);
        this.instructionIndex = -1;
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