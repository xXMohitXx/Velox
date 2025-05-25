package com.velox.compiler.semantic;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ClassNode;
import com.velox.compiler.ast.expressions.*;
import com.velox.compiler.ast.statements.*;
import com.velox.compiler.error.ErrorHandler;
import com.velox.compiler.error.SemanticError;
import com.velox.compiler.token.Token;
import java.util.*;

public class TypeChecker {
    private final ErrorHandler errorHandler;
    private final Stack<Map<String, Type>> scopes;
    private Type currentReturnType;
    private final Map<AST, Type> nodeTypes = new HashMap<>();

    public TypeChecker(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.scopes = new Stack<>();
        this.currentReturnType = Type.VOID;
    }

    public void check(AST node) {
        enterScope();
        try {
            if (node instanceof BinaryExpr) {
                checkBinaryExpr((BinaryExpr) node);
            } else if (node instanceof UnaryExpr) {
                checkUnaryExpr((UnaryExpr) node);
            } else if (node instanceof LiteralExpr) {
                checkLiteralExpr((LiteralExpr) node);
            } else if (node instanceof VariableExpr) {
                checkVariableExpr((VariableExpr) node);
            } else if (node instanceof AssignExpr) {
                checkAssignExpr((AssignExpr) node);
            } else if (node instanceof CallExpr) {
                checkCallExpr((CallExpr) node);
            } else if (node instanceof GetExpr) {
                checkGetExpr((GetExpr) node);
            } else if (node instanceof SetExpr) {
                checkSetExpr((SetExpr) node);
            } else if (node instanceof ThisExpr) {
                checkThisExpr((ThisExpr) node);
            } else if (node instanceof SuperExpr) {
                checkSuperExpr((SuperExpr) node);
            } else if (node instanceof GroupingExpr) {
                checkGroupingExpr((GroupingExpr) node);
            } else if (node instanceof IfStmt) {
                checkIfStmt((IfStmt) node);
            } else if (node instanceof WhileStmt) {
                checkWhileStmt((WhileStmt) node);
            } else if (node instanceof ReturnStmt) {
                checkReturnStmt((ReturnStmt) node);
            }
        } finally {
            exitScope();
        }
    }

    private void checkBinaryExpr(BinaryExpr expr) {
        check(expr.getLeft());
        check(expr.getRight());
        
        Type leftType = getType(expr.getLeft());
        Type rightType = getType(expr.getRight());
        
        switch (expr.getOperator().getType()) {
            case PLUS:
                if (leftType == Type.STRING || rightType == Type.STRING) {
                    setType(expr, Type.STRING);
                } else if (leftType == Type.NUMBER && rightType == Type.NUMBER) {
                    setType(expr, Type.NUMBER);
                } else {
                    error(expr.getOperator(), "Operands must be numbers or strings");
                }
                break;
            case MINUS:
            case STAR:
            case SLASH:
                if (leftType == Type.NUMBER && rightType == Type.NUMBER) {
                    setType(expr, Type.NUMBER);
                } else {
                    error(expr.getOperator(), "Operands must be numbers");
                }
                break;
            case EQUAL_EQUAL:
            case BANG_EQUAL:
                if (leftType == rightType) {
                    setType(expr, Type.BOOLEAN);
                } else {
                    error(expr.getOperator(), "Operands must be of the same type");
                }
                break;
            case GREATER:
            case GREATER_EQUAL:
            case LESS:
            case LESS_EQUAL:
                if (leftType == Type.NUMBER && rightType == Type.NUMBER) {
                    setType(expr, Type.BOOLEAN);
                } else {
                    error(expr.getOperator(), "Operands must be numbers");
                }
                break;
            default:
                error(expr.getOperator(), "Invalid operator for binary expression");
                break;
        }
    }

    private void checkUnaryExpr(UnaryExpr expr) {
        check(expr.getRight());
        Type rightType = getType(expr.getRight());
        
        switch (expr.getOperator().getType()) {
            case MINUS:
                if (rightType == Type.NUMBER) {
                    setType(expr, Type.NUMBER);
                } else {
                    error(expr.getOperator(), "Operand must be a number");
                }
                break;
            case BANG:
                if (rightType == Type.BOOLEAN) {
                    setType(expr, Type.BOOLEAN);
                } else {
                    error(expr.getOperator(), "Operand must be a boolean");
                }
                break;
            case PLUS_PLUS:
                if (rightType == Type.NUMBER) {
                    setType(expr, Type.NUMBER);
                } else {
                    error(expr.getOperator(), "Operand must be a number");
                }
                break;
            default:
                error(expr.getOperator(), "Invalid operator for unary expression");
                break;
        }
    }

    private void checkLiteralExpr(LiteralExpr expr) {
        Object value = expr.getValue();
        if (value == null) {
            setType(expr, Type.NIL);
        } else if (value instanceof Boolean) {
            setType(expr, Type.BOOLEAN);
        } else if (value instanceof Number) {
            setType(expr, Type.NUMBER);
        } else if (value instanceof String) {
            setType(expr, Type.STRING);
        }
    }

    private void checkVariableExpr(VariableExpr expr) {
        Type type = resolveVariable(expr.getToken());
        if (type == null) {
            error(expr.getToken(), "Undefined variable '" + expr.getToken().getLexeme() + "'");
        } else {
            setType(expr, type);
        }
    }

    private void checkAssignExpr(AssignExpr expr) {
        check(expr.getValue());
        Type valueType = getType(expr.getValue());
        
        Type variableType = resolveVariable(expr.getName());
        if (variableType == null) {
            defineVariable(expr.getName(), valueType);
        } else if (variableType != valueType) {
            error(expr.getName(), "Cannot assign " + valueType + " to " + variableType);
        }
        
        setType(expr, valueType);
    }

    private void checkCallExpr(CallExpr expr) {
        check(expr.getCallee());
        Type calleeType = getType(expr.getCallee());
        
        if (calleeType != Type.FUNCTION) {
            error(expr.getParen(), "Can only call functions");
            return;
        }
        
        for (AST arg : expr.getArguments()) {
            check(arg);
        }
        
        // TODO: Check argument types against function parameter types
        setType(expr, Type.ANY); // Temporary until we implement function types
    }

    private void checkGetExpr(GetExpr expr) {
        check(expr.getObject());
        Type objectType = getType(expr.getObject());
        
        if (objectType != Type.OBJECT) {
            error(expr.getName(), "Only objects have properties");
            return;
        }
        
        // TODO: Check if property exists and get its type
        setType(expr, Type.ANY); // Temporary until we implement object types
    }

    private void checkSetExpr(SetExpr expr) {
        check(expr.getObject());
        check(expr.getValue());
        
        Type objectType = getType(expr.getObject());
        if (objectType != Type.OBJECT) {
            error(expr.getName(), "Only objects have properties");
            return;
        }
        
        // TODO: Check if property exists and if value type matches
        setType(expr, getType(expr.getValue()));
    }

    private void checkThisExpr(ThisExpr expr) {
        if (currentClass == null) {
            error(expr.getKeyword(), "Cannot use 'this' outside of a class");
            return;
        }
        setType(expr, Type.OBJECT); // TODO: Use actual class type
    }

    private void checkSuperExpr(SuperExpr expr) {
        if (currentClass == null) {
            error(expr.getKeyword(), "Cannot use 'super' outside of a class");
            return;
        }
        setType(expr, Type.OBJECT); // TODO: Use actual superclass type
    }

    private void checkGroupingExpr(GroupingExpr expr) {
        check(expr.getExpression());
        setType(expr, getType(expr.getExpression()));
    }

    private void checkIfStmt(IfStmt stmt) {
        check(stmt.getCondition());
        Type conditionType = getType(stmt.getCondition());
        
        if (conditionType != Type.BOOLEAN) {
            error(stmt.getCondition().getToken(), "Condition must be a boolean");
        }
        
        check(stmt.getThenBranch());
        if (stmt.getElseBranch() != null) {
            check(stmt.getElseBranch());
        }
    }

    private void checkWhileStmt(WhileStmt stmt) {
        check(stmt.getCondition());
        Type conditionType = getType(stmt.getCondition());
        
        if (conditionType != Type.BOOLEAN) {
            error(stmt.getCondition().getToken(), "Condition must be a boolean");
        }
        
        check(stmt.getBody());
    }

    private void checkReturnStmt(ReturnStmt stmt) {
        if (stmt.getValue() != null) {
            check(stmt.getValue());
            Type returnType = getType(stmt.getValue());
            
            if (returnType != currentReturnType) {
                error(stmt.getToken(), "Cannot return " + returnType + " from function returning " + currentReturnType);
            }
        } else if (currentReturnType != Type.VOID) {
            error(stmt.getToken(), "Must return a value from function returning " + currentReturnType);
        }
    }

    private void enterScope() {
        scopes.push(new HashMap<>());
    }

    private void exitScope() {
        scopes.pop();
    }

    private void defineVariable(Token name, Type type) {
        if (scopes.isEmpty()) {
            error(name, "No active scope");
            return;
        }
        scopes.peek().put(name.getLexeme(), type);
    }

    private Type resolveVariable(Token name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Type type = scopes.get(i).get(name.getLexeme());
            if (type != null) {
                return type;
            }
        }
        return null;
    }

    private void setType(AST node, Type type) {
        nodeTypes.put(node, type);
    }

    private Type getType(AST node) {
        return nodeTypes.getOrDefault(node, Type.ANY);
    }

    private void error(Token token, String message) {
        errorHandler.handleError(new SemanticError(token, message));
    }

    private ClassNode currentClass = null;
} 