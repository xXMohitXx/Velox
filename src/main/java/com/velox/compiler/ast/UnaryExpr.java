package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Unary expression node
 */
public class UnaryExpr extends AST {
    private final Token operator;
    private final AST right;

    public UnaryExpr(Token operator, AST right) {
        super(operator);
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitUnaryExpr(this);
    }

    public Token getOperator() { return operator; }
    public AST getRight() { return right; }
} 