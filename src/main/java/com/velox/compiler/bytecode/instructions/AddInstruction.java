package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class AddInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Number && right instanceof Number) {
            if (left instanceof Double || right instanceof Double) {
                vm.push(((Number) left).doubleValue() + ((Number) right).doubleValue());
            } else if (left instanceof Long || right instanceof Long) {
                vm.push(((Number) left).longValue() + ((Number) right).longValue());
            } else {
                vm.push(((Number) left).intValue() + ((Number) right).intValue());
            }
        } else if (left instanceof String || right instanceof String) {
            vm.push(left.toString() + right.toString());
        } else {
            throw new RuntimeException("Invalid operands for addition");
        }
    }

    @Override
    public String toString() {
        return "ADD";
    }
} 