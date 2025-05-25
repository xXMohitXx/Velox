package com.velox.compiler;

import com.velox.compiler.lexer.Lexer;
import com.velox.compiler.parser.Parser;
import com.velox.compiler.semantic.SemanticAnalyzer;
import com.velox.compiler.codegen.CodeGenerator;
import com.velox.compiler.optimizer.Optimizer;
import com.velox.compiler.ast.ModuleNode;
import com.velox.compiler.bytecode.Bytecode;
import com.velox.compiler.error.CompilationError;
import com.velox.compiler.error.ErrorHandler;
import com.velox.compiler.token.Token;
import com.velox.compiler.util.PerformanceMetrics;
import java.util.List;

public class Compiler {
    private final Lexer lexer;
    private final SemanticAnalyzer semanticAnalyzer;
    private final CodeGenerator codeGenerator;
    private final Optimizer optimizer;
    private final ErrorHandler errorHandler;
    private final PerformanceMonitor performanceMonitor;

    public Compiler() {
        this.errorHandler = new ErrorHandler();
        this.lexer = new Lexer();
        this.semanticAnalyzer = new SemanticAnalyzer(errorHandler);
        this.codeGenerator = new CodeGenerator();
        this.optimizer = new Optimizer();
        this.performanceMonitor = new PerformanceMonitor();
    }

    public Bytecode compile(String source) throws CompilationError {
        try {
            // Start performance monitoring
            performanceMonitor.startPhase("lexical_analysis");
            
            // Lexical Analysis
            List<Token> tokens = lexer.tokenize(source);
            performanceMonitor.endPhase("lexical_analysis");

            // Syntax Analysis
            performanceMonitor.startPhase("syntax_analysis");
            Parser parser = new Parser(tokens);
            ModuleNode ast = (ModuleNode) parser.parse();
            performanceMonitor.endPhase("syntax_analysis");

            // Semantic Analysis
            performanceMonitor.startPhase("semantic_analysis");
            semanticAnalyzer.analyze(ast);
            performanceMonitor.endPhase("semantic_analysis");

            // Code Generation
            performanceMonitor.startPhase("code_generation");
            Bytecode bytecode = codeGenerator.generate(ast);
            performanceMonitor.endPhase("code_generation");

            // Optimization
            performanceMonitor.startPhase("optimization");
            Bytecode optimizedBytecode = optimizer.optimize(bytecode);
            performanceMonitor.endPhase("optimization");

            return optimizedBytecode;
        } catch (Exception e) {
            errorHandler.handleError(new CompilationError("Compilation failed", e));
            throw new CompilationError("Compilation failed", e);
        }
    }

    public void setOptimizationLevel(int level) {
        optimizer.setLevel(level);
    }

    public void enableDebugInfo(boolean enable) {
        codeGenerator.setDebugInfoEnabled(enable);
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMonitor.getMetrics();
    }
} 