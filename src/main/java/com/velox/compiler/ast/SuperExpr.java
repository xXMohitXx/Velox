package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Super expression node
 */
public class SuperExpr extends AST {
    private final Token method;

    public SuperExpr(Token keyword, Token method) {
        super(keyword);
        this.method = method;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitSuperExpr(this);
    }

    public Token getMethod() { return method; }
} 