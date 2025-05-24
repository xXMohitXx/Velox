package com.velox.compiler.token;

/**
 * Enumeration of all possible token types in the Velox language
 */
public enum TokenType {
    // Single-character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    LEFT_BRACKET, RIGHT_BRACKET, COMMA, DOT, MINUS, PLUS,
    SEMICOLON, SLASH, STAR, COLON, QUESTION, AT,

    // One or two character tokens
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
    PLUS_EQUAL, MINUS_EQUAL,
    STAR_EQUAL, SLASH_EQUAL,
    ARROW, DOUBLE_ARROW,
    MINUS_MINUS, PLUS_PLUS,

    // Literals
    IDENTIFIER, NUMBER, TRUE, FALSE,

    // Keywords - Module and Import
    MODULE, IMPORT, EXPORT, PACKAGE,

    // Keywords - Class and Object
    CLASS, INTERFACE, ENUM, STRUCT,
    EXTENDS, IMPLEMENTS, NEW,
    THIS, SUPER, STATIC, FINAL, ABSTRACT,

    // Keywords - Access Modifiers
    PUBLIC, PRIVATE, PROTECTED, INTERNAL,

    // Keywords - Functions
    FUN, LAMBDA, ASYNC, AWAIT,
    RETURN, YIELD, THROW, THROWS,

    // Keywords - Control Flow
    IF, ELSE, SWITCH, CASE, DEFAULT,
    WHILE, DO, FOR, FOREACH, IN,
    BREAK, CONTINUE, GOTO,

    // Keywords - Error Handling
    TRY, CATCH, FINALLY, EXCEPT,

    // Keywords - Variable Declaration
    VAR, LET, CONST, MUTABLE, IMMUTABLE,

    // Keywords - Type System
    TYPE, VOID, NULL, NIL,
    INT, FLOAT, DOUBLE, LONG,
    BOOL, CHAR, STRING, ARRAY,
    MAP, SET, LIST, OPTIONAL,
    RESULT, FUTURE, GENERIC,

    // Keywords - Logical Operators
    AND, OR, NOT, XOR,

    // Keywords - Built-in Functions
    PRINT, ASSERT, DEBUG, LOG,

    // Special
    EOF
} 