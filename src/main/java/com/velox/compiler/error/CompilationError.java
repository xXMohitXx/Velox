package com.velox.compiler.error;

/**
 * Base class for all compilation errors
 */
public class CompilationError extends RuntimeException {
    public CompilationError(String message) {
        super(message);
    }

    public CompilationError(String message, Throwable cause) {
        super(message, cause);
    }
} 