package com.velox.compiler.semantic;

import com.velox.compiler.ast.*;
import com.velox.compiler.error.SemanticError;
import com.velox.compiler.error.ErrorHandler;
import java.util.*;

public class SemanticAnalyzer {
    private final ErrorHandler errorHandler;
    private final Map<String, Symbol> symbols;
    private final Stack<Scope> scopes;
    private final TypeChecker typeChecker;

    public SemanticAnalyzer(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.symbols = new HashMap<>();
        this.scopes = new Stack<>();
        this.typeChecker = new TypeChecker(errorHandler);
    }

    public void analyze(ModuleNode module) {
        enterScope();
        try {
            // Analyze imports
            for (ImportNode importNode : module.getImports()) {
                analyzeImport(importNode);
            }

            // Analyze declarations
            for (ASTNode declaration : module.getDeclarations()) {
                analyzeDeclaration(declaration);
            }
        } finally {
            exitScope();
        }
    }

    private void analyzeImport(ImportNode importNode) {
        // TODO: Implement import analysis
    }

    private void analyzeDeclaration(ASTNode declaration) {
        if (declaration instanceof FunctionNode) {
            analyzeFunction((FunctionNode) declaration);
        } else if (declaration instanceof ClassNode) {
            analyzeClass((ClassNode) declaration);
        } else if (declaration instanceof VariableDeclarationNode) {
            analyzeVariableDeclaration((VariableDeclarationNode) declaration);
        }
    }

    private void analyzeFunction(FunctionNode function) {
        enterScope();
        try {
            // Analyze parameters
            for (ParameterNode parameter : function.getParameters()) {
                analyzeParameter(parameter);
            }

            // Analyze return type
            if (function.getReturnType() != null) {
                analyzeTypeAnnotation(function.getReturnType());
            }

            // Analyze body
            analyzeBlock(function.getBody());
        } finally {
            exitScope();
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

    private void analyzeVariableDeclaration(VariableDeclarationNode declaration) {
        // Analyze type
        if (declaration.getType() != null) {
            analyzeTypeAnnotation(declaration.getType());
        }

        // Analyze initializer
        if (declaration.getInitializer() != null) {
            analyzeExpression(declaration.getInitializer());
        }

        // Add to current scope
        Symbol symbol = new Symbol(declaration.getName(), declaration.getType(), declaration.isConst());
        defineSymbol(symbol);
    }

    private void analyzeParameter(ParameterNode parameter) {
        // Analyze type
        if (parameter.getType() != null) {
            analyzeTypeAnnotation(parameter.getType());
        }

        // Add to current scope
        Symbol symbol = new Symbol(parameter.getName(), parameter.getType(), false);
        defineSymbol(symbol);
    }

    private void analyzeField(FieldNode field) {
        // Analyze type
        if (field.getType() != null) {
            analyzeTypeAnnotation(field.getType());
        }

        // Add to current scope
        Symbol symbol = new Symbol(field.getName(), field.getType(), field.isConst());
        defineSymbol(symbol);
    }

    private void analyzeMethod(MethodNode method) {
        enterScope();
        try {
            // Analyze parameters
            for (ParameterNode parameter : method.getParameters()) {
                analyzeParameter(parameter);
            }

            // Analyze return type
            if (method.getReturnType() != null) {
                analyzeTypeAnnotation(method.getReturnType());
            }

            // Analyze body
            analyzeBlock(method.getBody());
        } finally {
            exitScope();
        }
    }

    private void analyzeConstructor(ConstructorNode constructor) {
        enterScope();
        try {
            // Analyze parameters
            for (ParameterNode parameter : constructor.getParameters()) {
                analyzeParameter(parameter);
            }

            // Analyze body
            analyzeBlock(constructor.getBody());
        } finally {
            exitScope();
        }
    }

    private void analyzeBlock(BlockNode block) {
        enterScope();
        try {
            for (ASTNode statement : block.getStatements()) {
                analyzeStatement(statement);
            }
        } finally {
            exitScope();
        }
    }

    private void analyzeStatement(ASTNode statement) {
        if (statement instanceof VariableDeclarationNode) {
            analyzeVariableDeclaration((VariableDeclarationNode) statement);
        } else if (statement instanceof IfNode) {
            analyzeIf((IfNode) statement);
        } else if (statement instanceof WhileNode) {
            analyzeWhile((WhileNode) statement);
        } else if (statement instanceof ReturnNode) {
            analyzeReturn((ReturnNode) statement);
        } else if (statement instanceof ExpressionNode) {
            analyzeExpression((ExpressionNode) statement);
        }
    }

    private void analyzeIf(IfNode ifNode) {
        analyzeExpression(ifNode.getCondition());
        analyzeStatement(ifNode.getThenBranch());
        if (ifNode.getElseBranch() != null) {
            analyzeStatement(ifNode.getElseBranch());
        }
    }

    private void analyzeWhile(WhileNode whileNode) {
        analyzeExpression(whileNode.getCondition());
        analyzeStatement(whileNode.getBody());
    }

    private void analyzeReturn(ReturnNode returnNode) {
        if (returnNode.getValue() != null) {
            analyzeExpression(returnNode.getValue());
        }
    }

    private void analyzeExpression(ExpressionNode expression) {
        typeChecker.checkExpression(expression);
    }

    private void analyzeTypeAnnotation(TypeAnnotationNode typeAnnotation) {
        typeChecker.checkType(typeAnnotation);
    }

    private void enterScope() {
        scopes.push(new Scope());
    }

    private void exitScope() {
        scopes.pop();
    }

    private void defineSymbol(Symbol symbol) {
        if (scopes.isEmpty()) {
            throw new SemanticError("No active scope");
        }
        scopes.peek().define(symbol);
    }

    private Symbol resolveSymbol(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Symbol symbol = scopes.get(i).resolve(name);
            if (symbol != null) {
                return symbol;
            }
        }
        return null;
    }
} 