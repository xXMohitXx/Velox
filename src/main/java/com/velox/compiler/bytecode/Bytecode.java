package com.velox.compiler.bytecode;

import java.util.*;

public class Bytecode {
    private final List<Instruction> instructions;
    private final List<Constant> constants;
    private final Map<String, Integer> functionOffsets;
    private final Map<String, Integer> globalOffsets;

    public Bytecode() {
        this.instructions = new ArrayList<>();
        this.constants = new ArrayList<>();
        this.functionOffsets = new HashMap<>();
        this.globalOffsets = new HashMap<>();
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public int addConstant(Constant constant) {
        constants.add(constant);
        return constants.size() - 1;
    }

    public void addFunction(String name, int offset) {
        functionOffsets.put(name, offset);
    }

    public void addGlobal(String name, int offset) {
        globalOffsets.put(name, offset);
    }

    public Instruction getInstruction(int index) {
        if (index < 0 || index >= instructions.size()) {
            throw new IndexOutOfBoundsException("Invalid instruction index: " + index);
        }
        return instructions.get(index);
    }

    public Constant getConstant(int index) {
        if (index < 0 || index >= constants.size()) {
            throw new IndexOutOfBoundsException("Invalid constant index: " + index);
        }
        return constants.get(index);
    }

    public Integer getFunctionOffset(String name) {
        return functionOffsets.get(name);
    }

    public Integer getGlobalOffset(String name) {
        return globalOffsets.get(name);
    }

    public int getInstructionCount() {
        return instructions.size();
    }

    public int getConstantCount() {
        return constants.size();
    }

    public Set<String> getFunctionNames() {
        return Collections.unmodifiableSet(functionOffsets.keySet());
    }

    public Set<String> getGlobalNames() {
        return Collections.unmodifiableSet(globalOffsets.keySet());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Constants:\n");
        for (int i = 0; i < constants.size(); i++) {
            sb.append(String.format("  %d: %s\n", i, constants.get(i)));
        }
        
        sb.append("\nFunctions:\n");
        for (Map.Entry<String, Integer> entry : functionOffsets.entrySet()) {
            sb.append(String.format("  %s: %d\n", entry.getKey(), entry.getValue()));
        }
        
        sb.append("\nGlobals:\n");
        for (Map.Entry<String, Integer> entry : globalOffsets.entrySet()) {
            sb.append(String.format("  %s: %d\n", entry.getKey(), entry.getValue()));
        }
        
        sb.append("\nInstructions:\n");
        for (int i = 0; i < instructions.size(); i++) {
            sb.append(String.format("  %d: %s\n", i, instructions.get(i)));
        }
        
        return sb.toString();
    }
} 