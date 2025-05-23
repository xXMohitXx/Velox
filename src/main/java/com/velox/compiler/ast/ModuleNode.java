package com.velox.compiler.ast;

import com.velox.compiler.token.Token;
import java.util.ArrayList;
import java.util.List;

public class ModuleNode extends ASTNode {
    private final String name;
    private final List<ImportNode> imports;
    private final List<ASTNode> declarations;

    public ModuleNode(Token token, String name) {
        super(token);
        this.name = name;
        this.imports = new ArrayList<>();
        this.declarations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ImportNode> getImports() {
        return imports;
    }

    public List<ASTNode> getDeclarations() {
        return declarations;
    }

    public void addImport(ImportNode importNode) {
        imports.add(importNode);
    }

    public void addDeclaration(ASTNode declaration) {
        declarations.add(declaration);
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitModule(this);
    }
} 