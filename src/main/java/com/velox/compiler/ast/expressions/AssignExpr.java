package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents an assignment expression in the AST.
 */
public class AssignExpr implements AST {
    private final Token name;
    private final AST value;

    public AssignExpr(Token name, AST value) {
        this.name = name;
        this.value = value;
    }

    public Token getToken() {
        return name;
    }

    public AST getValue() {
        return value;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitAssignExpr(this);
    }
} 