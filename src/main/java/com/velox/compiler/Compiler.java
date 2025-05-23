package com.velox.compiler;

import com.velox.compiler.lexer.Lexer;
import com.velox.compiler.parser.Parser;
import com.velox.compiler.semantic.SemanticAnalyzer;
import com.velox.compiler.codegen.CodeGenerator;
import com.velox.compiler.optimizer.Optimizer;
import com.velox.compiler.ast.AST;
import com.velox.compiler.bytecode.Bytecode;
import com.velox.compiler.error.CompilationError;
import com.velox.compiler.error.ErrorHandler;

public class Compiler {
    private final Lexer lexer;
    private final Parser parser;
    private final SemanticAnalyzer semanticAnalyzer;
    private final CodeGenerator codeGenerator;
    private final Optimizer optimizer;
    private final ErrorHandler errorHandler;
    private final PerformanceMonitor performanceMonitor;

    public Compiler() {
        this.lexer = new Lexer();
        this.parser = new Parser();
        this.semanticAnalyzer = new SemanticAnalyzer();
        this.codeGenerator = new CodeGenerator();
        this.optimizer = new Optimizer();
        this.errorHandler = new ErrorHandler();
        this.performanceMonitor = new PerformanceMonitor();
    }

    public Bytecode compile(String source) throws CompilationError {
        try {
            // Start performance monitoring
            performanceMonitor.startPhase("lexical_analysis");
            
            // Lexical Analysis
            var tokens = lexer.tokenize(source);
            performanceMonitor.endPhase("lexical_analysis");

            // Syntax Analysis
            performanceMonitor.startPhase("syntax_analysis");
            var ast = parser.parse(tokens);
            performanceMonitor.endPhase("syntax_analysis");

            // Semantic Analysis
            performanceMonitor.startPhase("semantic_analysis");
            var typeInfo = semanticAnalyzer.analyze(ast);
            performanceMonitor.endPhase("semantic_analysis");

            // Code Generation
            performanceMonitor.startPhase("code_generation");
            var bytecode = codeGenerator.generate(ast, typeInfo);
            performanceMonitor.endPhase("code_generation");

            // Optimization
            performanceMonitor.startPhase("optimization");
            var optimizedBytecode = optimizer.optimize(bytecode);
            performanceMonitor.endPhase("optimization");

            return optimizedBytecode;
        } catch (Exception e) {
            errorHandler.handleError(e);
            throw new CompilationError("Compilation failed", e);
        }
    }

    public void setOptimizationLevel(OptimizationLevel level) {
        optimizer.setLevel(level);
    }

    public void enableDebugInfo(boolean enable) {
        codeGenerator.setDebugInfoEnabled(enable);
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMonitor.getMetrics();
    }
} 