package com.velox.compiler.bytecode;

import java.util.*;
import java.io.PrintStream;
import com.velox.compiler.error.RuntimeError;

public class VirtualMachine {
    private final Bytecode bytecode;
    private final Stack<Object> stack;
    private final Stack<CallFrame> callStack;
    private int programCounter;
    private boolean running;
    private boolean debugMode;
    private PrintStream debugOutput;
    private Map<String, Object> globals;
    private List<RuntimeError> errors;

    public VirtualMachine(Bytecode bytecode) {
        this.bytecode = bytecode;
        this.stack = new Stack<>();
        this.callStack = new Stack<>();
        this.programCounter = 0;
        this.running = false;
        this.debugMode = false;
        this.debugOutput = System.out;
        this.globals = new HashMap<>();
        this.errors = new ArrayList<>();
    }

    public void execute() {
        running = true;
        programCounter = 0;
        errors.clear();

        while (running && programCounter < bytecode.getInstructionCount()) {
            if (debugMode) {
                debugOutput.println("PC: " + programCounter);
                debugOutput.println("Stack: " + stack);
                debugOutput.println("Call Stack: " + callStack.size());
            }

            try {
                Instruction instruction = bytecode.getInstruction(programCounter++);
                instruction.execute(this);
            } catch (RuntimeException e) {
                handleError(new RuntimeError(e.getMessage(), programCounter - 1));
                if (!debugMode) {
                    throw e;
                }
            }
        }
    }

    public void callFunction(String name, Object[] args) {
        Integer offset = bytecode.getFunctionOffset(name);
        if (offset == null) {
            throw new RuntimeException("Function not found: " + name);
        }

        // Save current state
        callStack.push(new CallFrame(programCounter, args.length, stack.size() - args.length));

        // Set up arguments as locals
        for (int i = 0; i < args.length; i++) {
            setLocal(i, args[i]);
        }

        // Jump to function
        programCounter = offset;
    }

    public void returnFromFunction(Object result) {
        if (callStack.isEmpty()) {
            throw new RuntimeException("No active function call");
        }

        CallFrame frame = callStack.pop();
        programCounter = frame.getReturnAddress();
        
        // Clear stack up to base pointer
        while (stack.size() > frame.getBasePointer()) {
            stack.pop();
        }
        
        // Push result
        if (result != null) {
            stack.push(result);
        }
    }

    public void push(Object value) {
        stack.push(value);
    }

    public Object pop() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Stack underflow");
        }
        return stack.pop();
    }

    public Object peek() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Stack underflow");
        }
        return stack.peek();
    }

    public void setLocal(int index, Object value) {
        if (callStack.isEmpty()) {
            throw new RuntimeException("No active function call");
        }
        callStack.peek().setLocal(index, value);
    }

    public Object getLocal(int index) {
        if (callStack.isEmpty()) {
            throw new RuntimeException("No active function call");
        }
        return callStack.peek().getLocal(index);
    }

    public void setGlobal(String name, Object value) {
        globals.put(name, value);
    }

    public Object getGlobal(String name) {
        return globals.get(name);
    }

    public Constant getConstant(int index) {
        return bytecode.getConstant(index);
    }

    public void jump(int offset) {
        if (offset < 0 || offset >= bytecode.getInstructionCount()) {
            throw new RuntimeException("Invalid jump offset: " + offset);
        }
        programCounter = offset;
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public Stack<Object> getStack() {
        return stack;
    }

    public Stack<CallFrame> getCallStack() {
        return callStack;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void setDebugOutput(PrintStream debugOutput) {
        this.debugOutput = debugOutput;
    }

    private void handleError(RuntimeError error) {
        errors.add(error);
        if (debugMode) {
            debugOutput.println("Error: " + error.getMessage());
            debugOutput.println("At instruction: " + error.getInstructionIndex());
            debugOutput.println("Stack trace:");
            for (CallFrame frame : callStack) {
                debugOutput.println("  at " + frame.getReturnAddress());
            }
        }
    }

    public List<RuntimeError> getErrors() {
        return Collections.unmodifiableList(errors);
    }
} 