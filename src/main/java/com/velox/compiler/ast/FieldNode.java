package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class FieldNode extends AST {
    private final String name;
    private final AST initializer;
    private final boolean isPublic;

    public FieldNode(Token token, String name, AST initializer, boolean isPublic) {
        super(token);
        this.name = name;
        this.initializer = initializer;
        this.isPublic = isPublic;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitField(this);
    }

    public String getName() { return name; }
    public AST getInitializer() { return initializer; }
    public boolean isPublic() { return isPublic; }
} 