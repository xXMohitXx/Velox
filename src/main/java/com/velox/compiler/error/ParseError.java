package com.velox.compiler.error;

import com.velox.compiler.token.Token;

public class ParseError extends CompilationError {
    private final Token token;

    public ParseError(Token token, String message) {
        super(message);
        this.token = token;
    }

    public ParseError(String message, Token token) {
        super(message + " at " + (token != null ? token.toString() : "unknown token"));
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String getMessage() {
        return String.format("Parse error at line %d, column %d: %s",
            token.getLine(), token.getColumn(), super.getMessage());
    }
} 