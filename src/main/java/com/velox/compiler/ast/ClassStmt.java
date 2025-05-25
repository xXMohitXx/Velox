package com.velox.compiler.ast;

import com.velox.compiler.token.Token;
import com.velox.compiler.ast.expressions.VariableExpr;
import java.util.List;

public class ClassStmt extends ASTNode {
    private final Token name;
    private final VariableExpr superclass;
    private final List<FunctionStmt> methods;

    public ClassStmt(Token token, Token name, VariableExpr superclass, List<FunctionStmt> methods) {
        super(token);
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitClassStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    public Token getName() { return name; }
    public VariableExpr getSuperclass() { return superclass; }
    public List<FunctionStmt> getMethods() { return methods; }
} 