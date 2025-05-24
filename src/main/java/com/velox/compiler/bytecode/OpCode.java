package com.velox.compiler.bytecode;

/**
 * Enumeration of bytecode operation codes.
 */
public enum OpCode {
    // Stack operations
    PUSH,
    POP,
    DUP,
    SWAP,

    // Arithmetic operations
    ADD,
    SUB,
    MUL,
    DIV,
    MOD,
    NEG,

    // Comparison operations
    EQ,
    NE,
    LT,
    LE,
    GT,
    GE,

    // Control flow operations
    JUMP,
    JUMP_IF_FALSE,
    JUMP_IF_TRUE,
    CALL,
    RETURN,

    // Variable operations
    GET_LOCAL,
    SET_LOCAL,
    GET_GLOBAL,
    SET_GLOBAL,

    // Object operations
    NEW,
    GET_PROPERTY,
    SET_PROPERTY,
    THIS,
    SUPER,

    // Module operations
    IMPORT,
    EXPORT,

    // Function operations
    PARAM,
    CLOSURE,

    // Special operations
    HALT
} 