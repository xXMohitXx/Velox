package com.velox.compiler.error;

public class LexicalError extends CompilationError {
    public LexicalError(String message) {
        super(message);
    }

    public LexicalError(String message, Throwable cause) {
        super(message, cause);
    }
} 