package com.velox.compiler.ast.statements;

import com.velox.compiler.ast.AST;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.ast.Stmt;
import com.velox.compiler.ast.StmtVisitor;
import com.velox.compiler.token.Token;
import java.util.List;

/**
 * Represents a block statement in the AST.
 */
public class BlockStmt extends Stmt {
    private final List<AST> statements;

    public BlockStmt(Token token, List<AST> statements) {
        super(token);
        this.statements = statements;
    }

    public List<AST> getStatements() {
        return statements;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitBlockStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    @Override
    public Object accept(StmtVisitor visitor) {
        return visitor.visitBlockStmt(this);
    }
} 