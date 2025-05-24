package com.velox.compiler.bytecode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents the compiled bytecode for a Velox program
 */
public class Bytecode {
    private final List<Byte> code;
    private final List<Object> constants;
    private final List<Integer> lines;
    private final Map<String, Integer> functionOffsets = new HashMap<>();

    public Bytecode() {
        this.code = new ArrayList<>();
        this.constants = new ArrayList<>();
        this.lines = new ArrayList<>();
    }

    public void add(byte instruction, int line) {
        code.add(instruction);
        lines.add(line);
    }

    public void addConstant(Object value) {
        constants.add(value);
    }

    public List<Byte> getCode() {
        return new ArrayList<>(code);
    }

    public List<Object> getConstants() {
        return new ArrayList<>(constants);
    }

    public List<Integer> getLines() {
        return new ArrayList<>(lines);
    }

    public int getCurrentOffset() {
        return code.size();
    }

    public void patchJump(int offset, int jumpTo) {
        code.set(offset, (byte)(jumpTo & 0xFF));
        code.set(offset + 1, (byte)((jumpTo >> 8) & 0xFF));
    }

    public int getInstructionCount() {
        return code.size();
    }

    public Instruction getInstruction(int index) {
        if (index < 0 || index >= code.size()) {
            throw new IndexOutOfBoundsException("Instruction index out of bounds: " + index);
        }
        return Instruction.fromByte(code.get(index));
    }

    public Integer getFunctionOffset(String name) {
        // Assuming there is a map or list that stores function names and their offsets
        // This is a placeholder implementation
        return functionOffsets.get(name);
    }

    public Constant getConstant(int index) {
        if (index < 0 || index >= constants.size()) {
            throw new IndexOutOfBoundsException("Constant index out of bounds: " + index);
        }
        return (Constant) constants.get(index);
    }
} 