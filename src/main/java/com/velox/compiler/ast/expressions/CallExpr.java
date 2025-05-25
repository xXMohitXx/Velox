package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;
import java.util.List;

/**
 * Represents a function call expression in the AST.
 */
public class CallExpr extends ASTNode {
    private final AST callee;
    private final Token paren;
    private final List<AST> arguments;

    public CallExpr(AST callee, Token paren, List<AST> arguments) {
        super(paren);
        this.callee = callee;
        this.paren = paren;
        this.arguments = arguments;
    }

    public AST getCallee() {
        return callee;
    }

    public Token getParen() {
        return paren;
    }

    public List<AST> getArguments() {
        return arguments;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitCallExpr(this);
    }
} 