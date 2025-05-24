package com.velox.compiler.ast;

import com.velox.compiler.ast.statements.IfStmt;
import com.velox.compiler.ast.statements.ReturnStmt;

public interface StmtVisitor extends ASTVisitor {
    Object visitExpressionStmt(ExpressionStmt stmt);
    Object visitPrintStmt(PrintStmt stmt);
    Object visitVarStmt(VarStmt stmt);
    Object visitBlockStmt(com.velox.compiler.ast.statements.BlockStmt stmt);
    Object visitIfStmt(IfStmt stmt);
    Object visitWhileStmt(com.velox.compiler.ast.statements.WhileStmt stmt);
    Object visitFunctionStmt(FunctionStmt stmt);
    Object visitReturnStmt(ReturnStmt stmt);
    Object visitClassStmt(ClassStmt stmt);
    Object visitModule(ModuleNode node);
    Object visitImport(ImportNode node);
    Object visitTypeAnnotation(TypeAnnotation type);
} 