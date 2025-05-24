package com.velox.compiler.semantic;

import com.velox.compiler.ast.TypeAnnotation;

public class Symbol {
    private final String name;
    private final TypeAnnotation type;
    private final boolean isConst;

    public Symbol(String name, TypeAnnotation type, boolean isConst) {
        this.name = name;
        this.type = type;
        this.isConst = isConst;
    }

    public String getName() {
        return name;
    }

    public TypeAnnotation getType() {
        return type;
    }

    public boolean isConst() {
        return isConst;
    }

    @Override
    public String toString() {
        return String.format("%s: %s%s", name, type != null ? type.getTypeName() : "inferred",
            isConst ? " (const)" : "");
    }
} 