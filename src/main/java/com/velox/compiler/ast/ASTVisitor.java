package com.velox.compiler.ast;

public interface ASTVisitor<R> {
    R visitModule(ModuleNode node);
    R visitFunction(FunctionNode node);
    R visitClass(ClassNode node);
    R visitBlock(BlockNode node);
    R visitVariableDeclaration(VariableDeclarationNode node);
    R visitAssignment(AssignmentNode node);
    R visitBinaryExpression(BinaryExpressionNode node);
    R visitUnaryExpression(UnaryExpressionNode node);
    R visitLiteral(LiteralNode node);
    R visitIdentifier(IdentifierNode node);
    R visitCall(CallNode node);
    R visitIf(IfNode node);
    R visitWhile(WhileNode node);
    R visitFor(ForNode node);
    R visitReturn(ReturnNode node);
    R visitBreak(BreakNode node);
    R visitContinue(ContinueNode node);
    R visitTry(TryNode node);
    R visitThrow(ThrowNode node);
    R visitTypeAnnotation(TypeAnnotationNode node);
    R visitParameter(ParameterNode node);
    R visitField(FieldNode node);
    R visitMethod(MethodNode node);
    R visitConstructor(ConstructorNode node);
    R visitImport(ImportNode node);
    R visitExport(ExportNode node);
} 