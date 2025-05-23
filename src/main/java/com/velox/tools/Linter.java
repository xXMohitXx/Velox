package com.velox.tools;

import com.velox.compiler.lexer.Lexer;
import com.velox.compiler.lexer.Token;
import com.velox.compiler.lexer.TokenType;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Linter {
    private final List<LintError> errors;
    private final Map<String, Integer> variableUsage;
    private int currentLine;
    private int currentColumn;
    
    public Linter() {
        this.errors = new ArrayList<>();
        this.variableUsage = new HashMap<>();
        this.currentLine = 1;
        this.currentColumn = 0;
    }
    
    public List<LintError> lint(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        return analyzeTokens(tokens);
    }
    
    private List<LintError> analyzeTokens(List<Token> tokens) {
        errors.clear();
        variableUsage.clear();
        
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            currentLine = token.getLine();
            currentColumn = token.getColumn();
            
            switch (token.getType()) {
                case IDENTIFIER -> checkIdentifier(token);
                case FUNCTION -> checkFunctionDeclaration(tokens, i);
                case CLASS -> checkClassDeclaration(tokens, i);
                case IF, WHILE, FOR -> checkControlStructure(tokens, i);
                case RETURN -> checkReturnStatement(tokens, i);
                case SEMICOLON -> checkSemicolon(tokens, i);
                default -> {} // Skip other tokens
            }
        }
        
        // Check for unused variables
        variableUsage.forEach((name, count) -> {
            if (count == 0) {
                errors.add(new LintError(
                    "Variable '" + name + "' is declared but never used",
                    currentLine,
                    currentColumn,
                    LintError.Severity.WARNING
                ));
            }
        });
        
        return errors;
    }
    
    private void checkIdentifier(Token token) {
        String name = token.getLexeme();
        variableUsage.merge(name, 1, Integer::sum);
    }
    
    private void checkFunctionDeclaration(List<Token> tokens, int index) {
        // Check function name
        if (index + 1 >= tokens.size() || tokens.get(index + 1).getType() != TokenType.IDENTIFIER) {
            errors.add(new LintError(
                "Function declaration must have a name",
                currentLine,
                currentColumn,
                LintError.Severity.ERROR
            ));
            return;
        }
        
        // Check parameters
        int paramIndex = index + 2;
        if (paramIndex >= tokens.size() || tokens.get(paramIndex).getType() != TokenType.LEFT_PAREN) {
            errors.add(new LintError(
                "Function declaration must have parameters",
                currentLine,
                currentColumn,
                LintError.Severity.ERROR
            ));
            return;
        }
        
        // Check function body
        int bodyIndex = findMatchingBrace(tokens, paramIndex);
        if (bodyIndex == -1) {
            errors.add(new LintError(
                "Function declaration must have a body",
                currentLine,
                currentColumn,
                LintError.Severity.ERROR
            ));
        }
    }
    
    private void checkClassDeclaration(List<Token> tokens, int index) {
        // Check class name
        if (index + 1 >= tokens.size() || tokens.get(index + 1).getType() != TokenType.IDENTIFIER) {
            errors.add(new LintError(
                "Class declaration must have a name",
                currentLine,
                currentColumn,
                LintError.Severity.ERROR
            ));
            return;
        }
        
        // Check class body
        int bodyIndex = index + 2;
        if (bodyIndex >= tokens.size() || tokens.get(bodyIndex).getType() != TokenType.LEFT_BRACE) {
            errors.add(new LintError(
                "Class declaration must have a body",
                currentLine,
                currentColumn,
                LintError.Severity.ERROR
            ));
            return;
        }
        
        // Check for at least one method
        boolean hasMethod = false;
        for (int i = bodyIndex + 1; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenType.FUNCTION) {
                hasMethod = true;
                break;
            }
        }
        
        if (!hasMethod) {
            errors.add(new LintError(
                "Class should have at least one method",
                currentLine,
                currentColumn,
                LintError.Severity.WARNING
            ));
        }
    }
    
    private void checkControlStructure(List<Token> tokens, int index) {
        // Check condition
        if (index + 1 >= tokens.size() || tokens.get(index + 1).getType() != TokenType.LEFT_PAREN) {
            errors.add(new LintError(
                "Control structure must have a condition",
                currentLine,
                currentColumn,
                LintError.Severity.ERROR
            ));
            return;
        }
        
        // Check body
        int bodyIndex = findMatchingBrace(tokens, index + 1);
        if (bodyIndex == -1) {
            errors.add(new LintError(
                "Control structure must have a body",
                currentLine,
                currentColumn,
                LintError.Severity.ERROR
            ));
        }
    }
    
    private void checkReturnStatement(List<Token> tokens, int index) {
        // Check if return is inside a function
        boolean inFunction = false;
        for (int i = index; i >= 0; i--) {
            if (tokens.get(i).getType() == TokenType.FUNCTION) {
                inFunction = true;
                break;
            }
        }
        
        if (!inFunction) {
            errors.add(new LintError(
                "Return statement must be inside a function",
                currentLine,
                currentColumn,
                LintError.Severity.ERROR
            ));
        }
    }
    
    private void checkSemicolon(List<Token> tokens, int index) {
        // Check for missing semicolon after statement
        if (index > 0 && tokens.get(index - 1).getType() == TokenType.RIGHT_BRACE) {
            errors.add(new LintError(
                "Semicolon not needed after closing brace",
                currentLine,
                currentColumn,
                LintError.Severity.WARNING
            ));
        }
    }
    
    private int findMatchingBrace(List<Token> tokens, int startIndex) {
        int braceCount = 0;
        for (int i = startIndex; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == TokenType.LEFT_BRACE) {
                braceCount++;
            } else if (token.getType() == TokenType.RIGHT_BRACE) {
                braceCount--;
                if (braceCount == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static class LintError {
        public enum Severity {
            ERROR,
            WARNING,
            INFO
        }
        
        private final String message;
        private final int line;
        private final int column;
        private final Severity severity;
        
        public LintError(String message, int line, int column, Severity severity) {
            this.message = message;
            this.line = line;
            this.column = column;
            this.severity = severity;
        }
        
        @Override
        public String toString() {
            return String.format("%s:%d:%d: %s: %s",
                severity.name().toLowerCase(),
                line,
                column,
                severity.name(),
                message
            );
        }
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Linter <source_file>");
            return;
        }
        
        try {
            String source = java.nio.file.Files.readString(java.nio.file.Paths.get(args[0]));
            Linter linter = new Linter();
            List<LintError> errors = linter.lint(source);
            
            if (errors.isEmpty()) {
                System.out.println("No linting errors found.");
            } else {
                errors.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("Error linting file: " + e.getMessage());
        }
    }
} 