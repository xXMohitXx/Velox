package com.velox.compiler.error;

public class CompilationError extends RuntimeException {
    public CompilationError(String message) {
        super(message);
    }

    public CompilationError(String message, Throwable cause) {
        super(message, cause);
    }
} 