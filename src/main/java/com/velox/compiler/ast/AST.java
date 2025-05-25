package com.velox.compiler.ast;

import com.velox.compiler.token.Token;

/**
 * Base interface for all AST nodes.
 */
public interface AST {
    /**
     * Accepts a visitor for traversing the AST.
     * @param visitor The visitor to accept
     * @return The result of the visitor's visit
     */
    Object accept(ASTVisitor visitor);

    /**
     * Gets the token associated with this node.
     * @return The token, or null if not applicable
     */
    Token getToken();

    /**
     * Gets the starting line number of this node.
     * @return The line number
     */
    int getStartLine();

    /**
     * Gets the starting column number of this node.
     * @return The column number
     */
    int getStartColumn();

    /**
     * Gets the ending line number of this node.
     * @return The line number
     */
    int getEndLine();

    /**
     * Gets the ending column number of this node.
     * @return The column number
     */
    int getEndColumn();
} 