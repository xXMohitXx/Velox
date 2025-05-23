package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;
import com.velox.compiler.error.RuntimeError;

public class ThrowInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object error = vm.pop();
        if (error instanceof String) {
            throw new RuntimeError((String) error, vm.getProgramCounter() - 1);
        } else {
            throw new RuntimeError(error.toString(), vm.getProgramCounter() - 1);
        }
    }

    @Override
    public String toString() {
        return "THROW";
    }
} 