module com.velox.compiler {
    requires org.python.core;
    requires org.python.util;
    requires jakarta.json;
    
    exports com.velox.compiler.ast;
    exports com.velox.compiler.ast.expressions;
    exports com.velox.compiler.ast.statements;
    exports com.velox.compiler.bytecode;
    exports com.velox.compiler.bytecode.instructions;
    exports com.velox.compiler.codegen;
    exports com.velox.compiler.error;
    exports com.velox.compiler.lexer;
    exports com.velox.compiler.optimizer;
    exports com.velox.compiler.parser;
    exports com.velox.compiler.python;
    exports com.velox.compiler.semantic;
    exports com.velox.compiler.token;
    exports com.velox.compiler.util;
    exports com.velox.std;
} 