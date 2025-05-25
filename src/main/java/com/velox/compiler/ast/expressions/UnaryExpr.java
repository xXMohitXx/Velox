package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Unary expression node
 */
public class UnaryExpr extends ASTNode {
    private final Token operator;
    private final AST right;

    public UnaryExpr(Token operator, AST right) {
        super(operator);
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitUnaryExpr(this);
    }

    public Token getOperator() { return operator; }
    public AST getRight() { return right; }
} 