package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.ast.Stmt;
import com.velox.compiler.ast.StmtVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents an expression statement in the AST.
 */
public class ExpressionStmt extends Stmt {
    private final AST expression;

    public ExpressionStmt(Token token, AST expression) {
        super(token);
        this.expression = expression;
    }

    public AST getExpression() {
        return expression;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitExpressionStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    @Override
    public Object accept(StmtVisitor visitor) {
        return visitor.visitExpressionStmt(this);
    }
} 