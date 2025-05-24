package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class JumpInstruction implements Instruction {
    private int offset;

    public JumpInstruction(int offset) {
        this.offset = offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.jump(offset);
    }

    @Override
    public String toString() {
        return "JUMP " + offset;
    }
} 