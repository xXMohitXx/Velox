package com.velox.compiler.error;

/**
 * Represents an error that occurs during lexical analysis.
 */
public class LexicalError extends RuntimeException {
    public LexicalError(String message) {
        super(message);
    }

    public LexicalError(String message, Throwable cause) {
        super(message, cause);
    }
} 