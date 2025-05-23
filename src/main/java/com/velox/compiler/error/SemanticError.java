package com.velox.compiler.error;

import com.velox.compiler.token.Token;

public class SemanticError extends CompilationError {
    private final Token token;

    public SemanticError(Token token, String message) {
        super(message);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String getMessage() {
        return String.format("Semantic error at line %d, column %d: %s",
            token.getLine(), token.getColumn(), super.getMessage());
    }
} 