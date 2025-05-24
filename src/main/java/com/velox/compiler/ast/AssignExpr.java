package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Assignment expression node
 */
public class AssignExpr extends AST {
    private final AST value;

    public AssignExpr(Token name, AST value) {
        super(name);
        this.value = value;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitAssignExpr(this);
    }

    public AST getValue() { return value; }
} 