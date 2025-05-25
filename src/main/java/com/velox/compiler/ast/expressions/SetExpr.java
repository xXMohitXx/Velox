package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Property assignment expression node
 */
public class SetExpr extends ASTNode {
    private final AST object;
    private final Token name;
    private final AST value;

    public SetExpr(AST object, Token name, AST value) {
        super(name);
        this.object = object;
        this.name = name;
        this.value = value;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitSetExpr(this);
    }

    public AST getObject() { return object; }
    public Token getName() { return name; }
    public AST getValue() { return value; }
} 