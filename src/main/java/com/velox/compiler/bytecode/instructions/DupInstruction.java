package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class DupInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object value = vm.peek();
        vm.push(value);
    }

    @Override
    public String toString() {
        return "DUP";
    }
} 