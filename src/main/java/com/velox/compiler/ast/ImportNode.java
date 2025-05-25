package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Represents an import declaration in the AST.
 */
public class ImportNode extends ASTNode {
    private final Token moduleName;
    private final Token alias;

    public ImportNode(Token token, Token moduleName, Token alias) {
        super(token);
        this.moduleName = moduleName;
        this.alias = alias;
    }

    public Token getModuleName() {
        return moduleName;
    }

    public Token getAlias() {
        return alias;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitImport(this);
    }
} 