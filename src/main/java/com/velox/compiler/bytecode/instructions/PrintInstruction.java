package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class PrintInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object value = vm.pop();
        System.out.println(value);
    }

    @Override
    public String toString() {
        return "PRINT";
    }
} 