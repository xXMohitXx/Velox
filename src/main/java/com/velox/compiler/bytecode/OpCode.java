package com.velox.compiler.bytecode;

/**
 * Enumeration of bytecode operation codes.
 */
public enum OpCode {
    // Constants
    CONSTANT,
    NIL,
    TRUE,
    FALSE,

    // Arithmetic
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    NEGATE,
    NOT,

    // Comparison
    EQUAL,
    NOT_EQUAL,
    LESS,
    LESS_EQUAL,
    GREATER,
    GREATER_EQUAL,

    // Variables
    GET_LOCAL,
    SET_LOCAL,
    GET_GLOBAL,
    SET_GLOBAL,

    // Properties
    GET_PROPERTY,
    SET_PROPERTY,

    // Control flow
    JUMP,
    JUMP_IF_FALSE,
    LOOP,
    CALL,
    RETURN,

    // Stack operations
    POP,
    PRINT,

    // Module operations
    IMPORT,

    // Type system
    TYPE_INFO
} 