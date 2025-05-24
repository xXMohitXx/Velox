package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class DivideInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Number && right instanceof Number) {
            double rightValue = ((Number) right).doubleValue();
            if (rightValue == 0) {
                throw new RuntimeException("Division by zero");
            }
            vm.push(((Number) left).doubleValue() / rightValue);
        } else {
            throw new RuntimeException("Invalid operands for division");
        }
    }

    @Override
    public String toString() {
        return "DIV";
    }
} 