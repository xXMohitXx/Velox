package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Literal expression node
 */
public class LiteralExpr extends ASTNode {
    private final Object value;

    public LiteralExpr(Token token, Object value) {
        super(token);
        this.value = value;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitLiteralExpr(this);
    }

    public Object getValue() { return value; }
} 