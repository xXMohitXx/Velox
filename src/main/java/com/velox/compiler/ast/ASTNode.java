package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public abstract class ASTNode {
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

    public Token getToken() {
        return token;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public abstract <R> R accept(ASTVisitor<R> visitor);
} 