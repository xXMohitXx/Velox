package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Property assignment expression node
 */
public class SetExpr extends AST {
    private final AST object;
    private final AST value;

    public SetExpr(AST object, Token name, AST value) {
        super(name);
        this.object = object;
        this.value = value;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitSetExpr(this);
    }

    public AST getObject() { return object; }
    public AST getValue() { return value; }
} 