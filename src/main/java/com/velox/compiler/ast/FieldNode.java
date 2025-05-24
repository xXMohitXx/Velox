package com.velox.compiler.ast;

import com.velox.compiler.lexer.Token;

/**
 * Represents a field declaration in the AST.
 */
public class FieldNode implements AST {
    private final Token name;
    private final AST type;
    private final AST initializer;
    private final Token accessModifier;

    public FieldNode(Token name, AST type, AST initializer, Token accessModifier) {
        this.name = name;
        this.type = type;
        this.initializer = initializer;
        this.accessModifier = accessModifier;
    }

    public Token getName() {
        return name;
    }

    public AST getType() {
        return type;
    }

    public AST getInitializer() {
        return initializer;
    }

    public Token getAccessModifier() {
        return accessModifier;
    }

    public boolean isFinal() {
        return accessModifier != null && accessModifier.getLexeme().equals("final");
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitField(this);
    }
} 