package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class SubtractInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Integer && right instanceof Integer) {
            vm.push((Integer) left - (Integer) right);
        } else if (left instanceof Double && right instanceof Double) {
            vm.push((Double) left - (Double) right);
        } else {
            throw new RuntimeException("Invalid operands for subtraction");
        }
    }

    @Override
    public String toString() {
        return "SUB";
    }
} 