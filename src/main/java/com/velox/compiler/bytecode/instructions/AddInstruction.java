package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class AddInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Integer && right instanceof Integer) {
            vm.push((Integer) left + (Integer) right);
        } else if (left instanceof Double && right instanceof Double) {
            vm.push((Double) left + (Double) right);
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