package com.velox.compiler.token;

import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

public class TokenPool {
    private final Map<String, Stack<Token>> pools;
    private final int maxPoolSize;

    public TokenPool() {
        this.pools = new HashMap<>();
        this.maxPoolSize = 1000;
    }

    public Token acquire(TokenType type, String lexeme, Object literal, int line, int column) {
        String key = type.toString();
        Stack<Token> pool = pools.get(key);
        
        if (pool != null && !pool.isEmpty()) {
            Token token = pool.pop();
            token.reset(type, lexeme, literal, line, column);
            return token;
        }

        return new Token(type, lexeme, literal, line, column);
    }

    public void release(Token token) {
        String key = token.getType().toString();
        Stack<Token> pool = pools.computeIfAbsent(key, k -> new Stack<>());
        
        if (pool.size() < maxPoolSize) {
            pool.push(token);
        }
    }

    public void clear() {
        pools.clear();
    }
} 