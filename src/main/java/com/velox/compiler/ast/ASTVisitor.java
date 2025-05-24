package com.velox.compiler.ast;

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
    Object visitBinaryExpr(com.velox.compiler.ast.expressions.BinaryExpr node);
    Object visitUnaryExpr(com.velox.compiler.ast.expressions.UnaryExpr node);
    Object visitLiteralExpr(com.velox.compiler.ast.expressions.LiteralExpr node);
    Object visitVariableExpr(com.velox.compiler.ast.expressions.VariableExpr node);
    Object visitAssignExpr(com.velox.compiler.ast.expressions.AssignExpr node);
    Object visitCallExpr(com.velox.compiler.ast.expressions.CallExpr node);
    Object visitGetExpr(com.velox.compiler.ast.expressions.GetExpr node);
    Object visitSetExpr(com.velox.compiler.ast.expressions.SetExpr node);
    Object visitGroupingExpr(com.velox.compiler.ast.expressions.GroupingExpr node);
    Object visitThisExpr(com.velox.compiler.ast.expressions.ThisExpr node);
    Object visitSuperExpr(com.velox.compiler.ast.expressions.SuperExpr node);
    
    // Statement nodes
    Object visitIfStmt(com.velox.compiler.ast.statements.IfStmt node);
    Object visitWhileStmt(com.velox.compiler.ast.statements.WhileStmt node);
    Object visitReturnStmt(com.velox.compiler.ast.statements.ReturnStmt node);
    Object visitBlockStmt(com.velox.compiler.ast.statements.BlockStmt node);
} 