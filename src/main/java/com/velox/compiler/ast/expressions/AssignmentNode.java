package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.token.Token;

public class AssignmentNode extends ASTNode {
    private final String name;
    private final ASTNode value;

    public AssignmentNode(Token token, String name, ASTNode value) {
        super(token);
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitAssignment(this);
    }
} 