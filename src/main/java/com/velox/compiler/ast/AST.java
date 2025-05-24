package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Base class for all AST nodes
 */
public abstract class AST {
    private final Token token;

    protected AST(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public abstract <R> R accept(ASTVisitor<R> visitor);
} 