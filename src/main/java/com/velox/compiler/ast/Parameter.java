package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class Parameter extends ASTNode {
    private final Token name;
    private final TypeAnnotation type;

    public Parameter(Token token, Token name, TypeAnnotation type) {
        super(token);
        this.name = name;
        this.type = type;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitParameter(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    public Token getName() { return name; }
    public TypeAnnotation getType() { return type; }
} 