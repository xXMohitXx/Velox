package com.velox.compiler.codegen;

import com.velox.compiler.ast.*;
import com.velox.compiler.bytecode.*;
import com.velox.compiler.error.ErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CodeGeneratorTest {
    private CodeGenerator codeGenerator;
    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
        codeGenerator = new CodeGenerator(errorHandler);
    }

    @Test
    void testEmptyModule() {
        ModuleNode module = new ModuleNode();
        Bytecode bytecode = codeGenerator.generate(module);
        
        assertNotNull(bytecode);
        assertEquals(0, bytecode.getInstructionCount());
        assertEquals(0, bytecode.getConstantCount());
        assertEquals(0, bytecode.getFunctionOffsets().size());
    }

    @Test
    void testSimpleFunction() {
        // Create a simple function: function add(a, b) { return a + b; }
        ModuleNode module = new ModuleNode();
        
        // Create parameters
        List<ParameterNode> parameters = new ArrayList<>();
        parameters.add(new ParameterNode("a"));
        parameters.add(new ParameterNode("b"));
        
        // Create function body
        BlockNode body = new BlockNode();
        body.addStatement(new ReturnNode(
            new BinaryExpressionNode(
                new IdentifierNode("a"),
                new IdentifierNode("b"),
                BinaryOperator.PLUS
            )
        ));
        
        // Create function
        FunctionNode function = new FunctionNode("add", parameters, body);
        module.addDeclaration(function);
        
        // Generate bytecode
        Bytecode bytecode = codeGenerator.generate(module);
        
        assertNotNull(bytecode);
        assertTrue(bytecode.getInstructionCount() > 0);
        assertTrue(bytecode.getConstantCount() > 0);
        assertTrue(bytecode.getFunctionOffsets().containsKey("add"));
    }

    @Test
    void testIfStatement() {
        // Create a simple if statement: if (x > 0) { return 1; } else { return 0; }
        ModuleNode module = new ModuleNode();
        
        // Create function body
        BlockNode body = new BlockNode();
        body.addStatement(new IfNode(
            new BinaryExpressionNode(
                new IdentifierNode("x"),
                new LiteralNode(0),
                BinaryOperator.GREATER
            ),
            new BlockNode(new ReturnNode(new LiteralNode(1))),
            new BlockNode(new ReturnNode(new LiteralNode(0)))
        ));
        
        // Create function
        FunctionNode function = new FunctionNode("test", new ArrayList<>(), body);
        module.addDeclaration(function);
        
        // Generate bytecode
        Bytecode bytecode = codeGenerator.generate(module);
        
        assertNotNull(bytecode);
        assertTrue(bytecode.getInstructionCount() > 0);
        assertTrue(bytecode.getConstantCount() > 0);
        assertTrue(bytecode.getFunctionOffsets().containsKey("test"));
    }

    @Test
    void testWhileLoop() {
        // Create a simple while loop: while (x > 0) { x = x - 1; }
        ModuleNode module = new ModuleNode();
        
        // Create function body
        BlockNode body = new BlockNode();
        body.addStatement(new WhileNode(
            new BinaryExpressionNode(
                new IdentifierNode("x"),
                new LiteralNode(0),
                BinaryOperator.GREATER
            ),
            new BlockNode(new AssignmentNode(
                "x",
                new BinaryExpressionNode(
                    new IdentifierNode("x"),
                    new LiteralNode(1),
                    BinaryOperator.MINUS
                )
            ))
        ));
        
        // Create function
        FunctionNode function = new FunctionNode("test", new ArrayList<>(), body);
        module.addDeclaration(function);
        
        // Generate bytecode
        Bytecode bytecode = codeGenerator.generate(module);
        
        assertNotNull(bytecode);
        assertTrue(bytecode.getInstructionCount() > 0);
        assertTrue(bytecode.getConstantCount() > 0);
        assertTrue(bytecode.getFunctionOffsets().containsKey("test"));
    }

    @Test
    void testVariableDeclaration() {
        // Create a simple variable declaration: let x = 42;
        ModuleNode module = new ModuleNode();
        
        // Create function body
        BlockNode body = new BlockNode();
        body.addStatement(new VariableDeclarationNode(
            "x",
            new LiteralNode(42)
        ));
        
        // Create function
        FunctionNode function = new FunctionNode("test", new ArrayList<>(), body);
        module.addDeclaration(function);
        
        // Generate bytecode
        Bytecode bytecode = codeGenerator.generate(module);
        
        assertNotNull(bytecode);
        assertTrue(bytecode.getInstructionCount() > 0);
        assertTrue(bytecode.getConstantCount() > 0);
        assertTrue(bytecode.getFunctionOffsets().containsKey("test"));
    }
} 