package com.velox.compiler.ast;

import com.velox.compiler.ast.statements.*;

public interface StmtVisitor extends ASTVisitor {
    Object visitBlockStmt(BlockStmt stmt);
    Object visitClassStmt(ClassStmt stmt);
    Object visitExpressionStmt(ExpressionStmt stmt);
    Object visitFunctionStmt(FunctionStmt stmt);
    Object visitIfStmt(IfStmt stmt);
    Object visitModuleNode(ModuleNode node);
    Object visitParameter(Parameter param);
    Object visitPrintStmt(com.velox.compiler.ast.statements.PrintStmt stmt);
    Object visitReturnStmt(ReturnStmt stmt);
    Object visitVarStmt(VarStmt stmt);
    Object visitWhileStmt(WhileStmt stmt);
    Object visitModule(ModuleNode node);
    Object visitImport(ImportNode node);
    Object visitTypeAnnotation(TypeAnnotation type);
} 