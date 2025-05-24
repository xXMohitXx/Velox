package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * This expression node
 */
public class ThisExpr extends AST {
    public ThisExpr(Token keyword) {
        super(keyword);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitThisExpr(this);
    }
} 
 