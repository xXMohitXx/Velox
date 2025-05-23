package com.velox.compiler.ast.expressions;

import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.token.Token;
import com.velox.compiler.token.TokenType;

public class UnaryExpressionNode extends ASTNode {
    private final TokenType operator;
    private final ASTNode operand;

    public UnaryExpressionNode(Token token, TokenType operator, ASTNode operand) {
        super(token);
        this.operator = operator;
        this.operand = operand;
    }

    public TokenType getOperator() {
        return operator;
    }

    public ASTNode getOperand() {
        return operand;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitUnaryExpression(this);
    }
} 