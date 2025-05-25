package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.Expression;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents a property access expression in the AST.
 */
public class GetExpr extends Expression {
    private final Expression object;
    private final Token name;

    public GetExpr(Expression object, Token name) {
        this.object = object;
        this.name = name;
    }

    public Expression getObject() {
        return object;
    }

    public Token getName() {
        return name;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitGetExpr(this);
    }
} 