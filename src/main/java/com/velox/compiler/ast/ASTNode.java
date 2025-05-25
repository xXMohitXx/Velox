package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public abstract class ASTNode implements AST {
    private final Token token;
    private int startLine;
    private int startColumn;
    private int endLine;
    private int endColumn;

    protected ASTNode(Token token) {
        this.token = token;
        if (token != null) {
            this.startLine = token.getLine();
            this.startColumn = token.getColumn();
        }
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public int getStartLine() {
        return startLine;
    }

    @Override
    public int getStartColumn() {
        return startColumn;
    }

    @Override
    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    @Override
    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public abstract Object accept(ASTVisitor visitor);
} 