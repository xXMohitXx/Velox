package com.velox.compiler.token;

import java.util.Map;
import java.util.HashMap;

/**
 * Maps keyword strings to their corresponding TokenTypes.
 * Used by the lexer to identify keywords in the source code.
 */
public class Keywords {
    private static final Map<String, TokenType> keywords = new HashMap<>();

    static {
        // Module and Import
        keywords.put("module", TokenType.MODULE);
        keywords.put("import", TokenType.IMPORT);
        keywords.put("export", TokenType.EXPORT);
        keywords.put("package", TokenType.PACKAGE);

        // Class and Object
        keywords.put("class", TokenType.CLASS);
        keywords.put("interface", TokenType.INTERFACE);
        keywords.put("enum", TokenType.ENUM);
        keywords.put("struct", TokenType.STRUCT);
        keywords.put("extends", TokenType.EXTENDS);
        keywords.put("implements", TokenType.IMPLEMENTS);
        keywords.put("new", TokenType.NEW);
        keywords.put("this", TokenType.THIS);
        keywords.put("super", TokenType.SUPER);
        keywords.put("static", TokenType.STATIC);
        keywords.put("final", TokenType.FINAL);

        // Access Modifiers
        keywords.put("public", TokenType.PUBLIC);
        keywords.put("private", TokenType.PRIVATE);
        keywords.put("protected", TokenType.PROTECTED);
        keywords.put("internal", TokenType.INTERNAL);

        // Functions
        keywords.put("fun", TokenType.FUN);
        keywords.put("lambda", TokenType.LAMBDA);
        keywords.put("async", TokenType.ASYNC);
        keywords.put("await", TokenType.AWAIT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("yield", TokenType.YIELD);
        keywords.put("throw", TokenType.THROW);
        keywords.put("throws", TokenType.THROWS);

        // Control Flow
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("switch", TokenType.SWITCH);
        keywords.put("case", TokenType.CASE);
        keywords.put("default", TokenType.DEFAULT);
        keywords.put("while", TokenType.WHILE);
        keywords.put("do", TokenType.DO);
        keywords.put("for", TokenType.FOR);
        keywords.put("foreach", TokenType.FOREACH);
        keywords.put("in", TokenType.IN);
        keywords.put("break", TokenType.BREAK);
        keywords.put("continue", TokenType.CONTINUE);
        keywords.put("goto", TokenType.GOTO);

        // Error Handling
        keywords.put("try", TokenType.TRY);
        keywords.put("catch", TokenType.CATCH);
        keywords.put("finally", TokenType.FINALLY);
        keywords.put("except", TokenType.EXCEPT);

        // Variable Declaration
        keywords.put("var", TokenType.VAR);
        keywords.put("let", TokenType.LET);
        keywords.put("const", TokenType.CONST);
        keywords.put("mutable", TokenType.MUTABLE);
        keywords.put("immutable", TokenType.IMMUTABLE);

        // Type System
        keywords.put("type", TokenType.TYPE);
        keywords.put("void", TokenType.VOID);
        keywords.put("null", TokenType.NULL);
        keywords.put("nil", TokenType.NIL);
        keywords.put("int", TokenType.INT);
        keywords.put("float", TokenType.FLOAT);
        keywords.put("double", TokenType.DOUBLE);
        keywords.put("long", TokenType.LONG);
        keywords.put("bool", TokenType.BOOL);
        keywords.put("char", TokenType.CHAR);
        keywords.put("string", TokenType.STRING);
        keywords.put("array", TokenType.ARRAY);
        keywords.put("map", TokenType.MAP);
        keywords.put("set", TokenType.SET);
        keywords.put("list", TokenType.LIST);
        keywords.put("optional", TokenType.OPTIONAL);
        keywords.put("result", TokenType.RESULT);
        keywords.put("future", TokenType.FUTURE);
        keywords.put("generic", TokenType.GENERIC);

        // Logical Operators
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("not", TokenType.NOT);
        keywords.put("xor", TokenType.XOR);

        // Built-in Functions
        keywords.put("print", TokenType.PRINT);
        keywords.put("assert", TokenType.ASSERT);
        keywords.put("debug", TokenType.DEBUG);
        keywords.put("log", TokenType.LOG);

        // Boolean Literals
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
    }

    /**
     * Returns the TokenType for a given keyword string, or null if the string is not a keyword.
     * @param keyword The keyword string to look up
     * @return The corresponding TokenType, or null if not found
     */
    public static TokenType get(String keyword) {
        return keywords.get(keyword);
    }

    /**
     * Checks if a string is a keyword in the language.
     * @param str The string to check
     * @return true if the string is a keyword, false otherwise
     */
    public static boolean isKeyword(String str) {
        return keywords.containsKey(str);
    }

    private Keywords() {} // Prevent instantiation
} 