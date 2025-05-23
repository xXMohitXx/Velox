package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.token.Token;
import java.util.List;

public class CallNode extends ASTNode {
    private final ASTNode callee;
    private final List<ASTNode> arguments;

    public CallNode(Token token, ASTNode callee, List<ASTNode> arguments) {
        super(token);
        this.callee = callee;
        this.arguments = arguments;
    }

    public ASTNode getCallee() {
        return callee;
    }

    public List<ASTNode> getArguments() {
        return arguments;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitCall(this);
    }
} 