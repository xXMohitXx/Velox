package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class PopInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        vm.pop();
    }

    @Override
    public String toString() {
        return "POP";
    }
} 