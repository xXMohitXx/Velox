package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.token.Token;

public class IdentifierNode extends ASTNode {
    private final String name;

    public IdentifierNode(Token token, String name) {
        super(token);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitIdentifier(this);
    }
} 