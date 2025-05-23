package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;
import java.util.Objects;

public class EqualInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        vm.push(Objects.equals(left, right));
    }

    @Override
    public String toString() {
        return "EQUAL";
    }
}

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

public class LessInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Number && right instanceof Number) {
            vm.push(((Number) left).doubleValue() < ((Number) right).doubleValue());
        } else {
            throw new RuntimeException("Invalid operands for less than comparison");
        }
    }

    @Override
    public String toString() {
        return "LESS";
    }
}

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

public class GreaterInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Number && right instanceof Number) {
            vm.push(((Number) left).doubleValue() > ((Number) right).doubleValue());
        } else {
            throw new RuntimeException("Invalid operands for greater than comparison");
        }
    }

    @Override
    public String toString() {
        return "GREATER";
    }
}

public class GreaterEqualInstruction implements Instruction {
    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        if (left instanceof Number && right instanceof Number) {
            vm.push(((Number) left).doubleValue() >= ((Number) right).doubleValue());
        } else {
            throw new RuntimeException("Invalid operands for greater than or equal comparison");
        }
    }

    @Override
    public String toString() {
        return "GREATER_EQUAL";
    }
} 