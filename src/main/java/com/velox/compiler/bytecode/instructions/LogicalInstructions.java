package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class AndInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Boolean && right instanceof Boolean) {
            vm.push((Boolean) left && (Boolean) right);
        } else {
            throw new RuntimeException("Invalid operands for logical AND");
        }
    }

    @Override
    public String toString() {
        return "AND";
    }
}

public class OrInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Boolean && right instanceof Boolean) {
            vm.push((Boolean) left || (Boolean) right);
        } else {
            throw new RuntimeException("Invalid operands for logical OR");
        }
    }

    @Override
    public String toString() {
        return "OR";
    }
}

public class NotInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object value = vm.pop();
        
        if (value instanceof Boolean) {
            vm.push(!(Boolean) value);
        } else {
            throw new RuntimeException("Invalid operand for logical NOT");
        }
    }

    @Override
    public String toString() {
        return "NOT";
    }
} 