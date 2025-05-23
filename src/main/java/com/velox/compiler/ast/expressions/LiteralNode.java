package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.token.Token;

public class LiteralNode extends ASTNode {
    private final Object value;

    public LiteralNode(Token token, Object value) {
        super(token);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitLiteral(this);
    }
} 