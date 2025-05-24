package com.velox.compiler;

import com.velox.compiler.ast.*;
import com.velox.compiler.token.Token;
import com.velox.compiler.token.TokenType;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private Stmt declaration() {
        try {
            if (match(TokenType.CLASS)) return classDeclaration();
            if (match(TokenType.FUN)) return function("function");
            if (match(TokenType.VAR)) return varDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt classDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect class name.");

        VariableExpr superclass = null;
        if (match(TokenType.LESS)) {
            consume(TokenType.IDENTIFIER, "Expect superclass name.");
            superclass = new VariableExpr(previous());
        }

        consume(TokenType.LEFT_BRACE, "Expect '{' before class body.");

        List<FunctionStmt> methods = new ArrayList<>();
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            methods.add(function("method"));
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after class body.");

        return new ClassStmt(name, superclass, methods);
    }

    private FunctionStmt function(String kind) {
        Token name = consume(TokenType.IDENTIFIER, "Expect " + kind + " name.");
        consume(TokenType.LEFT_PAREN, "Expect '(' after " + kind + " name.");
        List<Token> parameters = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                if (parameters.size() >= 255) {
                    error(peek(), "Cannot have more than 255 parameters.");
                }
                parameters.add(consume(TokenType.IDENTIFIER, "Expect parameter name."));
            } while (match(TokenType.COMMA));
        }
        consume(TokenType.RIGHT_PAREN, "Expect ')' after parameters.");
        consume(TokenType.LEFT_BRACE, "Expect '{' before " + kind + " body.");
        List<Stmt> body = block();
        return new FunctionStmt(name, parameters, body);
    }

    private Stmt varDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");

        AST initializer = null;
        if (match(TokenType.EQUAL)) {
            initializer = expression();
        }

        consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.");
        return new VarStmt(name, initializer);
    }

    private Stmt statement() {
        if (match(TokenType.FOR)) return forStatement();
        if (match(TokenType.IF)) return ifStatement();
        if (match(TokenType.PRINT)) return printStatement();
        if (match(TokenType.RETURN)) return returnStatement();
        if (match(TokenType.WHILE)) return whileStatement();
        if (match(TokenType.LEFT_BRACE)) return new BlockStmt(block());

        return expressionStatement();
    }

    private Stmt forStatement() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'for'.");

        Stmt initializer;
        if (match(TokenType.SEMICOLON)) {
            initializer = null;
        } else if (match(TokenType.VAR)) {
            initializer = varDeclaration();
        } else {
            initializer = expressionStatement();
        }

        AST condition = null;
        if (!check(TokenType.SEMICOLON)) {
            condition = expression();
        }
        consume(TokenType.SEMICOLON, "Expect ';' after loop condition.");

        AST increment = null;
        if (!check(TokenType.RIGHT_PAREN)) {
            increment = expression();
        }
        consume(TokenType.RIGHT_PAREN, "Expect ')' after for clauses.");

        Stmt body = statement();

        if (increment != null) {
            body = new BlockStmt(List.of(
                body,
                new ExpressionStmt(increment)
            ));
        }

        if (condition == null) condition = new LiteralExpr(true);
        body = new WhileStmt(condition, body);

        if (initializer != null) {
            body = new BlockStmt(List.of(initializer, body));
        }

        return body;
    }

    private Stmt ifStatement() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'if'.");
        AST condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expect ')' after if condition.");

        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(TokenType.ELSE)) {
            elseBranch = statement();
        }

        return new IfStmt(condition, thenBranch, elseBranch);
    }

    private Stmt printStatement() {
        AST value = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new PrintStmt(value);
    }

    private Stmt returnStatement() {
        Token keyword = previous();
        AST value = null;
        if (!check(TokenType.SEMICOLON)) {
            value = expression();
        }

        consume(TokenType.SEMICOLON, "Expect ';' after return value.");
        return new ReturnStmt(keyword, value);
    }

    private Stmt whileStatement() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'while'.");
        AST condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expect ')' after condition.");
        Stmt body = statement();

        return new WhileStmt(condition, body);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();

        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    private Stmt expressionStatement() {
        AST expr = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new ExpressionStmt(expr);
    }

    private AST expression() {
        return assignment();
    }

    private AST assignment() {
        AST expr = or();

        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            AST value = assignment();

            if (expr instanceof VariableExpr) {
                Token name = ((VariableExpr)expr).getToken();
                return new AssignExpr(name, value);
            } else if (expr instanceof GetExpr) {
                GetExpr get = (GetExpr)expr;
                return new SetExpr(get.getObject(), get.getToken(), value);
            }

            error(equals, "Invalid assignment target.");
        }

        return expr;
    }

    private AST or() {
        AST expr = and();

        while (match(TokenType.OR)) {
            Token operator = previous();
            AST right = and();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST and() {
        AST expr = equality();

        while (match(TokenType.AND)) {
            Token operator = previous();
            AST right = equality();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST equality() {
        AST expr = comparison();

        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            AST right = comparison();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST comparison() {
        AST expr = term();

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            AST right = term();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST term() {
        AST expr = factor();

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            AST right = factor();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST factor() {
        AST expr = unary();

        while (match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = previous();
            AST right = unary();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private AST unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            AST right = unary();
            return new UnaryExpr(operator, right);
        }

        return call();
    }

    private AST call() {
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

    private AST finishCall(AST callee) {
        List<AST> arguments = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                if (arguments.size() >= 255) {
                    error(peek(), "Cannot have more than 255 arguments.");
                }
                arguments.add(expression());
            } while (match(TokenType.COMMA));
        }

        Token paren = consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.");

        return new CallExpr(callee, paren, arguments);
    }

    private AST primary() {
        if (match(TokenType.FALSE)) return new LiteralExpr(false);
        if (match(TokenType.TRUE)) return new LiteralExpr(true);
        if (match(TokenType.NIL)) return new LiteralExpr(null);

        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new LiteralExpr(previous().getLiteral());
        }

        if (match(TokenType.SUPER)) {
            Token keyword = previous();
            consume(TokenType.DOT, "Expect '.' after 'super'.");
            Token method = consume(TokenType.IDENTIFIER, "Expect superclass method name.");
            return new SuperExpr(keyword, method);
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

        throw error(peek(), "Expect expression.");
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

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        if (token.getType() == TokenType.EOF) {
            throw new ParseError(" at end", message);
        } else {
            throw new ParseError(" at '" + token.getLexeme() + "'", message);
        }
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().getType() == TokenType.SEMICOLON) return;

            switch (peek().getType()) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }

            advance();
        }
    }

    private static class ParseError extends RuntimeException {
        ParseError(String where, String message) {
            super("Error" + where + ": " + message);
        }
    }
} 