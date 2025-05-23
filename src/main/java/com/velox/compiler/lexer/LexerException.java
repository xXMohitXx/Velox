package com.velox.compiler.lexer;

/**
 * Exception thrown when a lexical error occurs during tokenization.
 */
public class LexerException extends RuntimeException {
    private final int line;
    private final int column;

    public LexerException(String message, int line, int column) {
        super(String.format("[%d:%d] %s", line, column, message));
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
} 