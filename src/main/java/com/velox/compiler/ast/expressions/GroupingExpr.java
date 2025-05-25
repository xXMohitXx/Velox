package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents a grouped expression in the AST.
 */
public class GroupingExpr extends ASTNode {
    private final AST expression;

    public GroupingExpr(Token token, AST expression) {
        super(token);
        this.expression = expression;
    }

    public AST getExpression() {
        return expression;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitGroupingExpr(this);
    }
} 