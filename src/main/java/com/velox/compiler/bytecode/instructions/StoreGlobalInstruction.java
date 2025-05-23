package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class StoreGlobalInstruction implements Instruction {
    private final String name;

    public StoreGlobalInstruction(String name) {
        this.name = name;
    }

    @Override
    public void execute(VirtualMachine vm) {
        Object value = vm.pop();
        vm.setGlobal(name, value);
    }

    @Override
    public String toString() {
        return "STORE_GLOBAL " + name;
    }
} 