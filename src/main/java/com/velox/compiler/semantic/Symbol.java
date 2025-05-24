package com.velox.compiler.semantic;

import com.velox.compiler.token.Token;

/**
 * Represents a symbol in the semantic analysis phase.
 */
public class Symbol {
    private final Token name;
    private final Object type;
    private final boolean isMutable;

    public Symbol(Token name, Object type, boolean isMutable) {
        this.name = name;
        this.type = type;
        this.isMutable = isMutable;
    }

    public Token getName() {
        return name;
    }

    public Object getType() {
        return type;
    }

    public boolean isMutable() {
        return isMutable;
    }

    @Override
    public String toString() {
        return String.format("%s: %s%s", name, type != null ? type.toString() : "inferred",
            isMutable ? " (mutable)" : "");
    }
} 