package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.token.Token;
import com.velox.compiler.ast.TypeAnnotationNode;

public class VariableDeclarationNode extends ASTNode {
    private final String name;
    private final TypeAnnotationNode type;
    private final ASTNode initializer;
    private final boolean isConst;

    public VariableDeclarationNode(
        Token token,
        String name,
        TypeAnnotationNode type,
        ASTNode initializer,
        boolean isConst
    ) {
        super(token);
        this.name = name;
        this.type = type;
        this.initializer = initializer;
        this.isConst = isConst;
    }

    public String getName() {
        return name;
    }

    public TypeAnnotationNode getType() {
        return type;
    }

    public ASTNode getInitializer() {
        return initializer;
    }

    public boolean isConst() {
        return isConst;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitVariableDeclaration(this);
    }
} 