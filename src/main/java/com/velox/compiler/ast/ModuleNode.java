package com.velox.compiler.ast;

import java.util.List;
import java.util.ArrayList;

public class ModuleNode implements AST {
    private final List<ImportNode> imports;
    private final List<AST> declarations;

    public ModuleNode() {
        this.imports = new ArrayList<>();
        this.declarations = new ArrayList<>();
    }

    public List<ImportNode> getImports() {
        return imports;
    }

    public List<AST> getDeclarations() {
        return declarations;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitModule(this);
    }
} 