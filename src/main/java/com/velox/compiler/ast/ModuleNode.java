package com.velox.compiler.ast;

import com.velox.compiler.token.Token;
import java.util.List;
import java.util.ArrayList;

public class ModuleNode extends ASTNode {
    private final String name;
    private final List<AST> statements;
    private final List<ImportNode> imports;
    private final List<AST> declarations;

    public ModuleNode(Token token, String name) {
        super(token);
        this.name = name;
        this.statements = new ArrayList<>();
        this.imports = new ArrayList<>();
        this.declarations = new ArrayList<>();
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitModuleNode(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    public String getName() { return name; }
    public List<AST> getStatements() { return statements; }
    public List<ImportNode> getImports() { return imports; }
    public List<AST> getDeclarations() { return declarations; }

    public void addStatement(AST stmt) { statements.add(stmt); }
    public void addImport(ImportNode imp) { imports.add(imp); }
    public void addDeclaration(AST decl) { declarations.add(decl); }
} 