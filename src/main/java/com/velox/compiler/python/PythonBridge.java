package com.velox.compiler.python;

import com.velox.compiler.error.RuntimeError;
import java.util.Map;
import java.util.HashMap;

public class PythonBridge {
    private static final Map<String, Object> moduleCache = new HashMap<>();
    private static boolean isInitialized = false;

    public static void initialize() {
        if (!isInitialized) {
            try {
                System.loadLibrary("python3");
                isInitialized = true;
            } catch (UnsatisfiedLinkError e) {
                throw new RuntimeError("Failed to initialize Python interpreter", e);
            }
        }
    }

    public static Object importModule(String moduleName) {
        if (!isInitialized) {
            initialize();
        }
        return importModuleNative(moduleName);
    }

    public static Object callFunction(String moduleAlias, String functionName, Object... args) {
        Object module = moduleCache.get(moduleAlias);
        if (module == null) {
            throw new RuntimeError("Module not imported: " + moduleAlias);
        }
        return callFunctionNative(module, functionName, args);
    }

    public static void exec(String code) {
        if (!isInitialized) {
            initialize();
        }
        execNative(code);
    }

    public static Object eval(String expression) {
        if (!isInitialized) {
            initialize();
        }
        return evalNative(expression);
    }

    public static Object toPython(Object obj) {
        if (!isInitialized) {
            initialize();
        }
        return toPythonNative(obj);
    }

    public static <T> T toJava(Object pyObj, Class<T> targetClass) {
        if (!isInitialized) {
            initialize();
        }
        return toJavaNative(pyObj, targetClass);
    }

    public static void cleanup() {
        if (isInitialized) {
            cleanupNative();
            isInitialized = false;
        }
    }

    // Native method declarations
    private static native Object importModuleNative(String moduleName);
    private static native Object callFunctionNative(Object module, String functionName, Object[] args);
    private static native void execNative(String code);
    private static native Object evalNative(String expression);
    private static native Object toPythonNative(Object obj);
    private static native <T> T toJavaNative(Object pyObj, Class<T> targetClass);
    private static native void cleanupNative();
} 