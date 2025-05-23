package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.token.Token;

public class IfNode extends ASTNode {
    private final ASTNode condition;
    private final ASTNode thenBranch;
    private final ASTNode elseBranch;

    public IfNode(Token token, ASTNode condition, ASTNode thenBranch, ASTNode elseBranch) {
        super(token);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getThenBranch() {
        return thenBranch;
    }

    public ASTNode getElseBranch() {
        return elseBranch;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitIf(this);
    }
} 