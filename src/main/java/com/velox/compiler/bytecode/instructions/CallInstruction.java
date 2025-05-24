package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class CallInstruction implements Instruction {
    private final int argumentCount;

    public CallInstruction(int argumentCount) {
        this.argumentCount = argumentCount;
    }

    @Override
    public void execute(VirtualMachine vm) {
        // Get the function object from the stack
        Object function = vm.pop();
        
        // Get the arguments from the stack
        Object[] args = new Object[argumentCount];
        for (int i = argumentCount - 1; i >= 0; i--) {
            args[i] = vm.pop();
        }
        
        // Call the function
        if (function instanceof String) {
            vm.callFunction((String)function, args);
        } else {
            throw new RuntimeException("Invalid function object");
        }
    }

    @Override
    public String toString() {
        return "CALL " + argumentCount;
    }
} 