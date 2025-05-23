package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class LoadLocalInstruction implements Instruction {
    private final int index;

    public LoadLocalInstruction(int index) {
        this.index = index;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.push(vm.getLocal(index));
    }

    @Override
    public String toString() {
        return "LOAD_LOCAL " + index;
    }
} 