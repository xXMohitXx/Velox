package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class LoadGlobalInstruction implements Instruction {
    private final String name;

    public LoadGlobalInstruction(String name) {
        this.name = name;
    }

    @Override
    public void execute(VirtualMachine vm) {
        Object value = vm.getGlobal(name);
        if (value == null) {
            throw new RuntimeException("Undefined global variable: " + name);
        }
        vm.push(value);
    }

    @Override
    public String toString() {
        return "LOAD_GLOBAL " + name;
    }
} 