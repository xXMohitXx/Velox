package com.velox.compiler.ast;

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
} 