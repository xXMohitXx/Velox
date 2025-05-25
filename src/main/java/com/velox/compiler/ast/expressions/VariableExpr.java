package com.velox.compiler.ast.expressions;


import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Variable expression node
 */
public class VariableExpr extends ASTNode {
    private final Token name;

    public VariableExpr(Token name) {
        super(name);
        this.name = name;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitVariableExpr(this);
    }

    public Token getToken() { return name; }
} 