package com.velox.compiler.bytecode;

import java.util.Objects;

public class Constant {
    private final Object value;
    private final ConstantType type;

    public Constant(Object value, ConstantType type) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public ConstantType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", type, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Constant constant = (Constant) obj;
        return Objects.equals(value, constant.value) && Objects.equals(type, constant.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    public enum ConstantType {
        INTEGER,
        FLOAT,
        STRING,
        BOOLEAN,
        NULL,
        FUNCTION,
        CLASS,
        ARRAY,
        MAP
    }
} 