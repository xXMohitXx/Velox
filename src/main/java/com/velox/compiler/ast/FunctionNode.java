package com.velox.compiler.ast;

import java.util.List;
import java.util.ArrayList;
import com.velox.compiler.ast.statements.BlockStmt;
import com.velox.compiler.token.Token;

public class FunctionNode extends ASTNode {
    private final String name;
    private final List<ParameterNode> parameters;
    private final String returnType;
    private final BlockStmt body;

    public FunctionNode(Token token, String name, String returnType, BlockStmt body) {
        super(token);
        this.name = name;
        this.parameters = new ArrayList<>();
        this.returnType = returnType;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public List<ParameterNode> getParameters() {
        return parameters;
    }

    public String getReturnType() {
        return returnType;
    }

    public BlockStmt getBody() {
        return body;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitFunction(this);
    }
} 