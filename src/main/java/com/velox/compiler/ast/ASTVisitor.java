package com.velox.compiler.ast;

import com.velox.compiler.ast.expressions.*;

/**
 * Visitor interface for traversing the AST.
 */
public interface ASTVisitor {
    Object visitModule(ModuleNode node);
    Object visitClass(ClassNode node);
    Object visitFunction(FunctionNode node);
    Object visitMethod(MethodNode node);
    Object visitField(FieldNode node);
    Object visitConstructor(ConstructorNode node);
    Object visitImport(ImportNode node);
    Object visitParameter(ParameterNode node);
    
    // Expression nodes
    Object visitBinaryExpr(BinaryExpr expr);
    Object visitUnaryExpr(UnaryExpr expr);
    Object visitLiteralExpr(LiteralExpr expr);
    Object visitVariableExpr(VariableExpr expr);
    Object visitAssignExpr(AssignExpr expr);
    Object visitCallExpr(CallExpr expr);
    Object visitGetExpr(GetExpr expr);
    Object visitSetExpr(SetExpr expr);
    Object visitGroupingExpr(GroupingExpr expr);
    Object visitThisExpr(ThisExpr expr);
    Object visitSuperExpr(SuperExpr expr);
    
    // Statement nodes
    Object visitIfStmt(com.velox.compiler.ast.statements.IfStmt node);
    Object visitWhileStmt(com.velox.compiler.ast.statements.WhileStmt node);
    Object visitReturnStmt(com.velox.compiler.ast.statements.ReturnStmt node);
    Object visitBlockStmt(com.velox.compiler.ast.statements.BlockStmt node);
} 