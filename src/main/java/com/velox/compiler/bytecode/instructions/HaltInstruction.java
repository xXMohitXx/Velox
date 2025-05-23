package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class HaltInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        vm.stop();
    }

    @Override
    public String toString() {
        return "HALT";
    }
} 