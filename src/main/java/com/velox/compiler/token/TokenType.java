package com.velox.compiler.token;

public enum TokenType {
    // Single-character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    LEFT_BRACKET, RIGHT_BRACKET, COMMA, DOT, MINUS, PLUS,
    SEMICOLON, SLASH, STAR, COLON, QUESTION, ARROW,

    // One or two character tokens
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
    MINUS_EQUAL, PLUS_EQUAL,
    STAR_EQUAL, SLASH_EQUAL,
    PERCENT, PERCENT_EQUAL,

    // Bitwise operators
    BIT_AND, BIT_OR, BIT_XOR, BIT_NOT,
    LEFT_SHIFT, RIGHT_SHIFT, UNSIGNED_RIGHT_SHIFT,

    // Logical operators
    AND, OR, NOT,

    // Literals
    IDENTIFIER, STRING, NUMBER, FLOAT,

    // Keywords
    MODULE, IMPORT, EXPORT,
    FN, ASYNC, AWAIT, SPAWN,
    CLASS, NEW, THIS,
    LET, CONST,
    IF, ELSE, FOR, WHILE, DO,
    BREAK, CONTINUE, RETURN,
    TRY, CATCH, THROW,
    PUB, PRIVATE,
    INT, FLOAT_TYPE, BOOL, STRING_TYPE, CHAR, VOID,
    RESULT, OPTIONAL,

    // Special tokens
    EOF
} 