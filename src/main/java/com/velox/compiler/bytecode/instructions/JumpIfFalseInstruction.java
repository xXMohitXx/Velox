package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class JumpIfFalseInstruction implements Instruction {
    private final int offset;

    public JumpIfFalseInstruction(int offset) {
        this.offset = offset;
    }

    @Override
    public void execute(VirtualMachine vm) {
        Object condition = vm.pop();
        if (!(condition instanceof Boolean)) {
            throw new RuntimeException("Condition must be a boolean");
        }
        
        if (!(Boolean) condition) {
            vm.jump(offset);
        }
    }

    @Override
    public String toString() {
        return "JMPF " + offset;
    }
} 