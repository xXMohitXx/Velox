package com.velox.compiler.ast;

import com.velox.compiler.token.Token;
import com.velox.compiler.ast.expressions.VariableExpr;
import java.util.List;

public class ClassStmt extends Stmt {
    private final Token name;
    private final VariableExpr superclass;
    private final List<FunctionStmt> methods;

    public ClassStmt(Token name, VariableExpr superclass, List<FunctionStmt> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }

    @Override
    public Object accept(StmtVisitor visitor) {
        return visitor.visitClassStmt(this);
    }

    public Token getName() { return name; }
    public VariableExpr getSuperclass() { return superclass; }
    public List<FunctionStmt> getMethods() { return methods; }
} 