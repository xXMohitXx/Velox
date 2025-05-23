package com.velox.compiler.token;

import java.util.Map;
import java.util.HashMap;

public class Keywords {
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        
        // Module keywords
        keywords.put("module", TokenType.MODULE);
        keywords.put("import", TokenType.IMPORT);
        keywords.put("export", TokenType.EXPORT);

        // Function keywords
        keywords.put("fn", TokenType.FN);
        keywords.put("async", TokenType.ASYNC);
        keywords.put("await", TokenType.AWAIT);
        keywords.put("spawn", TokenType.SPAWN);

        // Class keywords
        keywords.put("class", TokenType.CLASS);
        keywords.put("new", TokenType.NEW);
        keywords.put("this", TokenType.THIS);

        // Variable keywords
        keywords.put("let", TokenType.LET);
        keywords.put("const", TokenType.CONST);

        // Control flow keywords
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("while", TokenType.WHILE);
        keywords.put("do", TokenType.DO);
        keywords.put("break", TokenType.BREAK);
        keywords.put("continue", TokenType.CONTINUE);
        keywords.put("return", TokenType.RETURN);

        // Error handling keywords
        keywords.put("try", TokenType.TRY);
        keywords.put("catch", TokenType.CATCH);
        keywords.put("throw", TokenType.THROW);

        // Access modifiers
        keywords.put("pub", TokenType.PUB);
        keywords.put("private", TokenType.PRIVATE);

        // Type keywords
        keywords.put("int", TokenType.INT);
        keywords.put("float", TokenType.FLOAT_TYPE);
        keywords.put("bool", TokenType.BOOL);
        keywords.put("string", TokenType.STRING_TYPE);
        keywords.put("char", TokenType.CHAR);
        keywords.put("void", TokenType.VOID);
        keywords.put("Result", TokenType.RESULT);
        keywords.put("Optional", TokenType.OPTIONAL);
    }

    public static TokenType get(String keyword) {
        return keywords.get(keyword);
    }

    private Keywords() {} // Prevent instantiation
} 