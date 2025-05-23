package com.velox.compiler.semantic;

import com.velox.compiler.ast.*;
import com.velox.compiler.ast.expressions.*;
import com.velox.compiler.error.SemanticError;
import com.velox.compiler.error.ErrorHandler;

public class TypeChecker {
    private final ErrorHandler errorHandler;

    public TypeChecker(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void checkExpression(ExpressionNode expression) {
        if (expression instanceof BinaryExpressionNode) {
            checkBinaryExpression((BinaryExpressionNode) expression);
        } else if (expression instanceof UnaryExpressionNode) {
            checkUnaryExpression((UnaryExpressionNode) expression);
        } else if (expression instanceof LiteralNode) {
            checkLiteral((LiteralNode) expression);
        } else if (expression instanceof IdentifierNode) {
            checkIdentifier((IdentifierNode) expression);
        } else if (expression instanceof CallNode) {
            checkCall((CallNode) expression);
        } else if (expression instanceof AssignmentNode) {
            checkAssignment((AssignmentNode) expression);
        }
    }

    private void checkBinaryExpression(BinaryExpressionNode expression) {
        checkExpression(expression.getLeft());
        checkExpression(expression.getRight());

        // Check operator compatibility
        switch (expression.getOperator()) {
            case PLUS:
            case MINUS:
            case STAR:
            case SLASH:
                // Arithmetic operators
                if (!isNumericType(expression.getLeft()) || !isNumericType(expression.getRight())) {
                    error(expression, "Arithmetic operators require numeric operands");
                }
                break;
            case EQUAL_EQUAL:
            case BANG_EQUAL:
                // Comparison operators
                if (!areTypesCompatible(expression.getLeft(), expression.getRight())) {
                    error(expression, "Cannot compare values of different types");
                }
                break;
            case LESS:
            case LESS_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
                // Relational operators
                if (!isNumericType(expression.getLeft()) || !isNumericType(expression.getRight())) {
                    error(expression, "Relational operators require numeric operands");
                }
                break;
            case AND:
            case OR:
                // Logical operators
                if (!isBooleanType(expression.getLeft()) || !isBooleanType(expression.getRight())) {
                    error(expression, "Logical operators require boolean operands");
                }
                break;
        }
    }

    private void checkUnaryExpression(UnaryExpressionNode expression) {
        checkExpression(expression.getOperand());

        switch (expression.getOperator()) {
            case MINUS:
                if (!isNumericType(expression.getOperand())) {
                    error(expression, "Unary minus requires numeric operand");
                }
                break;
            case BANG:
                if (!isBooleanType(expression.getOperand())) {
                    error(expression, "Logical not requires boolean operand");
                }
                break;
        }
    }

    private void checkLiteral(LiteralNode expression) {
        // Literals are always valid
    }

    private void checkIdentifier(IdentifierNode expression) {
        // Check if identifier is defined in current scope
        // This is handled by the semantic analyzer
    }

    private void checkCall(CallNode expression) {
        checkExpression(expression.getCallee());

        // Check arguments
        for (ASTNode argument : expression.getArguments()) {
            checkExpression(argument);
        }

        // Check if callee is callable
        if (!isCallable(expression.getCallee())) {
            error(expression, "Expression is not callable");
        }
    }

    private void checkAssignment(AssignmentNode expression) {
        checkExpression(expression.getValue());

        // Check if target is assignable
        if (!isAssignable(expression.getName())) {
            error(expression, "Cannot assign to this expression");
        }
    }

    private boolean isNumericType(ASTNode node) {
        // TODO: Implement numeric type check
        return true;
    }

    private boolean isBooleanType(ASTNode node) {
        // TODO: Implement boolean type check
        return true;
    }

    private boolean areTypesCompatible(ASTNode left, ASTNode right) {
        // TODO: Implement type compatibility check
        return true;
    }

    private boolean isCallable(ASTNode node) {
        // TODO: Implement callable check
        return true;
    }

    private boolean isAssignable(String name) {
        // TODO: Implement assignable check
        return true;
    }

    private void error(ASTNode node, String message) {
        errorHandler.handleError(new SemanticError(node.getToken(), message));
    }
} 