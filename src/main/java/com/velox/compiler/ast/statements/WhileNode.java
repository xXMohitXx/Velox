package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.token.Token;

public class WhileNode extends ASTNode {
    private final ASTNode condition;
    private final ASTNode body;

    public WhileNode(Token token, ASTNode condition, ASTNode body) {
        super(token);
        this.condition = condition;
        this.body = body;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getBody() {
        return body;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitWhile(this);
    }
} 