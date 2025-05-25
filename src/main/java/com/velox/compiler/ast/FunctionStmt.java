package com.velox.compiler.ast;

import com.velox.compiler.token.Token;
import java.util.List;


public class FunctionStmt extends ASTNode {
    private final Token name;
    private final List<Parameter> parameters;
    private final List<AST> body;
    private final TypeAnnotation returnType;

    public FunctionStmt(Token token, Token name, List<Parameter> parameters, List<AST> body, TypeAnnotation returnType) {
        super(token);
        this.name = name;
        this.parameters = parameters;
        this.body = body;
        this.returnType = returnType;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        if (visitor instanceof StmtVisitor) {
            return ((StmtVisitor) visitor).visitFunctionStmt(this);
        }
        throw new UnsupportedOperationException("Visitor must implement StmtVisitor");
    }

    public Token getName() { return name; }
    public List<Parameter> getParameters() { return parameters; }
    public List<AST> getBody() { return body; }
    public TypeAnnotation getReturnType() { return returnType; }
} 