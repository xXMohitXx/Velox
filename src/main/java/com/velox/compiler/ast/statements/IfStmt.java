package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;

/**
 * Represents an if statement in the AST.
 */
public class IfStmt implements AST {
    private final AST condition;
    private final AST thenBranch;
    private final AST elseBranch;

    public IfStmt(AST condition, AST thenBranch, AST elseBranch) {
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
    public Object accept(ASTVisitor visitor) {
        return visitor.visitIfStmt(this);
    }
} 