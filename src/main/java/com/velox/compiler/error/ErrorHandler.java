package com.velox.compiler.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private final List<CompilationError> errors;
    private boolean hadError;

    public ErrorHandler() {
        this.errors = new ArrayList<>();
        this.hadError = false;
    }

    public void handleError(CompilationError error) {
        errors.add(error);
        hadError = true;
    }

    public void handleError(String message) {
        handleError(new CompilationError(message));
    }

    public void handleError(String message, Throwable cause) {
        handleError(new CompilationError(message, cause));
    }

    public boolean hadError() {
        return hadError;
    }

    public List<CompilationError> getErrors() {
        return new ArrayList<>(errors);
    }

    public void clear() {
        errors.clear();
        hadError = false;
    }

    @Override
    public String toString() {
        if (errors.isEmpty()) {
            return "No errors";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Compilation errors:\n");
        for (CompilationError error : errors) {
            sb.append(error.getMessage()).append("\n");
        }
        return sb.toString();
    }
} 