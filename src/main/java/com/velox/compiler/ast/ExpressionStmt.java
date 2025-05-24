package com.velox.compiler.ast;

public class ExpressionStmt extends Stmt {
    private final AST expression;

    public ExpressionStmt(AST expression) {
        this.expression = expression;
    }

    @Override
    public Object accept(StmtVisitor visitor) {
        return visitor.visitExpressionStmt(this);
    }

    public AST getExpression() { return expression; }
} 