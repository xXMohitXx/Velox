package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class StoreLocalInstruction implements Instruction {
    private final int index;

    public StoreLocalInstruction(int index) {
        this.index = index;
    }

    @Override
    public void execute(VirtualMachine vm) {
        Object value = vm.pop();
        vm.setLocal(index, value);
    }

    @Override
    public String toString() {
        return "STORE_LOCAL " + index;
    }
} 