package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Binary expression node
 */
public class BinaryExpr extends AST {
    private final AST left;
    private final Token operator;
    private final AST right;

    public BinaryExpr(AST left, Token operator, AST right) {
        super(operator);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitBinaryExpr(this);
    }

    public AST getLeft() { return left; }
    public Token getOperator() { return operator; }
    public AST getRight() { return right; }
} 