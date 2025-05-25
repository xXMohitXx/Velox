package com.velox.compiler.ast;

import com.velox.compiler.token.Token;
import java.util.List;

public class ConstructorNode extends ASTNode {
    private final List<AST> parameters;
    private final List<AST> body;

    public ConstructorNode(Token token, List<AST> parameters, List<AST> body) {
        super(token);
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitConstructor(this);
    }

    public List<AST> getParameters() { return parameters; }
    public List<AST> getBody() { return body; }
} 