package com.velox.compiler.ast;

public interface StmtVisitor<R> extends ASTVisitor<R> {
    R visitExpressionStmt(ExpressionStmt stmt);
    R visitPrintStmt(PrintStmt stmt);
    R visitVarStmt(VarStmt stmt);
    R visitBlockStmt(BlockStmt stmt);
    R visitIfStmt(IfStmt stmt);
    R visitWhileStmt(WhileStmt stmt);
    R visitFunctionStmt(FunctionStmt stmt);
    R visitReturnStmt(ReturnStmt stmt);
    R visitClassStmt(ClassStmt stmt);
    R visitModule(ModuleNode node);
    R visitImport(ImportNode node);
    R visitTypeAnnotation(TypeAnnotation type);
} 