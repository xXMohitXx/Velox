package com.velox.compiler.ast;

public class PrintStmt extends Stmt {
    private final AST expression;

    public PrintStmt(AST expression) {
        this.expression = expression;
    }

    @Override
    public Object accept(StmtVisitor visitor) {
        return visitor.visitPrintStmt(this);
    }

    public AST getExpression() { return expression; }
} 