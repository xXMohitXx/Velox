package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class TypeAnnotation extends AST {
    private final String typeName;

    public TypeAnnotation(Token token, String typeName) {
        super(token);
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor<R>) visitor).visitTypeAnnotation(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }
} 