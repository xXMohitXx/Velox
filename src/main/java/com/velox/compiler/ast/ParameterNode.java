package com.velox.compiler.ast;

public class ParameterNode implements AST {
    private final String name;
    private final String type;

    public ParameterNode(String name, String type) {
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