package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import java.util.List;

/**
 * Represents a block statement in the AST.
 */
public class BlockStmt implements AST {
    private final List<AST> statements;

    public BlockStmt(List<AST> statements) {
        this.statements = statements;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitBlockStmt(this);
    }

    public List<AST> getStatements() {
        return statements;
    }
} 