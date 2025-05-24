package com.velox.compiler.lexer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for string interning to reduce memory usage
 */
public class StringInterner {
    private final ConcurrentHashMap<String, String> internedStrings;

    public StringInterner() {
        this.internedStrings = new ConcurrentHashMap<>();
    }

    public String intern(String str) {
        return internedStrings.computeIfAbsent(str, k -> k);
    }
} 