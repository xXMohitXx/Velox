package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;

/**
 * Literal expression node
 */
public class LiteralExpr implements AST {
    private final Object value;

    public LiteralExpr(Object value) {
        this.value = value;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitLiteralExpr(this);
    }

    public Object getValue() { return value; }
} 