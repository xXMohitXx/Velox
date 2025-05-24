package com.velox.compiler.lexer;

import com.velox.compiler.token.TokenType;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps keyword strings to their corresponding token types
 */
public class Keywords {
    private static final Map<String, TokenType> keywords = new HashMap<>();

    static {
        keywords.put("and", TokenType.AND);
        keywords.put("class", TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("fun", TokenType.FUN);
        keywords.put("if", TokenType.IF);
        keywords.put("nil", TokenType.NIL);
        keywords.put("or", TokenType.OR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("var", TokenType.VAR);
        keywords.put("while", TokenType.WHILE);
    }

    public static TokenType get(String keyword) {
        return keywords.get(keyword);
    }
} 