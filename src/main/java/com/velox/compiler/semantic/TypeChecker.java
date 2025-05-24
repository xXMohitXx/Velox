package com.velox.compiler.semantic;

import com.velox.compiler.ast.*;
import com.velox.compiler.error.ErrorHandler;

public class TypeChecker {
    private final ErrorHandler errorHandler;

    public TypeChecker(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void checkExpression(AST expression) {
        if (expression instanceof BinaryExpr) {
            checkBinaryExpression((BinaryExpr) expression);
        } else if (expression instanceof UnaryExpr) {
            checkUnaryExpression((UnaryExpr) expression);
        } else if (expression instanceof LiteralExpr) {
            checkLiteral((LiteralExpr) expression);
        } else if (expression instanceof VariableExpr) {
            checkIdentifier((VariableExpr) expression);
        } else if (expression instanceof CallExpr) {
            checkCall((CallExpr) expression);
        } else if (expression instanceof AssignExpr) {
            checkAssignment((AssignExpr) expression);
        }
    }

    private void checkBinaryExpression(BinaryExpr expression) {
        checkExpression(expression.getLeft());
        checkExpression(expression.getRight());

        // Check operator compatibility
        String operator = expression.getOperator().getLexeme();
        switch (operator) {
            case "+":
            case "-":
            case "*":
            case "/":
                // Arithmetic operators
                if (!isNumericType(expression.getLeft()) || !isNumericType(expression.getRight())) {
                    error(expression, "Arithmetic operators require numeric operands");
                }
                break;
            case "==":
            case "!=":
                // Comparison operators
                if (!areTypesCompatible(expression.getLeft(), expression.getRight())) {
                    error(expression, "Cannot compare values of different types");
                }
                break;
            case "<":
            case "<=":
            case ">":
            case ">=":
                // Relational operators
                if (!isNumericType(expression.getLeft()) || !isNumericType(expression.getRight())) {
                    error(expression, "Relational operators require numeric operands");
                }
                break;
            case "&&":
            case "||":
                // Logical operators
                if (!isBooleanType(expression.getLeft()) || !isBooleanType(expression.getRight())) {
                    error(expression, "Logical operators require boolean operands");
                }
                break;
        }
    }

    private void checkUnaryExpression(UnaryExpr expression) {
        checkExpression(expression.getRight());

        String operator = expression.getOperator().getLexeme();
        switch (operator) {
            case "-":
                if (!isNumericType(expression.getRight())) {
                    error(expression, "Unary minus requires numeric operand");
                }
                break;
            case "!":
                if (!isBooleanType(expression.getRight())) {
                    error(expression, "Logical not requires boolean operand");
                }
                break;
        }
    }

    private void checkLiteral(LiteralExpr expression) {
        // Literals are always valid
    }

    private void checkIdentifier(VariableExpr expression) {
        // Check if identifier is defined in current scope
        // This is handled by the semantic analyzer
    }

    private void checkCall(CallExpr expression) {
        checkExpression(expression.getCallee());

        // Check arguments
        for (AST argument : expression.getArguments()) {
            checkExpression(argument);
        }

        // Check if callee is callable
        if (!isCallable(expression.getCallee())) {
            error(expression, "Expression is not callable");
        }
    }

    private void checkAssignment(AssignExpr expression) {
        checkExpression(expression.getValue());

        // Check if target is assignable
        if (!isAssignable(expression.getToken().getLexeme())) {
            error(expression, "Cannot assign to this expression");
        }
    }

    private boolean isNumericType(AST node) {
        if (node instanceof LiteralExpr) {
            Object value = ((LiteralExpr) node).getValue();
            return value instanceof Number;
        }
        // TODO: Add type inference for variables and expressions
        return false;
    }

    private boolean isBooleanType(AST node) {
        if (node instanceof LiteralExpr) {
            Object value = ((LiteralExpr) node).getValue();
            return value instanceof Boolean;
        }
        // TODO: Add type inference for variables and expressions
        return false;
    }

    private boolean areTypesCompatible(AST left, AST right) {
        // For now, only check literal types
        if (left instanceof LiteralExpr && right instanceof LiteralExpr) {
            Object leftValue = ((LiteralExpr) left).getValue();
            Object rightValue = ((LiteralExpr) right).getValue();
            
            // Same type is always compatible
            if (leftValue.getClass() == rightValue.getClass()) {
                return true;
            }
            
            // Numbers are compatible with each other
            if (leftValue instanceof Number && rightValue instanceof Number) {
                return true;
            }
            
            // Strings are compatible with each other
            if (leftValue instanceof String && rightValue instanceof String) {
                return true;
            }
            
            // Booleans are compatible with each other
            if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
                return true;
            }
            
            return false;
        }
        // TODO: Add type inference for variables and expressions
        return true;
    }

    private boolean isCallable(AST node) {
        // For now, only function calls and method calls are callable
        return node instanceof CallExpr || 
               (node instanceof VariableExpr && 
                ((VariableExpr) node).getToken().getLexeme().startsWith("fn_"));
    }

    private boolean isAssignable(String name) {
        // For now, only variables can be assigned to
        // TODO: Add support for object properties and array elements
        return !name.startsWith("fn_") && !name.equals("this") && !name.equals("super");
    }

    private void error(AST node, String message) {
        errorHandler.handleError(message);
    }
} 