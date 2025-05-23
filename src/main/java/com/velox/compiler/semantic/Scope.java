package com.velox.compiler.semantic;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final Map<String, Symbol> symbols;

    public Scope() {
        this.symbols = new HashMap<>();
    }

    public void define(Symbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }

    public Symbol resolve(String name) {
        return symbols.get(name);
    }

    public boolean contains(String name) {
        return symbols.containsKey(name);
    }

    public Map<String, Symbol> getSymbols() {
        return new HashMap<>(symbols);
    }

    @Override
    public String toString() {
        return symbols.toString();
    }
} 