package com.velox.compiler.bytecode.instructions;

import com.velox.compiler.bytecode.Instruction;
import com.velox.compiler.bytecode.VirtualMachine;

public class CompareInstruction implements Instruction {
    public enum Operator {
        EQUALS,
        NOT_EQUALS,
        LESS_THAN,
        LESS_EQUALS,
        GREATER_THAN,
        GREATER_EQUALS
    }

    private final Operator operator;

    public CompareInstruction(Operator operator) {
        this.operator = operator;
    }

    @Override
    public void execute(VirtualMachine vm) {
        Object right = vm.pop();
        Object left = vm.pop();
        
        boolean result;
        if (left instanceof Integer && right instanceof Integer) {
            result = compareIntegers((Integer) left, (Integer) right);
        } else if (left instanceof Double && right instanceof Double) {
            result = compareDoubles((Double) left, (Double) right);
        } else if (left instanceof String && right instanceof String) {
            result = compareStrings((String) left, (String) right);
        } else {
            throw new RuntimeException("Invalid operands for comparison");
        }
        
        vm.push(result);
    }

    private boolean compareIntegers(Integer left, Integer right) {
        switch (operator) {
            case EQUALS: return left.equals(right);
            case NOT_EQUALS: return !left.equals(right);
            case LESS_THAN: return left < right;
            case LESS_EQUALS: return left <= right;
            case GREATER_THAN: return left > right;
            case GREATER_EQUALS: return left >= right;
            default: throw new RuntimeException("Invalid comparison operator");
        }
    }

    private boolean compareDoubles(Double left, Double right) {
        switch (operator) {
            case EQUALS: return left.equals(right);
            case NOT_EQUALS: return !left.equals(right);
            case LESS_THAN: return left < right;
            case LESS_EQUALS: return left <= right;
            case GREATER_THAN: return left > right;
            case GREATER_EQUALS: return left >= right;
            default: throw new RuntimeException("Invalid comparison operator");
        }
    }

    private boolean compareStrings(String left, String right) {
        switch (operator) {
            case EQUALS: return left.equals(right);
            case NOT_EQUALS: return !left.equals(right);
            case LESS_THAN: return left.compareTo(right) < 0;
            case LESS_EQUALS: return left.compareTo(right) <= 0;
            case GREATER_THAN: return left.compareTo(right) > 0;
            case GREATER_EQUALS: return left.compareTo(right) >= 0;
            default: throw new RuntimeException("Invalid comparison operator");
        }
    }

    @Override
    public String toString() {
        return "CMP " + operator;
    }
} 