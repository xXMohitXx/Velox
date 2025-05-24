package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public class ImportNode extends AST {
    private final String moduleName;
    private final String alias;

    public ImportNode(Token token, String moduleName, String alias) {
        super(token);
        this.moduleName = moduleName;
        this.alias = alias;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor<R>) visitor).visitImport(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    public String getModuleName() { return moduleName; }
    public String getAlias() { return alias; }
} 