package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.ast.StmtVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents a while statement in the AST.
 */
public class WhileStmt implements AST {
    private final AST condition;
    private final AST body;

    public WhileStmt(Token token, AST condition, AST body) {
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
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitWhileStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }
} 