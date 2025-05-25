package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class VarStmt extends ASTNode {
    private final Token name;
    private final AST initializer;
    private final TypeAnnotation type;

    public VarStmt(Token token, Token name, AST initializer, TypeAnnotation type) {
        super(token);
        this.name = name;
        this.initializer = initializer;
        this.type = type;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitVarStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    public Token getName() { return name; }
    public AST getInitializer() { return initializer; }
    public TypeAnnotation getType() { return type; }
} 