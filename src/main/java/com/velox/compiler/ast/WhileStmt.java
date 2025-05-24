package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class WhileStmt extends AST {
    private final AST condition;
    private final AST body;

    public WhileStmt(Token token, AST condition, AST body) {
        super(token);
        this.condition = condition;
        this.body = body;
    }

    public AST getCondition() {
        return condition;
    }

    public AST getBody() {
        return body;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor<R>) visitor).visitWhileStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }
} 