package com.velox.compiler.ast.expressions;


import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * Super expression node
 */
public class SuperExpr extends ASTNode {
    private final Token keyword;
    private final Token method;

    public SuperExpr(Token keyword, Token method) {
        super(keyword);
        this.keyword = keyword;
        this.method = method;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitSuperExpr(this);
    }

    public Token getKeyword() { return keyword; }
    public Token getMethod() { return method; }
} 