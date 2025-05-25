package com.velox.compiler.codegen;

import com.velox.compiler.ast.*;
import com.velox.compiler.ast.expressions.*;
import com.velox.compiler.ast.statements.*;
import com.velox.compiler.bytecode.Bytecode;
import com.velox.compiler.bytecode.OpCode;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates bytecode from an AST.
 */
public class CodeGenerator implements StmtVisitor {
    private final Bytecode bytecode;
    private final Map<String, Integer> functionOffsets;
    private final Map<String, Integer> localVariables;
    private boolean debugInfoEnabled;

    public CodeGenerator() {
        this.bytecode = new Bytecode();
        this.functionOffsets = new HashMap<>();
        this.localVariables = new HashMap<>();
        this.debugInfoEnabled = false;
    }

    public Bytecode getBytecode() {
        return bytecode;
    }

    public void setDebugInfoEnabled(boolean enabled) {
        this.debugInfoEnabled = enabled;
    }

    public Bytecode generate(ModuleNode node) {
        node.accept(this);
        return bytecode;
    }

    @Override
    public Object visitModuleNode(ModuleNode node) {
        for (ImportNode import_ : node.getImports()) {
            import_.accept(this);
        }
        for (AST declaration : node.getDeclarations()) {
            declaration.accept(this);
        }
        return null;
    }

    @Override
    public Object visitModule(ModuleNode node) {
        return visitModuleNode(node);
    }

    @Override
    public Object visitClass(ClassNode node) {
        String name = node.getName();
        
        // Emit class definition
        bytecode.emit(OpCode.CONSTANT, name);
        
        // Generate fields
        for (FieldNode field : node.getFields()) {
            field.accept(this);
        }
        
        // Generate constructor
        if (node.getConstructor() != null) {
            node.getConstructor().accept(this);
        }
        
        // Generate methods
        for (MethodNode method : node.getMethods()) {
            method.accept(this);
        }
        
        return null;
    }

    @Override
    public Object visitFunction(FunctionNode node) {
        functionOffsets.put(node.getName(), bytecode.getCurrentOffset());
        for (AST param : node.getParameters()) {
            param.accept(this);
        }
        node.getBody().accept(this);
        bytecode.emit(OpCode.RETURN);
        return null;
    }

    @Override
    public Object visitMethod(MethodNode node) {
        String name = node.getName().getLexeme();
        functionOffsets.put(name, bytecode.getCurrentOffset());
        
        // Save current locals
        Map<String, Integer> previousLocals = new HashMap<>(localVariables);
        localVariables.clear();
        
        // Add 'this' as first local
        localVariables.put("this", 0);
        
        // Add parameters to locals
        for (int i = 0; i < node.getParameters().size(); i++) {
            AST param = node.getParameters().get(i);
            if (param instanceof ParameterNode) {
                ParameterNode paramNode = (ParameterNode) param;
                localVariables.put(paramNode.getName(), i + 1);
            }
        }
        
        // Generate method body
        node.getBody().accept(this);
        
        // Add implicit return if no explicit return
        if (!(node.getBody() instanceof ReturnStmt)) {
            bytecode.emit(OpCode.NIL);
            bytecode.emit(OpCode.RETURN);
        }
        
        // Restore previous locals
        localVariables.clear();
        localVariables.putAll(previousLocals);
        
        return null;
    }

    @Override
    public Object visitField(FieldNode node) {
        String name = node.getName().getLexeme();
        
        // Emit field definition
        bytecode.emit(OpCode.CONSTANT, name);
        
        // Generate initializer if present
        if (node.getInitializer() != null) {
            node.getInitializer().accept(this);
        } else {
            bytecode.emit(OpCode.NIL);
        }
        
        // Set field value
        bytecode.emit(OpCode.SET_PROPERTY, name);
        
        return null;
    }

    @Override
    public Object visitConstructor(ConstructorNode node) {
        // Save current locals
        Map<String, Integer> previousLocals = new HashMap<>(localVariables);
        localVariables.clear();
        
        // Add 'this' as first local
        localVariables.put("this", 0);
        
        // Add parameters to locals
        for (int i = 0; i < node.getParameters().size(); i++) {
            AST param = node.getParameters().get(i);
            if (param instanceof ParameterNode) {
                ParameterNode paramNode = (ParameterNode) param;
                localVariables.put(paramNode.getName(), i + 1);
            }
        }
        
        // Generate constructor body
        for (AST statement : node.getBody()) {
            statement.accept(this);
        }
        
        // Add implicit return if no explicit return
        if (node.getBody().isEmpty() || !(node.getBody().get(node.getBody().size() - 1) instanceof ReturnStmt)) {
            bytecode.emit(OpCode.GET_LOCAL, 0); // Return 'this'
            bytecode.emit(OpCode.RETURN);
        }
        
        // Restore previous locals
        localVariables.clear();
        localVariables.putAll(previousLocals);
        
        return null;
    }

    @Override
    public Object visitImport(ImportNode node) {
        String moduleName = node.getModuleName().getLexeme();
        String alias = node.getAlias() != null ? node.getAlias().getLexeme() : moduleName;
        
        // Emit import instruction with module name and optional alias
        bytecode.emit(OpCode.CONSTANT, moduleName);
        if (node.getAlias() != null) {
            bytecode.emit(OpCode.CONSTANT, alias);
        }
        bytecode.emit(OpCode.IMPORT);
        return null;
    }

    @Override
    public Object visitParameter(Parameter param) {
        // Parameters are handled in function/method/constructor contexts
        return null;
    }

    @Override
    public Object visitParameter(ParameterNode param) {
        // Parameters are handled in function/method/constructor contexts
        return null;
    }

    @Override
    public Object visitLiteralExpr(LiteralExpr node) {
        Object value = node.getValue();
        if (value instanceof Boolean) {
            bytecode.emit((Boolean)value ? OpCode.TRUE : OpCode.FALSE);
        } else if (value instanceof Number) {
            bytecode.emit(OpCode.CONSTANT, value);
        } else if (value instanceof String) {
            bytecode.emit(OpCode.CONSTANT, value);
        } else if (value == null) {
            bytecode.emit(OpCode.NIL);
        }
        return null;
    }

    @Override
    public Object visitVariableExpr(VariableExpr node) {
        String name = node.getToken().getLexeme();
        Integer slot = localVariables.get(name);
        if (slot != null) {
            bytecode.emit(OpCode.GET_LOCAL, slot);
        } else {
            bytecode.emit(OpCode.GET_GLOBAL, name);
        }
        return null;
    }

    @Override
    public Object visitAssignExpr(AssignExpr node) {
        String name = node.getToken().getLexeme();
        node.getValue().accept(this);
        
        Integer slot = localVariables.get(name);
        if (slot != null) {
            bytecode.emit(OpCode.SET_LOCAL, slot);
        } else {
            bytecode.emit(OpCode.SET_GLOBAL, name);
        }
        return null;
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);

        switch (node.getOperator().getType()) {
            case PLUS: bytecode.emit(OpCode.ADD); break;
            case MINUS: bytecode.emit(OpCode.SUBTRACT); break;
            case STAR: bytecode.emit(OpCode.MULTIPLY); break;
            case SLASH: bytecode.emit(OpCode.DIVIDE); break;
            case EQUAL_EQUAL: bytecode.emit(OpCode.EQUAL); break;
            case BANG_EQUAL: bytecode.emit(OpCode.NOT_EQUAL); break;
            case LESS: bytecode.emit(OpCode.LESS); break;
            case LESS_EQUAL: bytecode.emit(OpCode.LESS_EQUAL); break;
            case GREATER: bytecode.emit(OpCode.GREATER); break;
            case GREATER_EQUAL: bytecode.emit(OpCode.GREATER_EQUAL); break;
            default:
                throw new RuntimeException("Invalid binary operator: " + node.getOperator().getType());
        }
        return null;
    }

    @Override
    public Object visitUnaryExpr(UnaryExpr node) {
        node.getRight().accept(this);

        switch (node.getOperator().getType()) {
            case MINUS: bytecode.emit(OpCode.NEGATE); break;
            case BANG: bytecode.emit(OpCode.NOT); break;
            default:
                throw new RuntimeException("Invalid unary operator: " + node.getOperator().getType());
        }
        return null;
    }

    @Override
    public Object visitCallExpr(CallExpr node) {
        node.getCallee().accept(this);
        
        for (AST argument : node.getArguments()) {
            argument.accept(this);
        }
        
        bytecode.emit(OpCode.CALL, node.getArguments().size());
        return null;
    }

    @Override
    public Object visitGetExpr(GetExpr node) {
        node.getObject().accept(this);
        bytecode.emit(OpCode.GET_PROPERTY, node.getToken().getLexeme());
        return null;
    }

    @Override
    public Object visitSetExpr(SetExpr node) {
        node.getObject().accept(this);
        node.getValue().accept(this);
        bytecode.emit(OpCode.SET_PROPERTY, node.getName().getLexeme());
        return null;
    }

    @Override
    public Object visitGroupingExpr(GroupingExpr node) {
        node.getExpression().accept(this);
        return null;
    }

    @Override
    public Object visitThisExpr(ThisExpr node) {
        bytecode.emit(OpCode.GET_LOCAL, 0); // 'this' is always in slot 0
        return null;
    }

    @Override
    public Object visitSuperExpr(SuperExpr node) {
        bytecode.emit(OpCode.GET_LOCAL, 0); // 'this' is always in slot 0
        bytecode.emit(OpCode.GET_PROPERTY, node.getMethod().getLexeme());
        return null;
    }

    @Override
    public Object visitIfStmt(IfStmt node) {
        node.getCondition().accept(this);
        
        int elseJump = bytecode.emitJump(OpCode.JUMP_IF_FALSE);
        bytecode.emit(OpCode.POP); // Pop the condition
        
        node.getThenBranch().accept(this);
        
        int endJump = bytecode.emitJump(OpCode.JUMP);
        
        bytecode.patchJump(elseJump);
        bytecode.emit(OpCode.POP); // Pop the condition
        
        if (node.getElseBranch() != null) {
            node.getElseBranch().accept(this);
        }
        
        bytecode.patchJump(endJump);
        return null;
    }

    @Override
    public Object visitWhileStmt(WhileStmt node) {
        int loopStart = bytecode.getCurrentOffset();
        
        node.getCondition().accept(this);
        int exitJump = bytecode.emitJump(OpCode.JUMP_IF_FALSE);
        
        bytecode.emit(OpCode.POP); // Pop the condition
        node.getBody().accept(this);
        
        bytecode.emitLoop(loopStart);
        
        bytecode.patchJump(exitJump);
        bytecode.emit(OpCode.POP); // Pop the condition
        return null;
    }

    @Override
    public Object visitReturnStmt(ReturnStmt node) {
        if (node.getValue() != null) {
            node.getValue().accept(this);
        } else {
            bytecode.emit(OpCode.NIL);
        }
        bytecode.emit(OpCode.RETURN);
        return null;
    }

    @Override
    public Object visitBlockStmt(BlockStmt node) {
        Map<String, Integer> previousLocals = new HashMap<>(localVariables);
        for (AST statement : node.getStatements()) {
            statement.accept(this);
        }
        localVariables.clear();
        localVariables.putAll(previousLocals);
        return null;
    }

    @Override
    public Object visitExpressionStmt(ExpressionStmt stmt) {
        stmt.getExpression().accept(this);
        bytecode.emit(OpCode.POP); // Pop the expression result since it's a statement
        return null;
    }

    @Override
    public Object visitVarStmt(VarStmt stmt) {
        String name = stmt.getName().getLexeme();
        int slot = localVariables.size();
        localVariables.put(name, slot);

        if (stmt.getInitializer() != null) {
            stmt.getInitializer().accept(this);
        } else {
            bytecode.emit(OpCode.NIL);
        }
        bytecode.emit(OpCode.SET_LOCAL, slot);
        return null;
    }

    @Override
    public Object visitPrintStmt(PrintStmt stmt) {
        stmt.getExpression().accept(this);
        bytecode.emit(OpCode.PRINT);
        return null;
    }

    @Override
    public Object visitFunctionStmt(FunctionStmt stmt) {
        String name = stmt.getName().getLexeme();
        functionOffsets.put(name, bytecode.getCurrentOffset());
        
        // Save current locals
        Map<String, Integer> previousLocals = new HashMap<>(localVariables);
        localVariables.clear();
        
        // Add parameters to locals
        for (int i = 0; i < stmt.getParameters().size(); i++) {
            Parameter param = stmt.getParameters().get(i);
            localVariables.put(param.getName().getLexeme(), i);
        }
        
        // Generate function body
        for (AST statement : stmt.getBody()) {
            statement.accept(this);
        }
        
        // If no return statement, add implicit return
        if (stmt.getBody().isEmpty() || !(stmt.getBody().get(stmt.getBody().size() - 1) instanceof ReturnStmt)) {
            bytecode.emit(OpCode.NIL);
            bytecode.emit(OpCode.RETURN);
        }
        
        // Restore previous locals
        localVariables.clear();
        localVariables.putAll(previousLocals);
        
        return null;
    }

    @Override
    public Object visitClassStmt(ClassStmt stmt) {
        String name = stmt.getName().getLexeme();
        
        // Emit class definition
        bytecode.emit(OpCode.CONSTANT, name);
        
        // Handle inheritance if present
        if (stmt.getSuperclass() != null) {
            stmt.getSuperclass().accept(this);
            bytecode.emit(OpCode.CONSTANT, "super");
        }
        
        // Generate method code
        for (FunctionStmt method : stmt.getMethods()) {
            method.accept(this);
        }
        
        return null;
    }

    @Override
    public Object visitTypeAnnotation(TypeAnnotation type) {
        // Store type information in the constant pool for runtime type checking
        String typeName = type.getTypeName();
        bytecode.emit(OpCode.CONSTANT, typeName);
        
        // Emit type info instruction if debug info is enabled
        if (debugInfoEnabled) {
            bytecode.emit(OpCode.TYPE_INFO);
        }
        return null;
    }
} 