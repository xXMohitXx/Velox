package com.velox.compiler.bytecode;

import java.util.Map;
import java.util.HashMap;

public interface Instruction {
    void execute(VirtualMachine vm);
    String toString();

    static Instruction fromByte(Byte b) {
        // Assuming there is a mapping from byte values to Instruction objects
        // This is a placeholder implementation
        return instructionMap.get(b);
    }

    public static final Map<Byte, Instruction> instructionMap = new HashMap<>();
} 