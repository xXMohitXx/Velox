package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class LoadLocalInstruction implements Instruction {
    private final int index;

    public LoadLocalInstruction(int index) {
        this.index = index;
    }

    @Override
    public void execute(VirtualMachine vm) {
        vm.push(vm.getLocal(index));
    }

    @Override
    public String toString() {
        return "LOAD_LOCAL " + index;
    }
}

public class StoreLocalInstruction implements Instruction {
    private final int index;

    public StoreLocalInstruction(int index) {
        this.index = index;
    }

    @Override
    public void execute(VirtualMachine vm) {
        Object value = vm.pop();
        vm.setLocal(index, value);
    }

    @Override
    public String toString() {
        return "STORE_LOCAL " + index;
    }
}

public class PopInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        vm.pop();
    }

    @Override
    public String toString() {
        return "POP";
    }
}

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