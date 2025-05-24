package com.velox.compiler.bytecode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Manages bytecode generation and storage.
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

    public void emit(OpCode opcode) {
        code.add((byte) opcode.ordinal());
        lines.add(0); // TODO: Add proper line tracking
    }

    public void emit(byte value) {
        code.add(value);
        lines.add(0);
    }

    public void emit(OpCode opcode, Object value) {
        emit(opcode);
        if (value instanceof String) {
            constants.add(value);
            emit((byte) (constants.size() - 1));
        } else if (value instanceof Number) {
            constants.add(value);
            emit((byte) (constants.size() - 1));
        } else {
            throw new IllegalArgumentException("Unsupported constant type: " + value.getClass());
        }
    }

    public int emitJump(OpCode opcode) {
        emit(opcode);
        int jumpPos = code.size();
        emit((byte) 0); // Placeholder for jump offset
        return jumpPos;
    }

    public void patchJump(int jumpPos) {
        int jumpOffset = code.size() - jumpPos - 1;
        code.set(jumpPos, (byte) jumpOffset);
    }

    public void emitLoop(int loopStart) {
        emit(OpCode.JUMP);
        int jumpOffset = code.size() - loopStart;
        emit((byte) jumpOffset);
    }

    public int getCurrentOffset() {
        return code.size();
    }

    public List<Byte> getCode() {
        return code;
    }

    public List<Object> getConstants() {
        return constants;
    }

    public List<Integer> getLines() {
        return lines;
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