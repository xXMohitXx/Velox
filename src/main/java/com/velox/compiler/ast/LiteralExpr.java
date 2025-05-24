package com.velox.compiler.ast;

/**
 * Literal expression node
 */
public class LiteralExpr extends AST {
    private final Object value;

    public LiteralExpr(Object value) {
        super(null);
        this.value = value;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitLiteralExpr(this);
    }

    public Object getValue() { return value; }
} 