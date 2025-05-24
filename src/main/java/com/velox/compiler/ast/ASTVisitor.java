package com.velox.compiler.ast;

/**
 * Visitor interface for AST nodes
 */
public interface ASTVisitor<R> {
    R visitBinaryExpr(BinaryExpr expr);
    R visitUnaryExpr(UnaryExpr expr);
    R visitLiteralExpr(LiteralExpr expr);
    R visitVariableExpr(VariableExpr expr);
    R visitAssignExpr(AssignExpr expr);
    R visitCallExpr(CallExpr expr);
    R visitGetExpr(GetExpr expr);
    R visitSetExpr(SetExpr expr);
    R visitThisExpr(ThisExpr expr);
    R visitSuperExpr(SuperExpr expr);
    R visitGroupingExpr(GroupingExpr expr);
    R visitClass(ClassNode node);
    R visitConstructor(ConstructorNode node);
    R visitMethod(MethodNode node);
    R visitField(FieldNode node);
    R visitIfStmt(IfStmt stmt);
    R visitImport(ImportNode node);
    R visitModule(ModuleNode node);
} 