package com.velox.compiler.ast;

import com.velox.compiler.lexer.Token;
import java.util.List;

/**
 * Represents a method declaration in the AST.
 */
public class MethodNode implements AST {
    private final Token name;
    private final List<AST> parameters;
    private final AST returnType;
    private final AST body;
    private final Token accessModifier;

    public MethodNode(Token name, List<AST> parameters, AST returnType, AST body, Token accessModifier) {
        this.name = name;
        this.parameters = parameters;
        this.returnType = returnType;
        this.body = body;
        this.accessModifier = accessModifier;
    }

    public Token getName() {
        return name;
    }

    public List<AST> getParameters() {
        return parameters;
    }

    public AST getReturnType() {
        return returnType;
    }

    public AST getBody() {
        return body;
    }

    public Token getAccessModifier() {
        return accessModifier;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitMethod(this);
    }
} 