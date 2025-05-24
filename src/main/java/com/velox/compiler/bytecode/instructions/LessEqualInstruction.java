package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class LessEqualInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Number && right instanceof Number) {
            vm.push(((Number) left).doubleValue() <= ((Number) right).doubleValue());
        } else {
            throw new RuntimeException("Invalid operands for less than or equal comparison");
        }
    }

    @Override
    public String toString() {
        return "LESS_EQUAL";
    }
} 