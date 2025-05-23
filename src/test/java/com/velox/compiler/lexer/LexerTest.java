package com.velox.compiler.lexer;

import com.velox.compiler.token.Token;
import com.velox.compiler.token.TokenType;
import com.velox.compiler.error.LexicalError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class LexerTest {
    private Lexer lexer;

    @BeforeEach
    void setUp() {
        lexer = new Lexer();
    }

    @Test
    void testEmptyInput() {
        List<Token> tokens = lexer.tokenize("");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.EOF, tokens.get(0).getType());
    }

    @Test
    void testSingleCharacterTokens() {
        String input = "(){}[],.;+-*/";
        List<Token> tokens = lexer.tokenize(input);
        
        assertEquals(TokenType.LEFT_PAREN, tokens.get(0).getType());
        assertEquals(TokenType.RIGHT_PAREN, tokens.get(1).getType());
        assertEquals(TokenType.LEFT_BRACE, tokens.get(2).getType());
        assertEquals(TokenType.RIGHT_BRACE, tokens.get(3).getType());
        assertEquals(TokenType.LEFT_BRACKET, tokens.get(4).getType());
        assertEquals(TokenType.RIGHT_BRACKET, tokens.get(5).getType());
        assertEquals(TokenType.COMMA, tokens.get(6).getType());
        assertEquals(TokenType.DOT, tokens.get(7).getType());
        assertEquals(TokenType.SEMICOLON, tokens.get(8).getType());
        assertEquals(TokenType.PLUS, tokens.get(9).getType());
        assertEquals(TokenType.MINUS, tokens.get(10).getType());
        assertEquals(TokenType.STAR, tokens.get(11).getType());
        assertEquals(TokenType.SLASH, tokens.get(12).getType());
        assertEquals(TokenType.EOF, tokens.get(13).getType());
    }

    @Test
    void testStringLiteral() {
        String input = "\"hello world\"";
        List<Token> tokens = lexer.tokenize(input);
        
        assertEquals(2, tokens.size());
        assertEquals(TokenType.STRING, tokens.get(0).getType());
        assertEquals("hello world", tokens.get(0).getLiteral());
        assertEquals(TokenType.EOF, tokens.get(1).getType());
    }

    @Test
    void testNumberLiteral() {
        String input = "123 45.67";
        List<Token> tokens = lexer.tokenize(input);
        
        assertEquals(3, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        assertEquals(123.0, tokens.get(0).getLiteral());
        assertEquals(TokenType.NUMBER, tokens.get(1).getType());
        assertEquals(45.67, tokens.get(1).getLiteral());
        assertEquals(TokenType.EOF, tokens.get(2).getType());
    }

    @Test
    void testKeywords() {
        String input = "let const fn class if else while for";
        List<Token> tokens = lexer.tokenize(input);
        
        assertEquals(9, tokens.size());
        assertEquals(TokenType.LET, tokens.get(0).getType());
        assertEquals(TokenType.CONST, tokens.get(1).getType());
        assertEquals(TokenType.FN, tokens.get(2).getType());
        assertEquals(TokenType.CLASS, tokens.get(3).getType());
        assertEquals(TokenType.IF, tokens.get(4).getType());
        assertEquals(TokenType.ELSE, tokens.get(5).getType());
        assertEquals(TokenType.WHILE, tokens.get(6).getType());
        assertEquals(TokenType.FOR, tokens.get(7).getType());
        assertEquals(TokenType.EOF, tokens.get(8).getType());
    }

    @Test
    void testIdentifiers() {
        String input = "variable_name _123 camelCase";
        List<Token> tokens = lexer.tokenize(input);
        
        assertEquals(4, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.get(0).getType());
        assertEquals("variable_name", tokens.get(0).getLexeme());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        assertEquals("_123", tokens.get(1).getLexeme());
        assertEquals(TokenType.IDENTIFIER, tokens.get(2).getType());
        assertEquals("camelCase", tokens.get(2).getLexeme());
        assertEquals(TokenType.EOF, tokens.get(3).getType());
    }

    @Test
    void testComments() {
        String input = "// This is a comment\nlet x = 42;";
        List<Token> tokens = lexer.tokenize(input);
        
        assertEquals(5, tokens.size());
        assertEquals(TokenType.LET, tokens.get(0).getType());
        assertEquals(TokenType.IDENTIFIER, tokens.get(1).getType());
        assertEquals("x", tokens.get(1).getLexeme());
        assertEquals(TokenType.EQUAL, tokens.get(2).getType());
        assertEquals(TokenType.NUMBER, tokens.get(3).getType());
        assertEquals(42.0, tokens.get(3).getLiteral());
        assertEquals(TokenType.EOF, tokens.get(4).getType());
    }

    @Test
    void testInvalidCharacter() {
        assertThrows(LexicalError.class, () -> {
            lexer.tokenize("@");
        });
    }

    @Test
    void testUnterminatedString() {
        assertThrows(LexicalError.class, () -> {
            lexer.tokenize("\"unterminated string");
        });
    }
} 