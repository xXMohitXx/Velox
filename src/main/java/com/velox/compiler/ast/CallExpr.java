package com.velox.compiler.ast;

import com.velox.compiler.token.Token;
import java.util.List;

/**
 * Function call expression node
 */
public class CallExpr extends AST {
    private final AST callee;
    private final List<AST> arguments;

    public CallExpr(AST callee, Token paren, List<AST> arguments) {
        super(paren);
        this.callee = callee;
        this.arguments = arguments;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitCallExpr(this);
    }

    public AST getCallee() {
        return callee;
    }

    public List<AST> getArguments() {
        return arguments;
    }
} 