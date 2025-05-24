# Velox Compiler Documentation

## Overview

The Velox compiler is responsible for transforming Velox source code into executable bytecode. It consists of several key components:

1. Lexer
2. Parser
3. Semantic Analyzer
4. Code Generator
5. Optimizer

## Lexer

The lexer tokenizes source code into a stream of tokens.

### Token Types

```velox
enum TokenType {
    // Module and Import Keywords
    MODULE, IMPORT, EXPORT,
    
    // Class and Object Keywords
    CLASS, INTERFACE, EXTENDS, IMPLEMENTS,
    SUPER, THIS, NEW, STATIC,
    
    // Access Modifiers
    PUBLIC, PRIVATE, PROTECTED, INTERNAL,
    
    // Functions
    FUN, RETURN, VOID,
    
    // Control Flow
    IF, ELSE, WHILE, FOR, DO,
    BREAK, CONTINUE, SWITCH, CASE, DEFAULT,
    
    // Error Handling
    TRY, CATCH, FINALLY, THROW,
    
    // Variable Declaration
    VAR, LET, CONST,
    
    // Type System
    TYPE, INTERFACE, ENUM, STRUCT,
    STRING, NUMBER, BOOLEAN, CHAR,
    ARRAY, MAP, SET, TUPLE,
    
    // Logical Operators
    AND, OR, NOT,
    
    // Built-in Functions
    PRINT, READ, WRITE,
    
    // Literals
    IDENTIFIER, NUMBER, TRUE, FALSE,
    
    // Operators
    PLUS, MINUS, STAR, SLASH,
    EQUAL, EQUAL_EQUAL, BANG, BANG_EQUAL,
    GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,
    
    // Punctuation
    LEFT_PAREN, RIGHT_PAREN,
    LEFT_BRACE, RIGHT_BRACE,
    LEFT_BRACKET, RIGHT_BRACKET,
    SEMICOLON, COMMA, DOT, COLON,
    
    // Special
    EOF
}
```

### Example Usage

```velox
// Create a lexer
let lexer = new Lexer(sourceCode);

// Scan tokens
let tokens = lexer.scanTokens();

// Process tokens
for (let token in tokens) {
    println("Token: " + token.type + " - " + token.lexeme);
}
```

## Parser

The parser converts tokens into an Abstract Syntax Tree (AST).

### AST Node Types

```velox
interface AST {
    function accept(visitor: ASTVisitor): any;
}

class ModuleNode implements AST {
    private imports: ImportNode[];
    private declarations: AST[];
    
    function accept(visitor: ASTVisitor): any {
        return visitor.visitModule(this);
    }
}

class ClassNode implements AST {
    private name: string;
    private superclass: string;
    private interfaces: string[];
    private fields: FieldNode[];
    private methods: MethodNode[];
    private constructor: ConstructorNode;
    
    function accept(visitor: ASTVisitor): any {
        return visitor.visitClass(this);
    }
}

class FunctionNode implements AST {
    private name: string;
    private parameters: ParameterNode[];
    private returnType: TypeNode;
    private body: BlockNode;
    
    function accept(visitor: ASTVisitor): any {
        return visitor.visitFunction(this);
    }
}

class MethodNode implements AST {
    private name: string;
    private parameters: ParameterNode[];
    private returnType: TypeNode;
    private body: BlockNode;
    private isStatic: boolean;
    
    function accept(visitor: ASTVisitor): any {
        return visitor.visitMethod(this);
    }
}

class FieldNode implements AST {
    private name: string;
    private type: TypeNode;
    private initializer: AST;
    private isStatic: boolean;
    
    function accept(visitor: ASTVisitor): any {
        return visitor.visitField(this);
    }
}

class ConstructorNode implements AST {
    private parameters: ParameterNode[];
    private body: BlockNode;
    
    function accept(visitor: ASTVisitor): any {
        return visitor.visitConstructor(this);
    }
}
```

### Example Usage

```velox
// Create a parser
let parser = new Parser(tokens);

// Parse the program
let ast = parser.parse();

// Visit the AST
let visitor = new AstVisitor();
ast.accept(visitor);
```

## Semantic Analyzer

The semantic analyzer performs type checking and other semantic validations.

### Type System

```velox
interface Type {
    function isAssignableTo(other: Type): bool;
    function toString(): string;
}

class PrimitiveType implements Type {
    private name: string;
    
    function isAssignableTo(other: Type): bool {
        return this == other;
    }
}

class FunctionType implements Type {
    private parameterTypes: Type[];
    private returnType: Type;
    
    function isAssignableTo(other: Type): bool {
        if (!(other instanceof FunctionType)) {
            return false;
        }
        // Check parameter and return type compatibility
    }
}
```

### Example Usage

```velox
// Create a semantic analyzer
let analyzer = new SemanticAnalyzer();

// Analyze the AST
let typeInfo = analyzer.analyze(ast);

// Check for errors
if (analyzer.hasErrors()) {
    println("Semantic errors found:");
    for (let error in analyzer.getErrors()) {
        println("- " + error);
    }
}
```

## Code Generator

The code generator transforms the AST into bytecode.

### Bytecode Instructions

```velox
enum OpCode {
    // Stack operations
    PUSH, POP, DUP, SWAP,
    
    // Arithmetic
    ADD, SUB, MUL, DIV, MOD,
    
    // Comparison
    EQ, NE, GT, LT, GE, LE,
    
    // Control flow
    JUMP, JUMP_IF_FALSE, CALL, RETURN,
    
    // Variable operations
    LOAD, STORE, GET_PROPERTY, SET_PROPERTY
}
```

### Function Calls

The CALL instruction handles function invocation in the bytecode:

```velox
class CallInstruction implements Instruction {
    private argumentCount: int;

    function execute(vm: VirtualMachine): void {
        // Pop function name from stack
        let function = vm.pop();
        
        // Pop arguments from stack
        let args = new Array[argumentCount];
        for (let i = argumentCount - 1; i >= 0; i--) {
            args[i] = vm.pop();
        }
        
        // Call the function
        if (function instanceof String) {
            vm.callFunction(function, args);
        } else {
            throw new RuntimeException("Invalid function object");
        }
    }
}
```

### Example Usage

```velox
// Create a code generator
let generator = new CodeGenerator();

// Generate bytecode
let bytecode = generator.generate(ast);

// Write bytecode to file
let writer = new BytecodeWriter("output.vbc");
writer.write(bytecode);
```

## Optimizer

The optimizer improves the generated bytecode.

### Optimization Passes

```velox
interface OptimizationPass {
    function optimize(bytecode: Bytecode): Bytecode;
}

class ConstantFolding implements OptimizationPass {
    function optimize(bytecode: Bytecode): Bytecode {
        // Fold constant expressions
    }
}

class DeadCodeElimination implements OptimizationPass {
    function optimize(bytecode: Bytecode): Bytecode {
        // Remove unreachable code
    }
}
```

### Example Usage

```velox
// Create an optimizer
let optimizer = new Optimizer();

// Add optimization passes
optimizer.addPass(new ConstantFolding());
optimizer.addPass(new DeadCodeElimination());

// Optimize bytecode
let optimizedBytecode = optimizer.optimize(bytecode);
```

## Compiler Pipeline

### Basic Usage

```velox
// Create a compiler
let compiler = new Compiler();

// Compile source code
let result = compiler.compile(sourceCode);

// Check for errors
if (result.hasErrors()) {
    println("Compilation errors:");
    for (let error in result.getErrors()) {
        println("- " + error);
    }
} else {
    // Get the compiled bytecode
    let bytecode = result.getBytecode();
    
    // Save to file
    bytecode.save("output.vbc");
}
```

### Advanced Usage

```velox
// Create a compiler with custom options
let options = new CompilerOptions()
    .setOptimizationLevel(OptimizationLevel.HIGH)
    .setTargetPlatform(Platform.NATIVE)
    .enableDebugInfo(true);

let compiler = new Compiler(options);

// Add custom optimization passes
compiler.addOptimizationPass(new CustomOptimizationPass());

// Compile with progress tracking
compiler.compileWithProgress(sourceCode, (progress) => {
    println("Compilation progress: " + progress + "%");
});
```

## Error Handling

### Compiler Errors

```velox
class CompilerError extends Error {
    private severity: ErrorSeverity;
    private location: SourceLocation;
    
    constructor(message: string, severity: ErrorSeverity, location: SourceLocation) {
        super(message);
        this.severity = severity;
        this.location = location;
    }
}

// Example error handling
try {
    let result = compiler.compile(sourceCode);
} catch (error) {
    if (error instanceof CompilerError) {
        println("Error at " + error.location + ": " + error.message);
        println("Severity: " + error.severity);
    }
}
```

## Debugging

### Debug Information

```velox
// Enable debug information
let options = new CompilerOptions()
    .enableDebugInfo(true)
    .setDebugLevel(DebugLevel.VERBOSE);

// Get debug information
let debugInfo = result.getDebugInfo();
println("Source maps: " + debugInfo.getSourceMaps());
println("Variable tables: " + debugInfo.getVariableTables());
```

### Compiler Diagnostics

```velox
// Get compiler diagnostics
let diagnostics = compiler.getDiagnostics();

// Print warnings and errors
for (let diagnostic in diagnostics) {
    println(diagnostic.severity + ": " + diagnostic.message);
    println("Location: " + diagnostic.location);
}
``` 