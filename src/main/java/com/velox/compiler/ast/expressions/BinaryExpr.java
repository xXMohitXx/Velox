package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

public class BinaryExpr implements AST {
    private final AST left;
    private final Token operator;
    private final AST right;

    public BinaryExpr(AST left, Token operator, AST right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public AST getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }

    public AST getRight() {
        return right;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitBinaryExpr(this);
    }
} 