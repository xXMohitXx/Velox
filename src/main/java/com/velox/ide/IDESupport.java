package com.velox.ide;

import com.velox.compiler.*;
import com.velox.compiler.ast.*;
import java.util.*;
import java.io.*;

public class IDESupport {
    private final Compiler compiler;
    private final Map<String, List<Diagnostic>> diagnostics;
    private final Map<String, List<CompletionItem>> completions;
    
    public IDESupport() {
        this.compiler = new Compiler();
        this.diagnostics = new HashMap<>();
        this.completions = new HashMap<>();
    }
    
    public List<Diagnostic> getDiagnostics(String filePath) {
        return diagnostics.getOrDefault(filePath, new ArrayList<>());
    }
    
    public List<CompletionItem> getCompletions(String filePath, int offset) {
        return completions.getOrDefault(filePath, new ArrayList<>());
    }
    
    public void updateFile(String filePath, String content) {
        try {
            // Parse and analyze the file
            ASTNode ast = compiler.parse(content);
            compiler.analyze(ast);
            
            // Update diagnostics
            diagnostics.put(filePath, compiler.getDiagnostics());
            
            // Update completions
            updateCompletions(filePath, ast, offset);
        } catch (Exception e) {
            diagnostics.put(filePath, Collections.singletonList(
                new Diagnostic(DiagnosticSeverity.ERROR, e.getMessage())
            ));
        }
    }
    
    private void updateCompletions(String filePath, ASTNode ast, int offset) {
        List<CompletionItem> items = new ArrayList<>();
        
        // Add keyword completions
        items.addAll(getKeywordCompletions());
        
        // Add variable completions
        items.addAll(getVariableCompletions(ast, offset));
        
        // Add function completions
        items.addAll(getFunctionCompletions(ast, offset));
        
        completions.put(filePath, items);
    }
    
    private List<CompletionItem> getKeywordCompletions() {
        List<CompletionItem> items = new ArrayList<>();
        String[] keywords = {
            "if", "else", "while", "for", "function",
            "return", "break", "continue", "var", "const"
        };
        
        for (String keyword : keywords) {
            items.add(new CompletionItem(
                keyword,
                CompletionItemKind.KEYWORD,
                "Velox keyword"
            ));
        }
        
        return items;
    }
    
    private List<CompletionItem> getVariableCompletions(ASTNode ast, int offset) {
        List<CompletionItem> items = new ArrayList<>();
        // Implementation to find variables in scope
        return items;
    }
    
    private List<CompletionItem> getFunctionCompletions(ASTNode ast, int offset) {
        List<CompletionItem> items = new ArrayList<>();
        // Implementation to find functions in scope
        return items;
    }
    
    // Diagnostic class for error reporting
    public static class Diagnostic {
        private final DiagnosticSeverity severity;
        private final String message;
        private final int line;
        private final int column;
        
        public Diagnostic(DiagnosticSeverity severity, String message) {
            this(severity, message, 0, 0);
        }
        
        public Diagnostic(DiagnosticSeverity severity, String message, int line, int column) {
            this.severity = severity;
            this.message = message;
            this.line = line;
            this.column = column;
        }
        
        public DiagnosticSeverity getSeverity() { return severity; }
        public String getMessage() { return message; }
        public int getLine() { return line; }
        public int getColumn() { return column; }
    }
    
    // Completion item class for code completion
    public static class CompletionItem {
        private final String label;
        private final CompletionItemKind kind;
        private final String detail;
        
        public CompletionItem(String label, CompletionItemKind kind, String detail) {
            this.label = label;
            this.kind = kind;
            this.detail = detail;
        }
        
        public String getLabel() { return label; }
        public CompletionItemKind getKind() { return kind; }
        public String getDetail() { return detail; }
    }
    
    // Enums for diagnostic severity and completion item kind
    public enum DiagnosticSeverity {
        ERROR, WARNING, INFO, HINT
    }
    
    public enum CompletionItemKind {
        KEYWORD, VARIABLE, FUNCTION, CLASS, INTERFACE,
        MODULE, PROPERTY, METHOD, CONSTRUCTOR, FIELD
    }
} 