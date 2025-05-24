package com.velox.compiler.semantic;

import com.velox.compiler.ast.*;
import com.velox.compiler.error.ErrorHandler;
import java.util.*;

public class SemanticAnalyzer {
    private final ErrorHandler errorHandler;
    private final Stack<Scope> scopes;

    public SemanticAnalyzer(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.scopes = new Stack<>();
    }

    public void analyze(ModuleNode module) {
        enterScope();
        try {
            // Analyze imports
            for (ImportNode importNode : module.getImports()) {
                analyzeImport(importNode);
            }

            // Analyze declarations
            for (AST declaration : module.getDeclarations()) {
                analyzeDeclaration(declaration);
            }
        } finally {
            exitScope();
        }
    }

    private void analyzeImport(ImportNode importNode) {
        // TODO: Implement import analysis
    }

    private void analyzeDeclaration(AST declaration) {
        if (declaration instanceof ClassNode) {
            analyzeClass((ClassNode) declaration);
        } else if (declaration instanceof MethodNode) {
            analyzeMethod((MethodNode) declaration);
        } else if (declaration instanceof FieldNode) {
            analyzeField((FieldNode) declaration);
        } else if (declaration instanceof ConstructorNode) {
            analyzeConstructor((ConstructorNode) declaration);
        } else if (declaration instanceof ImportNode) {
            analyzeImport((ImportNode) declaration);
        }
    }

    private void analyzeClass(ClassNode classNode) {
        enterScope();
        try {
            // Analyze fields
            for (FieldNode field : classNode.getFields()) {
                analyzeField(field);
            }

            // Analyze methods
            for (MethodNode method : classNode.getMethods()) {
                analyzeMethod(method);
            }

            // Analyze constructor
            if (classNode.getConstructor() != null) {
                analyzeConstructor(classNode.getConstructor());
            }
        } finally {
            exitScope();
        }
    }

    private void analyzeField(FieldNode field) {
        // Add to current scope
        Symbol symbol = new Symbol(field.getName(), null, false);
        defineSymbol(symbol);
    }

    private void analyzeMethod(MethodNode method) {
        enterScope();
        try {
            // TODO: Implement parameter analysis
            // Analyze body
            for (AST statement : method.getBody()) {
                analyzeStatement(statement);
            }
        } finally {
            exitScope();
        }
    }

    private void analyzeConstructor(ConstructorNode constructor) {
        enterScope();
        try {
            // TODO: Implement parameter analysis
            // Analyze body
            for (AST statement : constructor.getBody()) {
                analyzeStatement(statement);
            }
        } finally {
            exitScope();
        }
    }

    private void analyzeStatement(AST statement) {
        if (statement instanceof IfStmt) {
            analyzeIf((IfStmt) statement);
        } else if (statement instanceof WhileStmt) {
            analyzeWhile((WhileStmt) statement);
        } else if (statement instanceof ReturnStmt) {
            analyzeReturn((ReturnStmt) statement);
        } else {
            // Generic expression or statement node
            analyzeExpression(statement);
        }
    }

    private void analyzeIf(IfStmt ifNode) {
        analyzeExpression(ifNode.getCondition());
        analyzeStatement(ifNode.getThenBranch());
        if (ifNode.getElseBranch() != null) {
            analyzeStatement(ifNode.getElseBranch());
        }
    }

    private void analyzeWhile(WhileStmt whileNode) {
        analyzeExpression(whileNode.getCondition());
        analyzeStatement(whileNode.getBody());
    }

    private void analyzeReturn(ReturnStmt returnNode) {
        if (returnNode.getValue() != null) {
            analyzeExpression(returnNode.getValue());
        }
    }

    private void analyzeExpression(AST expression) {
        // TODO: Implement expression analysis
    }

    private void enterScope() {
        scopes.push(new Scope());
    }

    private void exitScope() {
        scopes.pop();
    }

    private void defineSymbol(Symbol symbol) {
        if (scopes.isEmpty()) {
            errorHandler.handleError("No active scope");
            return;
        }
        scopes.peek().define(symbol);
    }

    protected Symbol resolveSymbol(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Symbol symbol = scopes.get(i).resolve(name);
            if (symbol != null) {
                return symbol;
            }
        }
        return null;
    }
} 