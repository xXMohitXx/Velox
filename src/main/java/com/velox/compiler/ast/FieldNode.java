package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Represents a field declaration in the AST.
 */
public class FieldNode extends ASTNode {
    private final Token name;
    private final AST type;
    private final AST initializer;
    private final Token accessModifier;

    public FieldNode(Token token, Token name, AST type, AST initializer, Token accessModifier) {
        super(token);
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