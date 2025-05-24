package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class IfStmt extends AST {
    private final AST condition;
    private final AST thenBranch;
    private final AST elseBranch;

    public IfStmt(Token token, AST condition, AST thenBranch, AST elseBranch) {
        super(token);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public AST getCondition() {
        return condition;
    }

    public AST getThenBranch() {
        return thenBranch;
    }

    public AST getElseBranch() {
        return elseBranch;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor<R>) visitor).visitIfStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }
} 