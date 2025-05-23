package com.velox.compiler.token;

/**
 * Represents a lexical token in the Velox language.
 */
public class Token {
    private TokenType type;
    private String lexeme;
    private Object literal;
    private int line;
    private int column;

    public Token(TokenType type, String lexeme, Object literal, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.column = column;
    }

    public void reset(TokenType type, String lexeme, Object literal, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Object getLiteral() {
        return literal;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("Token{type=%s, lexeme='%s', literal=%s, line=%d, column=%d}",
                type, lexeme, literal, line, column);
    }
} 