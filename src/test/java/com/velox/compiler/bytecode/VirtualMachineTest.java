package com.velox.compiler.bytecode;

import com.velox.compiler.bytecode.instructions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VirtualMachineTest {
    private VirtualMachine vm;
    private List<Instruction> instructions;
    private List<Constant> constants;
    private Map<String, Integer> functionOffsets;
    private Bytecode bytecode;

    @BeforeEach
    void setUp() {
        instructions = new ArrayList<>();
        constants = new ArrayList<>();
        functionOffsets = new HashMap<>();
        bytecode = new Bytecode();
        vm = new VirtualMachine(bytecode);
    }

    @Test
    void testEmptyBytecode() {
        Bytecode bytecode = new Bytecode(instructions, constants, functionOffsets);
        vm = new VirtualMachine(bytecode);
        
        vm.execute();
        assertFalse(vm.isRunning());
        assertTrue(vm.getStack().isEmpty());
    }

    @Test
    void testLoadConstant() {
        constants.add(new Constant(42));
        instructions.add(new LoadConstantInstruction(0));
        
        Bytecode bytecode = new Bytecode(instructions, constants, functionOffsets);
        vm = new VirtualMachine(bytecode);
        
        vm.execute();
        assertEquals(42, vm.pop());
    }

    @Test
    void testArithmeticOperations() {
        // Test addition: 2 + 3
        constants.add(new Constant(2));
        constants.add(new Constant(3));
        instructions.add(new LoadConstantInstruction(0));
        instructions.add(new LoadConstantInstruction(1));
        instructions.add(new AddInstruction());
        
        Bytecode bytecode = new Bytecode(instructions, constants, functionOffsets);
        vm = new VirtualMachine(bytecode);
        
        vm.execute();
        assertEquals(5, vm.pop());
    }

    @Test
    void testLocalVariables() {
        // Test storing and loading local variables
        constants.add(new Constant(42));
        instructions.add(new LoadConstantInstruction(0));
        instructions.add(new StoreLocalInstruction(0));
        instructions.add(new LoadLocalInstruction(0));
        
        Bytecode bytecode = new Bytecode(instructions, constants, functionOffsets);
        vm = new VirtualMachine(bytecode);
        
        vm.execute();
        assertEquals(42, vm.pop());
    }

    @Test
    void testJump() {
        // Test unconditional jump
        constants.add(new Constant(1));
        constants.add(new Constant(2));
        instructions.add(new LoadConstantInstruction(0));
        instructions.add(new JumpInstruction(3));
        instructions.add(new LoadConstantInstruction(1));
        instructions.add(new LoadConstantInstruction(0));
        
        Bytecode bytecode = new Bytecode(instructions, constants, functionOffsets);
        vm = new VirtualMachine(bytecode);
        
        vm.execute();
        assertEquals(1, vm.pop());
    }

    @Test
    void testConditionalJump() {
        // Test conditional jump: if (true) jump to end
        constants.add(new Constant(true));
        instructions.add(new LoadConstantInstruction(0));
        instructions.add(new JumpIfFalseInstruction(3));
        instructions.add(new LoadConstantInstruction(0));
        
        Bytecode bytecode = new Bytecode(instructions, constants, functionOffsets);
        vm = new VirtualMachine(bytecode);
        
        vm.execute();
        assertTrue((Boolean) vm.pop());
    }

    @Test
    void testComparison() {
        // Test greater than comparison: 5 > 3
        constants.add(new Constant(5));
        constants.add(new Constant(3));
        instructions.add(new LoadConstantInstruction(0));
        instructions.add(new LoadConstantInstruction(1));
        instructions.add(new GreaterInstruction());
        
        Bytecode bytecode = new Bytecode(instructions, constants, functionOffsets);
        vm = new VirtualMachine(bytecode);
        
        vm.execute();
        assertTrue((Boolean) vm.pop());
    }

    @Test
    void testLogicalOperations() {
        // Test logical AND: true && false
        constants.add(new Constant(true));
        constants.add(new Constant(false));
        instructions.add(new LoadConstantInstruction(0));
        instructions.add(new LoadConstantInstruction(1));
        instructions.add(new AndInstruction());
        
        Bytecode bytecode = new Bytecode(instructions, constants, functionOffsets);
        vm = new VirtualMachine(bytecode);
        
        vm.execute();
        assertFalse((Boolean) vm.pop());
    }

    @Test
    void testReturn() {
        // Test return instruction
        constants.add(new Constant(42));
        instructions.add(new LoadConstantInstruction(0));
        instructions.add(new ReturnInstruction());
        
        Bytecode bytecode = new Bytecode(instructions, constants, functionOffsets);
        vm = new VirtualMachine(bytecode);
        
        vm.execute();
        assertFalse(vm.isRunning());
        assertEquals(42, vm.pop());
    }

    @Test
    void testStackOperations() {
        // Test stack operations: dup and pop
        constants.add(new Constant(42));
        instructions.add(new LoadConstantInstruction(0));
        instructions.add(new DupInstruction());
        instructions.add(new PopInstruction());
        
        Bytecode bytecode = new Bytecode(instructions, constants, functionOffsets);
        vm = new VirtualMachine(bytecode);
        
        vm.execute();
        assertEquals(42, vm.pop());
    }

    @Test
    void testSimpleFunctionCall() {
        // Add constants
        int addFunctionOffset = bytecode.getInstructionCount();
        bytecode.addFunction("add", addFunctionOffset);

        // Add function implementation
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load first argument
        bytecode.addInstruction(new LoadConstantInstruction(1)); // Load second argument
        bytecode.addInstruction(new AddInstruction());
        bytecode.addInstruction(new ReturnInstruction(true));

        // Add main program
        bytecode.addInstruction(new LoadConstantInstruction(2)); // Load 5
        bytecode.addInstruction(new LoadConstantInstruction(3)); // Load 3
        bytecode.addInstruction(new CallInstruction("add", 2));
        bytecode.addInstruction(new HaltInstruction());

        // Add constants
        bytecode.addConstant(new Constant(5, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(3, Constant.ConstantType.INTEGER));

        // Execute
        vm.execute();

        // Verify result
        assertEquals(8, vm.pop());
    }

    @Test
    void testRecursiveFunctionCall() {
        // Add constants
        int factorialOffset = bytecode.getInstructionCount();
        bytecode.addFunction("factorial", factorialOffset);

        // Add factorial function implementation
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load argument
        bytecode.addInstruction(new LoadConstantInstruction(1)); // Load 1
        bytecode.addInstruction(new CompareInstruction(CompareInstruction.Operator.EQUALS));
        bytecode.addInstruction(new JumpIfFalseInstruction(5)); // Skip to recursive case
        bytecode.addInstruction(new LoadConstantInstruction(1)); // Return 1
        bytecode.addInstruction(new ReturnInstruction(true));
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load argument
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load argument again
        bytecode.addInstruction(new LoadConstantInstruction(2)); // Load 1
        bytecode.addInstruction(new SubtractInstruction());
        bytecode.addInstruction(new CallInstruction("factorial", 1));
        bytecode.addInstruction(new MultiplyInstruction());
        bytecode.addInstruction(new ReturnInstruction(true));

        // Add main program
        bytecode.addInstruction(new LoadConstantInstruction(3)); // Load 5
        bytecode.addInstruction(new CallInstruction("factorial", 1));
        bytecode.addInstruction(new HaltInstruction());

        // Add constants
        bytecode.addConstant(new Constant(5, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(1, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(1, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(5, Constant.ConstantType.INTEGER));

        // Execute
        vm.execute();

        // Verify result
        assertEquals(120, vm.pop());
    }

    @Test
    void testFunctionCallWithMultipleArguments() {
        // Add constants
        int sumOffset = bytecode.getInstructionCount();
        bytecode.addFunction("sum", sumOffset);

        // Add sum function implementation
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load first argument
        bytecode.addInstruction(new LoadConstantInstruction(1)); // Load second argument
        bytecode.addInstruction(new AddInstruction());
        bytecode.addInstruction(new LoadConstantInstruction(2)); // Load third argument
        bytecode.addInstruction(new AddInstruction());
        bytecode.addInstruction(new ReturnInstruction(true));

        // Add main program
        bytecode.addInstruction(new LoadConstantInstruction(3)); // Load 1
        bytecode.addInstruction(new LoadConstantInstruction(4)); // Load 2
        bytecode.addInstruction(new LoadConstantInstruction(5)); // Load 3
        bytecode.addInstruction(new CallInstruction("sum", 3));
        bytecode.addInstruction(new HaltInstruction());

        // Add constants
        bytecode.addConstant(new Constant(1, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(2, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(3, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(1, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(2, Constant.ConstantType.INTEGER));
        bytecode.addConstant(new Constant(3, Constant.ConstantType.INTEGER));

        // Execute
        vm.execute();

        // Verify result
        assertEquals(6, vm.pop());
    }

    @Test
    void testVoidFunction() {
        // Add constants
        int printOffset = bytecode.getInstructionCount();
        bytecode.addFunction("print", printOffset);

        // Add print function implementation
        bytecode.addInstruction(new LoadConstantInstruction(0)); // Load argument
        bytecode.addInstruction(new PrintInstruction());
        bytecode.addInstruction(new ReturnInstruction(false));

        // Add main program
        bytecode.addInstruction(new LoadConstantInstruction(1)); // Load "Hello, World!"
        bytecode.addInstruction(new CallInstruction("print", 1));
        bytecode.addInstruction(new HaltInstruction());

        // Add constants
        bytecode.addConstant(new Constant("Hello, World!", Constant.ConstantType.STRING));
        bytecode.addConstant(new Constant("Hello, World!", Constant.ConstantType.STRING));

        // Execute
        vm.execute();

        // Verify stack is empty
        assertTrue(vm.getStack().isEmpty());
    }
} 