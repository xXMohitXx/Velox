package com.velox.compiler.parser;

import com.velox.compiler.token.Token;
import com.velox.compiler.token.TokenType;
import com.velox.compiler.ast.*;
import com.velox.compiler.error.ParseError;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for the Velox language.
 * Converts a stream of tokens into an Abstract Syntax Tree (AST).
 */
public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public AST parse() throws ParseError {
        try {
            return expression();
        } catch (ParseError error) {
            throw error;
        }
    }

    private AST expression() throws ParseError {
        return assignment();
    }

    private AST assignment() throws ParseError {
        AST expr = or();

        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            AST value = assignment();

            if (expr instanceof VariableExpr) {
                Token name = ((VariableExpr) expr).getToken();
                return new AssignExpr(name, value);
            } else if (expr instanceof GetExpr) {
                GetExpr get = (GetExpr) expr;
                return new SetExpr(get.getObject(), get.getToken(), value);
            }

            throw new ParseError("Invalid assignment target.", equals);
        }

        return expr;
    }

    private AST or() throws ParseError {
        AST expr = and();

        while (match(TokenType.OR)) {
            Token operator = previous();
            AST right = and();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST and() throws ParseError {
        AST expr = equality();

        while (match(TokenType.AND)) {
            Token operator = previous();
            AST right = equality();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST equality() throws ParseError {
        AST expr = comparison();

        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            AST right = comparison();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST comparison() throws ParseError {
        AST expr = term();

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            AST right = term();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST term() throws ParseError {
        AST expr = factor();

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            AST right = factor();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST factor() throws ParseError {
        AST expr = unary();

        while (match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = previous();
            AST right = unary();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST unary() throws ParseError {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            AST right = unary();
            return new UnaryExpr(operator, right);
        }

        return call();
    }

    private AST call() throws ParseError {
        AST expr = primary();

        while (true) {
            if (match(TokenType.LEFT_PAREN)) {
                expr = finishCall(expr);
            } else if (match(TokenType.DOT)) {
                Token name = consume(TokenType.IDENTIFIER, "Expect property name after '.'.");
                expr = new GetExpr(expr, name);
            } else {
                break;
            }
        }

        return expr;
    }

    private AST finishCall(AST callee) throws ParseError {
        List<AST> arguments = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                arguments.add(expression());
            } while (match(TokenType.COMMA));
        }

        Token paren = consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.");
        return new CallExpr(callee, paren, arguments);
    }

    private AST primary() throws ParseError {
        if (match(TokenType.FALSE)) return new LiteralExpr(false);
        if (match(TokenType.TRUE)) return new LiteralExpr(true);
        if (match(TokenType.NIL)) return new LiteralExpr(null);

        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new LiteralExpr(previous().getLiteral());
        }

        if (match(TokenType.THIS)) return new ThisExpr(previous());

        if (match(TokenType.IDENTIFIER)) {
            return new VariableExpr(previous());
        }

        if (match(TokenType.LEFT_PAREN)) {
            AST expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new GroupingExpr(expr);
        }

        throw new ParseError("Expect expression.", peek());
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String message) throws ParseError {
        if (check(type)) return advance();
        throw new ParseError(message, peek());
    }
} 