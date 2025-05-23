package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class JumpInstruction implements Instruction {
    private final int offset;

    public JumpInstruction(int offset) {
        this.offset = offset;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.jump(offset);
    }

    @Override
    public String toString() {
        return "JMP " + offset;
    }
} 