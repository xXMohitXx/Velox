package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Base class for all statement nodes
 */
public abstract class Stmt implements AST {
    protected Token token;
    protected int startLine;
    protected int startColumn;
    protected int endLine;
    protected int endColumn;

    protected Stmt(Token token) {
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

    @Override
    public int getEndColumn() {
        return endColumn;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public abstract Object accept(StmtVisitor visitor);
} 