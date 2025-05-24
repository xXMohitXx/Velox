package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class MultiplyInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Number && right instanceof Number) {
            if (left instanceof Double || right instanceof Double) {
                vm.push(((Number) left).doubleValue() * ((Number) right).doubleValue());
            } else if (left instanceof Long || right instanceof Long) {
                vm.push(((Number) left).longValue() * ((Number) right).longValue());
            } else {
                vm.push(((Number) left).intValue() * ((Number) right).intValue());
            }
        } else {
            throw new RuntimeException("Invalid operands for multiplication");
        }
    }

    @Override
    public String toString() {
        return "MUL";
    }
} 