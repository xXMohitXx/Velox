package com.velox.compiler.ast;

/**
 * Grouping expression node
 */
public class GroupingExpr extends AST {
    private final AST expression;

    public GroupingExpr(AST expression) {
        super(null);
        this.expression = expression;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitGroupingExpr(this);
    }

    public AST getExpression() { return expression; }
} 