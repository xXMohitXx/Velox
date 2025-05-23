package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class ReturnInstruction implements Instruction {
    private final boolean hasValue;

    public ReturnInstruction(boolean hasValue) {
        this.hasValue = hasValue;
    }

    @Override
    public void execute(VirtualMachine vm) {
        Object result = hasValue ? vm.pop() : null;
        vm.returnFromFunction(result);
    }

    @Override
    public String toString() {
        return hasValue ? "RETURN" : "RETURN_VOID";
    }
} 