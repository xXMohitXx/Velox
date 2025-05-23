package com.velox.compiler.lexer;

import com.velox.compiler.token.Token;
import com.velox.compiler.token.TokenType;
import com.velox.compiler.error.LexicalError;
import java.util.ArrayList;
import java.util.List;

/**
 * Lexer for the Velox language.
 * Converts source code into a stream of tokens.
 */
public class Lexer {
    private final StringInterner stringInterner;
    private final TokenPool tokenPool;
    private String source;
    private int current;
    private int start;
    private int line;
    private int column;

    public Lexer() {
        this.stringInterner = new StringInterner();
        this.tokenPool = new TokenPool();
        this.current = 0;
        this.start = 0;
        this.line = 1;
        this.column = 1;
    }

    public List<Token> tokenize(String source) throws LexicalError {
        this.source = source;
        this.current = 0;
        this.start = 0;
        this.line = 1;
        this.column = 1;

        List<Token> tokens = new ArrayList<>();
        while (!isAtEnd()) {
            start = current;
            try {
                Token token = scanToken();
                if (token != null) {
                    tokens.add(token);
                }
            } catch (Exception e) {
                throw new LexicalError("Error at line " + line + ", column " + column, e);
            }
        }

        tokens.add(createToken(TokenType.EOF));
        return tokens;
    }

    private Token scanToken() {
        char c = advance();
        switch (c) {
            case '(': return createToken(TokenType.LEFT_PAREN);
            case ')': return createToken(TokenType.RIGHT_PAREN);
            case '{': return createToken(TokenType.LEFT_BRACE);
            case '}': return createToken(TokenType.RIGHT_BRACE);
            case '[': return createToken(TokenType.LEFT_BRACKET);
            case ']': return createToken(TokenType.RIGHT_BRACKET);
            case ',': return createToken(TokenType.COMMA);
            case '.': return createToken(TokenType.DOT);
            case '-': return createToken(TokenType.MINUS);
            case '+': return createToken(TokenType.PLUS);
            case ';': return createToken(TokenType.SEMICOLON);
            case '*': return createToken(TokenType.STAR);
            case '!': return createToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
            case '=': return createToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
            case '<': return createToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
            case '>': return createToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
            case '/':
                if (match('/')) {
                    // Comment
                    while (peek() != '\n' && !isAtEnd()) advance();
                    return null;
                }
                return createToken(TokenType.SLASH);
            case ' ':
            case '\r':
            case '\t':
                return null;
            case '\n':
                line++;
                column = 1;
                return null;
            case '"': return string();
            default:
                if (isDigit(c)) return number();
                if (isAlpha(c)) return identifier();
                throw new LexicalError("Unexpected character: " + c);
        }
    }

    private Token string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
                column = 1;
            }
            advance();
        }

        if (isAtEnd()) {
            throw new LexicalError("Unterminated string.");
        }

        advance(); // The closing ".

        String value = source.substring(start + 1, current - 1);
        return createToken(TokenType.STRING, stringInterner.intern(value));
    }

    private Token number() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) advance();
        }

        String value = source.substring(start, current);
        return createToken(TokenType.NUMBER, Double.parseDouble(value));
    }

    private Token identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = Keywords.get(text);
        if (type == null) type = TokenType.IDENTIFIER;

        return createToken(type, stringInterner.intern(text));
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        column++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        current++;
        column++;
        return source.charAt(current - 1);
    }

    private Token createToken(TokenType type) {
        return createToken(type, null);
    }

    private Token createToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        return tokenPool.acquire(type, stringInterner.intern(text), literal, line, column);
    }
} 