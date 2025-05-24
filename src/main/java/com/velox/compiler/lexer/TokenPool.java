package com.velox.compiler.lexer;

import com.velox.compiler.token.Token;
import com.velox.compiler.token.TokenType;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Object pool for Token instances to reduce garbage collection
 */
public class TokenPool {
    private final ConcurrentLinkedQueue<Token> pool;

    public TokenPool() {
        this.pool = new ConcurrentLinkedQueue<>();
    }

    public Token acquire(TokenType type, String lexeme, Object literal, int line, int column) {
        Token token = pool.poll();
        if (token == null) {
            token = new Token(type, lexeme, literal, line, column);
        }
        return token;
    }

    public void release(Token token) {
        pool.offer(token);
    }
} 