package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.ast.Stmt;
import com.velox.compiler.ast.StmtVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents an if statement in the AST.
 */
public class IfStmt extends Stmt {
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
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitIfStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    @Override
    public Object accept(StmtVisitor visitor) {
        return visitor.visitIfStmt(this);
    }
} 