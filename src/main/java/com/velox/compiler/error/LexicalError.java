package com.velox.compiler.error;

/**
 * Exception thrown when a lexical error occurs during tokenization
 */
public class LexicalError extends CompilationError {
    public LexicalError(String message) {
        super(message);
    }

    public LexicalError(String message, Throwable cause) {
        super(message, cause);
    }
} 