package com.velox.compiler.bytecode;

import java.util.List;
import java.util.ArrayList;

public class CallFrame {
    private final int returnAddress;
    private final List<Object> locals;
    private final int basePointer;

    public CallFrame(int returnAddress, int numLocals, int basePointer) {
        this.returnAddress = returnAddress;
        this.locals = new ArrayList<>(numLocals);
        for (int i = 0; i < numLocals; i++) {
            locals.add(null);
        }
        this.basePointer = basePointer;
    }

    public int getReturnAddress() {
        return returnAddress;
    }

    public Object getLocal(int index) {
        return locals.get(index);
    }

    public void setLocal(int index, Object value) {
        locals.set(index, value);
    }

    public int getBasePointer() {
        return basePointer;
    }
} 