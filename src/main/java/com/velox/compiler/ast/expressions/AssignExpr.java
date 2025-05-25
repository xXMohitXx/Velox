package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.Expression;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents an assignment expression in the AST.
 */
public class AssignExpr extends Expression {
    private final Token name;
    private final Expression value;

    public AssignExpr(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public Token getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitAssignExpr(this);
    }
} 