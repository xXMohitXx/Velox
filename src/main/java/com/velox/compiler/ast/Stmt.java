package com.velox.compiler.ast;

/**
 * Base class for all statement nodes
 */
public abstract class Stmt {
    public abstract <R> R accept(StmtVisitor<R> visitor);
} 