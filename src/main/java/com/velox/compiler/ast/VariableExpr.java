package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Variable expression node
 */
public class VariableExpr extends AST {
    public VariableExpr(Token name) {
        super(name);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitVariableExpr(this);
    }
} 