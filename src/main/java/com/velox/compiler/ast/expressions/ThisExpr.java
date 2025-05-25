package com.velox.compiler.ast.expressions;


import com.velox.compiler.ast.ASTNode;
import com.velox.compiler.ast.ASTVisitor;
import com.velox.compiler.token.Token;

/**
 * This expression node
 */
public class ThisExpr extends ASTNode {
    private final Token keyword;

    public ThisExpr(Token keyword) {
        super(keyword);
        this.keyword = keyword;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitThisExpr(this);
    }

    public Token getKeyword() { return keyword; }
} 