package com.velox.compiler.bytecode;

import com.velox.compiler.bytecode.instructions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicFunctionalityTest {
    private Bytecode bytecode;
    private VirtualMachine vm;

    @BeforeEach
    void setUp() {
        bytecode = new Bytecode();
        vm = new VirtualMachine(bytecode);
    }

    @Test
    void testVariablesAndAssignment() {
        // Test local variables
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load 42
        bytecode.addInstruction(new StoreLocalInstruction(0));   // Store in local 0
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load from local 0
        bytecode.addInstruction(new HaltInstruction());

        bytecode.addConstant(new Constant(42, Constant.ConstantType.INTEGER));

        vm.execute();
        assertEquals(42, vm.pop());
    }

    @Test
    void testGlobalVariables() {
        // Test global variables
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load "global"
        bytecode.addInstruction(new StoreGlobalInstruction("message"));
        bytecode.addInstruction(new LoadGlobalInstruction("message"));
        bytecode.addInstruction(new HaltInstruction());

        bytecode.addConstant(new Constant("global", Constant.ConstantType.STRING));

        vm.execute();
        assertEquals("global", vm.pop());
    }

    @Test
    void testWhileLoop() {
        // Test while loop: while (i < 5) { i = i + 1 }
        int loopStart = bytecode.getInstructionCount();
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load i
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load 5
        bytecode.addInstruction(new CompareInstruction(CompareInstruction.Operator.LESS_THAN));
        bytecode.addInstruction(new JumpIfFalseInstruction(8));  // Exit loop if false
        
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load i
        bytecode.addInstruction(new LoadConstantInstruction(1)); // Load 1
        bytecode.addInstruction(new AddInstruction());           // i + 1
        bytecode.addInstruction(new StoreLocalInstruction(0));   // Store back to i
        bytecode.addInstruction(new JumpInstruction(loopStart)); // Jump back to start
        
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load final i
        bytecode.addInstruction(new HaltInstruction());

        bytecode.addConstant(new Constant(5, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(1, Constant.ConstantType.INTEGER));

        // Initialize i to 0
        vm.setLocal(0, 0);
        vm.execute();
        assertEquals(5, vm.pop());
    }

    @Test
    void testForLoop() {
        // Test for loop: for (i = 0; i < 5; i = i + 1) { sum = sum + i }
        // Initialize i and sum
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load 0
        bytecode.addInstruction(new StoreLocalInstruction(0));   // i = 0
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load 0
        bytecode.addInstruction(new StoreLocalInstruction(1));   // sum = 0

        int loopStart = bytecode.getInstructionCount();
        // Check condition
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load i
        bytecode.addInstruction(new LoadConstantInstruction(1)); // Load 5
        bytecode.addInstruction(new CompareInstruction(CompareInstruction.Operator.LESS_THAN));
        bytecode.addInstruction(new JumpIfFalseInstruction(12)); // Exit loop if false

        // Loop body
        bytecode.addInstruction(new LoadLocalInstruction(1));    // Load sum
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load i
        bytecode.addInstruction(new AddInstruction());           // sum + i
        bytecode.addInstruction(new StoreLocalInstruction(1));   // Store sum

        // Increment
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load i
        bytecode.addInstruction(new LoadConstantInstruction(2)); // Load 1
        bytecode.addInstruction(new AddInstruction());           // i + 1
        bytecode.addInstruction(new StoreLocalInstruction(0));   // Store i
        bytecode.addInstruction(new JumpInstruction(loopStart)); // Jump back to start

        bytecode.addInstruction(new LoadLocalInstruction(1));    // Load final sum
        bytecode.addInstruction(new HaltInstruction());

        bytecode.addConstant(new Constant(0, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(5, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(1, Constant.ConstantType.INTEGER));

        vm.execute();
        assertEquals(10, vm.pop()); // 0 + 1 + 2 + 3 + 4 = 10
    }

    @Test
    void testErrorHandling() {
        // Test error handling with try-catch
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load "error"
        bytecode.addInstruction(new ThrowInstruction());         // Throw error
        bytecode.addInstruction(new LoadConstantInstruction(1)); // This should be skipped
        bytecode.addInstruction(new HaltInstruction());

        bytecode.addConstant(new Constant("error", Constant.ConstantType.STRING));
        bytecode.addConstant(new Constant("skipped", Constant.ConstantType.STRING));

        vm.setDebugMode(true);
        vm.execute();
        
        assertFalse(vm.getErrors().isEmpty());
        assertEquals("error", vm.getErrors().get(0).getMessage());
    }

    @Test
    void testPrintStatements() {
        // Test print statements
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load "Hello"
        bytecode.addInstruction(new PrintInstruction());
        bytecode.addInstruction(new LoadConstantInstruction(1)); // Load 42
        bytecode.addInstruction(new PrintInstruction());
        bytecode.addInstruction(new HaltInstruction());

        bytecode.addConstant(new Constant("Hello", Constant.ConstantType.STRING));
        bytecode.addConstant(new Constant(42, Constant.ConstantType.INTEGER));

        vm.execute();
        assertTrue(vm.getStack().isEmpty());
    }

    @Test
    void testUserDefinedFunction() {
        // Define a function that calculates square
        int squareOffset = bytecode.getInstructionCount();
        bytecode.addFunction("square", squareOffset);

        // Function implementation
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load argument
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load argument again
        bytecode.addInstruction(new MultiplyInstruction());      // Multiply
        bytecode.addInstruction(new ReturnInstruction(true));    // Return result

        // Main program
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load 5
        bytecode.addInstruction(new CallInstruction("square", 1));
        bytecode.addInstruction(new HaltInstruction());

        bytecode.addConstant(new Constant(5, Constant.ConstantType.INTEGER));

        vm.execute();
        assertEquals(25, vm.pop());
    }

    @Test
    void testNestedFunctionCalls() {
        // Define a function that adds two numbers
        int addOffset = bytecode.getInstructionCount();
        bytecode.addFunction("add", addOffset);
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load first arg
        bytecode.addInstruction(new LoadLocalInstruction(1));    // Load second arg
        bytecode.addInstruction(new AddInstruction());           // Add them
        bytecode.addInstruction(new ReturnInstruction(true));    // Return result

        // Define a function that doubles a number
        int doubleOffset = bytecode.getInstructionCount();
        bytecode.addFunction("double", doubleOffset);
        bytecode.addInstruction(new LoadLocalInstruction(0));    // Load argument
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load 2
        bytecode.addInstruction(new MultiplyInstruction());      // Multiply by 2
        bytecode.addInstruction(new ReturnInstruction(true));    // Return result

        // Main program: double(add(3, 4))
        bytecode.addInstruction(new LoadConstantInstruction(1)); // Load 3
        bytecode.addInstruction(new LoadConstantInstruction(2)); // Load 4
        bytecode.addInstruction(new CallInstruction("add", 2));  // Call add(3, 4)
        bytecode.addInstruction(new CallInstruction("double", 1)); // Call double(result)
        bytecode.addInstruction(new HaltInstruction());

        bytecode.addConstant(new Constant(2, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(3, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(4, Constant.ConstantType.INTEGER));

        vm.execute();
        assertEquals(14, vm.pop()); // double(add(3, 4)) = double(7) = 14
    }
} 