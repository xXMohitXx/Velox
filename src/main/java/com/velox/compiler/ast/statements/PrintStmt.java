package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.ast.StmtVisitor;
import com.velox.compiler.token.Token;

/**
 * Represents a print statement in the AST.
 */
public class PrintStmt extends ASTNode {
    private final AST expression;

    public PrintStmt(Token token, AST expression) {
        super(token);
        this.expression = expression;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitPrintStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    public AST getExpression() {
        return expression;
    }
} 