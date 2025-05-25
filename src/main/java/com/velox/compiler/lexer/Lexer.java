package com.velox.compiler.lexer;

import com.velox.compiler.token.Token;
import com.velox.compiler.token.TokenType;
import com.velox.compiler.error.LexicalError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lexer for the Velox language.
 * Converts source code into a stream of tokens.
 */
public class Lexer {
    private final StringInterner stringInterner;
    private String source;
    private int current;
    private int start;
    private int line;
    private int column;
    private final List<Token> tokens;
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("class", TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("fun", TokenType.FUN);
        keywords.put("if", TokenType.IF);
        keywords.put("nil", TokenType.NIL);
        keywords.put("or", TokenType.OR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("var", TokenType.VAR);
        keywords.put("while", TokenType.WHILE);
        keywords.put("public", TokenType.PUBLIC);
        keywords.put("private", TokenType.PRIVATE);
        keywords.put("protected", TokenType.PROTECTED);
        keywords.put("static", TokenType.STATIC);
        keywords.put("final", TokenType.FINAL);
        keywords.put("abstract", TokenType.ABSTRACT);
    }

    public Lexer() {
        this.stringInterner = new StringInterner();
        this.current = 0;
        this.start = 0;
        this.line = 1;
        this.column = 1;
        this.tokens = new ArrayList<>();
    }

    public Lexer(String source) {
        this.stringInterner = new StringInterner();
        this.source = source;
        this.current = 0;
        this.start = 0;
        this.line = 1;
        this.column = 1;
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize(String source) throws LexicalError {
        this.source = source;
        this.current = 0;
        this.start = 0;
        this.line = 1;
        this.column = 1;
        this.tokens.clear();

        while (!isAtEnd()) {
            start = current;
            try {
                scanToken();
            } catch (Exception e) {
                throw new LexicalError("Error at line " + line + ", column " + column + ": " + e.getMessage(), e);
            }
        }

        tokens.add(createToken(TokenType.EOF));
        return tokens;
    }

    private void scanToken() throws LexicalError {
        char c = advance();
        switch (c) {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case '[': addToken(TokenType.LEFT_BRACKET); break;
            case ']': addToken(TokenType.RIGHT_BRACKET); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(match('-') ? TokenType.MINUS_MINUS : TokenType.MINUS); break;
            case '+': addToken(match('+') ? TokenType.PLUS_PLUS : TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;
            case '?': addToken(TokenType.QUESTION); break;
            case ':': addToken(TokenType.COLON); break;
            case '!': addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG); break;
            case '=': addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL); break;
            case '<': addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS); break;
            case '>': addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER); break;
            case '/':
                if (match('/')) {
                    // Comment
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                column = 1;
                break;
            case '"': string(); break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    throw new LexicalError("Unexpected character: " + c);
                }
                break;
        }
    }

    private void string() throws LexicalError {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
                column = 1;
            }
            advance();
        }

        if (isAtEnd()) {
            throw new LexicalError("Unterminated string at line " + line);
        }

        advance(); // The closing ".

        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, stringInterner.intern(value));
    }

    private void number() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) advance();
        }

        String value = source.substring(start, current);
        addToken(TokenType.NUMBER, Double.parseDouble(value));
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = TokenType.IDENTIFIER;
        addToken(type, stringInterner.intern(text));
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

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, stringInterner.intern(text), literal, line, column));
    }

    private Token createToken(TokenType type) {
        return createToken(type, null);
    }

    private Token createToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        return new Token(type, stringInterner.intern(text), literal, line, column);
    }
} 