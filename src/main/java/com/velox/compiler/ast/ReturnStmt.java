package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class ReturnStmt extends AST {
    private final AST value;

    public ReturnStmt(Token token, AST value) {
        super(token);
        this.value = value;
    }

    public AST getValue() {
        return value;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor<R>) visitor).visitReturnStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }
} 