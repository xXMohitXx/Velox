# Velox Compiler Documentation

## Overview

The Velox compiler is responsible for transforming Velox source code into executable bytecode. It consists of several key components:

1. Lexer
2. Parser
3. Semantic Analyzer
4. Code Generator
5. Optimizer

## Components

### Lexer

The lexer performs lexical analysis on the source code, breaking it down into tokens. It supports:

- Single-character tokens (e.g., `(`, `)`, `{`, `}`, `[`, `]`, `,`, `.`, `-`, `+`, `;`, `/`, `*`, `:`, `?`, `->`)
- One or two character tokens (e.g., `!`, `!=`, `=`, `==`, `>`, `>=`, `<`, `<=`, `--`, `++`)
- Literals (identifiers, strings, numbers)
- Keywords (e.g., `and`, `class`, `else`, `false`, `for`, `fun`, `if`, `nil`, `or`, `print`, `return`, `super`, `this`, `true`, `var`, `while`)
- Access modifiers (e.g., `public`, `private`, `protected`, `static`, `final`, `abstract`)
- Built-in functions (e.g., `println`, `read`, `readln`, `length`, `substring`, `concat`, `parseInt`, `parseFloat`, `toString`, `toInt`, `toFloat`)

### Parser

The parser performs syntactic analysis on the tokens, building an Abstract Syntax Tree (AST). The AST nodes include:

- Module nodes (imports and declarations)
- Class nodes (fields, methods, constructors)
- Function nodes (parameters and body)
- Method nodes (parameters, return type, body, access modifier)
- Field nodes (type, initializer, access modifier)
- Constructor nodes (parameters, body, access modifier)
- Import nodes (module name and alias)
- Parameter nodes (name and type)
- Expression nodes:
  - Binary expressions (left, operator, right)
  - Unary expressions (operator, right)
  - Literal expressions (value)
  - Variable expressions (name)
  - Assignment expressions (name, value)
  - Call expressions (callee, arguments)
  - Get expressions (object, name)
  - Set expressions (object, name, value)
  - Grouping expressions (expression)
  - This expressions (keyword)
  - Super expressions (keyword, method)
- Statement nodes:
  - If statements (condition, then branch, else branch)
  - While statements (condition, body)
  - Return statements (keyword, value)
  - Block statements (statements)

### Code Generator

The code generator transforms the AST into bytecode. It supports:

- Stack operations (PUSH, POP, DUP, SWAP)
- Arithmetic operations (ADD, SUB, MUL, DIV, MOD, NEG)
- Comparison operations (EQ, NE, LT, LE, GT, GE)
- Control flow operations (JUMP, JUMP_IF_FALSE, JUMP_IF_TRUE, CALL, RETURN)
- Variable operations (GET_LOCAL, SET_LOCAL, GET_GLOBAL, SET_GLOBAL)
- Object operations (NEW, GET_PROPERTY, SET_PROPERTY, THIS, SUPER)
- Module operations (IMPORT, EXPORT)
- Function operations (PARAM, CLOSURE)
- Special operations (HALT)

### Optimizer

The optimizer performs various optimizations on the generated bytecode:

- Constant folding
- Dead code elimination
- Common subexpression elimination
- Loop optimization
- Inlining
- Tail call optimization

## Example Usage

```java
// Create a lexer
Lexer lexer = new Lexer(source);

// Tokenize the source code
List<Token> tokens = lexer.scanTokens();

// Create a parser
Parser parser = new Parser(tokens);

// Parse the tokens into an AST
AST ast = parser.parse();

// Create a code generator
CodeGenerator codeGenerator = new CodeGenerator();

// Generate bytecode from the AST
Bytecode bytecode = codeGenerator.generate(ast);

// Create an optimizer
Optimizer optimizer = new Optimizer();

// Optimize the bytecode
Bytecode optimizedBytecode = optimizer.optimize(bytecode);
```

## Error Handling

The compiler provides detailed error messages for:

- Lexical errors (e.g., unexpected characters, unterminated strings)
- Syntax errors (e.g., missing semicolons, unmatched brackets)
- Semantic errors (e.g., undefined variables, type mismatches)
- Code generation errors (e.g., invalid bytecode, stack overflow)

## Performance

The compiler is designed for performance:

- Efficient tokenization using a state machine
- Fast parsing using recursive descent
- Optimized code generation with minimal overhead
- Aggressive bytecode optimization
- Minimal memory usage
- Parallel processing where possible

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

## Debugging

### Debug Information

```