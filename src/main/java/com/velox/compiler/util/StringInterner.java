package com.velox.compiler.util;

import java.util.Set;
import java.util.HashSet;

public class StringInterner {
    private final Set<String> interned;

    public StringInterner() {
        this.interned = new HashSet<>();
    }

    public String intern(String str) {
        if (str == null) return null;
        if (interned.contains(str)) {
            return str;
        }
        interned.add(str);
        return str;
    }

    public void clear() {
        interned.clear();
    }
} 