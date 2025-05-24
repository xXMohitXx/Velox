package com.velox.compiler.ast;

import java.util.List;

public class BlockStmt extends Stmt {
    private final List<Stmt> statements;

    public BlockStmt(List<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    public <R> R accept(StmtVisitor<R> visitor) {
        return visitor.visitBlockStmt(this);
    }

    public List<Stmt> getStatements() { return statements; }
} 