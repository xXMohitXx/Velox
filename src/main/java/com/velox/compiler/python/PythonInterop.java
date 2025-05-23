package com.velox.compiler.python;

import org.python.core.PyObject;
import java.util.Map;
import java.util.HashMap;

/**
 * Provides Velox syntax for Python interoperability.
 * This class will be used by the Velox compiler to generate appropriate code.
 */
public class PythonInterop {
    private static final Map<String, PyObject> moduleCache = new HashMap<>();

    /**
     * Import a Python module in Velox syntax
     * Example: import py "numpy" as np;
     */
    public static PyObject importModule(String moduleName, String alias) {
        PyObject module = PythonBridge.importModule(moduleName);
        moduleCache.put(alias, module);
        return module;
    }

    /**
     * Call a Python function in Velox syntax
     * Example: let result = np.array([1, 2, 3]);
     */
    public static Object callFunction(String moduleAlias, String functionName, Object... args) {
        PyObject module = moduleCache.get(moduleAlias);
        if (module == null) {
            throw new RuntimeException("Module not imported: " + moduleAlias);
        }
        return PythonBridge.callFunction(moduleAlias, functionName, args);
    }

    /**
     * Execute Python code in Velox syntax
     * Example: py.exec("""
     *     def process(x):
     *         return x * 2
     * """);
     */
    public static void exec(String code) {
        PythonBridge.exec(code);
    }

    /**
     * Evaluate Python expression in Velox syntax
     * Example: let result = py.eval("2 + 2");
     */
    public static Object eval(String expression) {
        return PythonBridge.eval(expression);
    }

    /**
     * Convert Velox object to Python object
     * Example: let pyObj = py.toPython(myVeloxObject);
     */
    public static PyObject toPython(Object obj) {
        return PythonBridge.toPython(obj);
    }

    /**
     * Convert Python object to Velox object
     * Example: let veloxObj = py.toJava(pyObj, MyType.class);
     */
    public static <T> T toJava(PyObject pyObj, Class<T> targetClass) {
        return PythonBridge.toJava(pyObj, targetClass);
    }
} 