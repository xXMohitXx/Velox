package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class VarStmt extends Stmt {
    private final Token name;
    private final AST initializer;

    public VarStmt(Token name, AST initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public Object accept(StmtVisitor visitor) {
        return visitor.visitVarStmt(this);
    }

    public Token getName() { return name; }
    public AST getInitializer() { return initializer; }
} 