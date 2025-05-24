package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;
import java.util.Objects;

public class NotEqualInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        vm.push(!Objects.equals(left, right));
    }

    @Override
    public String toString() {
        return "NOT_EQUAL";
    }
} 