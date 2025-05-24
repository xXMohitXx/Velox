package com.velox.compiler.ast;

import com.velox.compiler.token.Token;
import java.util.ArrayList;
import java.util.List;

public class ClassNode implements AST {
    private final String name;
    private final List<FieldNode> fields;
    private final List<MethodNode> methods;
    private ConstructorNode constructor;
    private final boolean isPublic;

    public ClassNode(Token token, String name, boolean isPublic) {
        this.name = name;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.constructor = null;
        this.isPublic = isPublic;
    }

    public String getName() {
        return name;
    }

    public List<FieldNode> getFields() {
        return fields;
    }

    public List<MethodNode> getMethods() {
        return methods;
    }

    public ConstructorNode getConstructor() {
        return constructor;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void addField(FieldNode field) {
        fields.add(field);
    }

    public void addMethod(MethodNode method) {
        methods.add(method);
    }

    public void setConstructor(ConstructorNode constructor) {
        this.constructor = constructor;
    }

    @Override
    public Object accept(ASTVisitor visitor) {
        return visitor.visitClass(this);
    }
} 