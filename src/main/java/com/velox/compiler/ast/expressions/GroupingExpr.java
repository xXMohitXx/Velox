package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;

/**
 * Represents a grouped expression in the AST.
 */
public class GroupingExpr implements AST {
    private final AST expression;

    public GroupingExpr(AST expression) {
        this.expression = expression;
    }

    public AST getExpression() {
        return expression;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitGroupingExpr(this);
    }
} 