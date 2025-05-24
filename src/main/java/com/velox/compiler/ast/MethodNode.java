package com.velox.compiler.ast;

import com.velox.compiler.token.Token;
import java.util.List;

public class MethodNode extends AST {
    private final String name;
    private final List<AST> parameters;
    private final List<AST> body;
    private final boolean isPublic;

    public MethodNode(Token token, String name, List<AST> parameters, List<AST> body, boolean isPublic) {
        super(token);
        this.name = name;
        this.parameters = parameters;
        this.body = body;
        this.isPublic = isPublic;
    }

    @Override
    public <R> R accept(ASTVisitor<R> visitor) {
        return visitor.visitMethod(this);
    }

    public String getName() { return name; }
    public List<AST> getParameters() { return parameters; }
    public List<AST> getBody() { return body; }
    public boolean isPublic() { return isPublic; }
} 