package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Property access expression node
 */
public class GetExpr extends AST {
    private final AST object;

    public GetExpr(AST object, Token name) {
        super(name);
        this.object = object;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitGetExpr(this);
    }

    public AST getObject() { return object; }
} 