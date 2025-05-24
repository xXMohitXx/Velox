package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents a property access expression in the AST.
 */
public class GetExpr implements AST {
    private final AST object;
    private final Token name;

    public GetExpr(AST object, Token name) {
        this.object = object;
        this.name = name;
    }

    public AST getObject() {
        return object;
    }

    public Token getToken() {
        return name;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitGetExpr(this);
    }
} 