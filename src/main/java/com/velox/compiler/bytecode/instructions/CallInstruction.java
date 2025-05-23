package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class CallInstruction implements Instruction {
    private final String functionName;
    private final int argumentCount;

    public CallInstruction(String functionName, int argumentCount) {
        this.functionName = functionName;
        this.argumentCount = argumentCount;
    }

    @Override
    public void execute(VirtualMachine vm) {
        Object[] args = new Object[argumentCount];
        for (int i = argumentCount - 1; i >= 0; i--) {
            args[i] = vm.pop();
        }
        vm.callFunction(functionName, args);
    }

    @Override
    public String toString() {
        return String.format("CALL %s(%d)", functionName, argumentCount);
    }
} 