package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class TypeAnnotation implements AST {
    private final String typeName;

    public TypeAnnotation(Token token, String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitTypeAnnotation(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }
} 