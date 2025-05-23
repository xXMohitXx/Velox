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

public class ReturnInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        vm.stop();
    }

    @Override
    public String toString() {
        return "RETURN";
    }
}

public class CallInstruction implements Instruction {
    private final int argumentCount;

    public CallInstruction(int argumentCount) {
        this.argumentCount = argumentCount;
    }

    @Override
    public void execute(VirtualMachine vm) {
        // Get the function object from the stack
        Object function = vm.pop();
        
        // Get the arguments from the stack
        Object[] args = new Object[argumentCount];
        for (int i = argumentCount - 1; i >= 0; i--) {
            args[i] = vm.pop();
        }
        
        // TODO: Implement function calling
        // For now, just push a placeholder result
        vm.push(null);
    }

    @Override
    public String toString() {
        return "CALL " + argumentCount;
    }
} 