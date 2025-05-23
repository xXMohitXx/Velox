package com.velox.compiler.lexer;

/**
 * Enum representing all possible token types in the Velox language.
 */
public enum TokenType {
    // Single-character tokens
    LEFT_PAREN("("), RIGHT_PAREN(")"),
    LEFT_BRACE("{"), RIGHT_BRACE("}"),
    LEFT_BRACKET("["), RIGHT_BRACKET("]"),
    COMMA(","), DOT("."), SEMICOLON(";"),
    COLON(":"), ARROW("->"), RANGE(".."),
    QUESTION("?"), BANG("!"), DOUBLE_COLON("::"),

    // One or two character tokens
    EQUAL("="), EQUAL_EQUAL("=="),
    BANG_EQUAL("!="),
    LESS("<"), LESS_EQUAL("<="),
    GREATER(">"), GREATER_EQUAL(">="),
    PLUS("+"), PLUS_EQUAL("+="),
    MINUS("-"), MINUS_EQUAL("-="),
    STAR("*"), STAR_EQUAL("*="),
    SLASH("/"), SLASH_EQUAL("/="),
    PERCENT("%"), PERCENT_EQUAL("%="),
    STAR_STAR("**"), STAR_STAR_EQUAL("**="),
    AND("&&"), OR("||"),

    // Bitwise operators
    BIT_AND("&"), BIT_OR("|"), BIT_XOR("^"),
    BIT_NOT("~"), LEFT_SHIFT("<<"), RIGHT_SHIFT(">>"),
    UNSIGNED_RIGHT_SHIFT(">>>"),

    // Literals
    IDENTIFIER, STRING, NUMBER, FLOAT,

    // Keywords
    MODULE, IMPORT, EXPORT,
    FN, CLASS, NEW, THIS,
    LET, CONST,
    IF, ELSE, FOR, WHILE, DO,
    BREAK, CONTINUE, RETURN,
    TRY, CATCH, THROW,
    ASYNC, AWAIT, SPAWN,
    PUB, PRIVATE,

    // Types
    INT, FLOAT_TYPE, BOOL, STRING_TYPE, CHAR, VOID,
    RESULT, OPTIONAL,

    // Special tokens
    EOF;

    private final String lexeme;

    TokenType() {
        this.lexeme = null;
    }

    TokenType(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }
} 