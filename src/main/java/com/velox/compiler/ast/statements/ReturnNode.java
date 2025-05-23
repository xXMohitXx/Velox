package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.token.Token;

public class ReturnNode extends ASTNode {
    private final ASTNode value;

    public ReturnNode(Token token, ASTNode value) {
        super(token);
        this.value = value;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitReturn(this);
    }
} 