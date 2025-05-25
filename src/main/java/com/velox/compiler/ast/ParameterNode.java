package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class ParameterNode extends ASTNode {
    private final String name;
    private final String type;

    public ParameterNode(Token token, String name, String type) {
        super(token);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitParameter(this);
    }
} 