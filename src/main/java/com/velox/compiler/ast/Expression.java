package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

public abstract class Expression implements AST {
    private Token token;
    private int startLine;
    private int startColumn;
    private int endLine;
    private int endColumn;

    @Override
    public Token getToken() { return token; }
    public void setToken(Token token) { this.token = token; }

    @Override
    public int getStartLine() { return startLine; }
    public void setStartLine(int line) { this.startLine = line; }

    @Override
    public int getStartColumn() { return startColumn; }
    public void setStartColumn(int column) { this.startColumn = column; }

    @Override
    public int getEndLine() { return endLine; }
    public void setEndLine(int line) { this.endLine = line; }

    @Override
    public int getEndColumn() { return endColumn; }
    public void setEndColumn(int column) { this.endColumn = column; }
} 