package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.ast.Stmt;
import com.velox.compiler.ast.StmtVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents a return statement in the AST.
 */
public class ReturnStmt extends Stmt {
    private final AST value;

    public ReturnStmt(Token token, AST value) {
        super(token);
        this.value = value;
    }

    public AST getValue() {
        return value;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitReturnStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    @Override
    public Object accept(StmtVisitor visitor) {
        return visitor.visitReturnStmt(this);
    }
} 