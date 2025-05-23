package com.velox.compiler.bytecode;

public interface Instruction {
    void execute(VirtualMachine vm);
    String toString();
} 