package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class JumpIfFalseInstruction implements Instruction {
    private int offset;

    public JumpIfFalseInstruction(int offset) {
        this.offset = offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void execute(VirtualMachine vm) {
        Object condition = vm.pop();
        if (!isTrue(condition)) {
            vm.jump(offset);
        }
    }

    private boolean isTrue(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof Number) return ((Number) value).doubleValue() != 0;
        if (value instanceof String) return !((String) value).isEmpty();
        return true;
    }

    @Override
    public String toString() {
        return "JUMP_IF_FALSE " + offset;
    }
} 