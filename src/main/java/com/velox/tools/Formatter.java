package com.velox.tools;

import com.velox.compiler.lexer.Lexer;
import com.velox.compiler.token.Token;
import com.velox.compiler.token.TokenType;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class Formatter {
    private static final int DEFAULT_INDENT_SIZE = 4;
    private final int indentSize;
    
    public Formatter() {
        this(DEFAULT_INDENT_SIZE);
    }
    
    public Formatter(int indentSize) {
        this.indentSize = indentSize;
    }
    
    public String format(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize(source);
        return formatTokens(tokens);
    }
    
    private String formatTokens(List<Token> tokens) {
        StringBuilder result = new StringBuilder();
        int currentIndent = 0;
        boolean needSpace = false;
        boolean needNewline = false;
        Stack<Boolean> blockStack = new Stack<>();
        
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            TokenType type = token.getType();
            
            // Handle indentation
            if (needNewline) {
                result.append("\n");
                result.append(" ".repeat(currentIndent));
                needNewline = false;
            }
            
            // Handle spaces between tokens
            if (needSpace && !isPunctuation(type)) {
                result.append(" ");
            }
            
            // Process token
            switch (type) {
                case LEFT_BRACE -> {
                    result.append("{\n");
                    currentIndent += indentSize;
                    needNewline = true;
                    blockStack.push(true);
                }
                case RIGHT_BRACE -> {
                    currentIndent -= indentSize;
                    result.append("\n").append(" ".repeat(currentIndent)).append("}");
                    needNewline = true;
                    if (!blockStack.isEmpty()) blockStack.pop();
                }
                case LEFT_PAREN -> {
                    result.append("(");
                    needSpace = false;
                }
                case RIGHT_PAREN -> {
                    result.append(")");
                    needSpace = true;
                }
                case SEMICOLON -> {
                    result.append(";");
                    needNewline = true;
                }
                case COMMA -> {
                    result.append(",");
                    needSpace = true;
                }
                case DOT -> {
                    result.append(".");
                    needSpace = false;
                }
                case MODULE, IMPORT, EXPORT, FUN, CLASS, IF, ELSE, WHILE, FOR, RETURN, BREAK, CONTINUE -> {
                    if (needNewline) {
                        result.append("\n").append(" ".repeat(currentIndent));
                    }
                    result.append(token.getLexeme());
                    needSpace = true;
                }
                default -> {
                    result.append(token.getLexeme());
                    needSpace = true;
                }
            }
        }
        
        return result.toString();
    }
    
    private boolean isPunctuation(TokenType type) {
        return type == TokenType.DOT || 
               type == TokenType.COMMA || 
               type == TokenType.SEMICOLON ||
               type == TokenType.LEFT_PAREN ||
               type == TokenType.RIGHT_PAREN;
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Formatter <source_file>");
            return;
        }
        
        try {
            String source = java.nio.file.Files.readString(java.nio.file.Paths.get(args[0]));
            Formatter formatter = new Formatter();
            String formatted = formatter.format(source);
            System.out.println(formatted);
        } catch (Exception e) {
            System.err.println("Error formatting file: " + e.getMessage());
        }
    }
} 