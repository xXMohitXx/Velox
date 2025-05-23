package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class LoadConstantInstruction implements Instruction {
    private final int index;

    public LoadConstantInstruction(int index) {
        this.index = index;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.push(vm.getConstant(index).getValue());
    }

    @Override
    public String toString() {
        return "LOAD_CONST " + index;
    }
} 