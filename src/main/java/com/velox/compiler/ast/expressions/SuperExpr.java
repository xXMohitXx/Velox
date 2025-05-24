package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Super expression node
 */
public class SuperExpr implements AST {
    private final Token method;

    public SuperExpr(Token keyword, Token method) {
        this.method = method;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitSuperExpr(this);
    }

    public Token getMethod() { return method; }
} 