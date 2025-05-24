package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents a return statement in the AST.
 */
public class ReturnStmt implements AST {
    private final AST value;

    public ReturnStmt(Token token, AST value) {
        this.value = value;
    }

    public AST getValue() {
        return value;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitReturnStmt(this);
    }
} 