package com.velox.compiler.python;

import org.python.core.*;
import org.python.util.PythonInterpreter;
import java.util.Map;
import java.util.HashMap;

/**
 * Bridge class for Python interoperability in Velox.
 * Handles Python library imports and function calls.
 */
public class PythonBridge {
    private static final PythonInterpreter interpreter = new PythonInterpreter();
    private static final Map<String, PyObject> importedModules = new HashMap<>();

    /**
     * Import a Python module
     * @param moduleName The name of the Python module to import
     * @return The imported module as a PyObject
     */
    public static PyObject importModule(String moduleName) {
        if (importedModules.containsKey(moduleName)) {
            return importedModules.get(moduleName);
        }

        try {
            PyObject module = interpreter.get(moduleName);
            if (module == null) {
                interpreter.exec("import " + moduleName);
                module = interpreter.get(moduleName);
            }
            importedModules.put(moduleName, module);
            return module;
        } catch (PyException e) {
            throw new RuntimeException("Failed to import Python module: " + moduleName, e);
        }
    }

    /**
     * Call a Python function
     * @param moduleName The name of the module containing the function
     * @param functionName The name of the function to call
     * @param args The arguments to pass to the function
     * @return The result of the function call
     */
    public static Object callFunction(String moduleName, String functionName, Object... args) {
        PyObject module = importModule(moduleName);
        PyObject function = module.__getattr__(functionName);
        
        PyObject[] pyArgs = new PyObject[args.length];
        for (int i = 0; i < args.length; i++) {
            pyArgs[i] = Py.java2py(args[i]);
        }
        
        try {
            PyObject result = function.__call__(pyArgs);
            return result.__tojava__(Object.class);
        } catch (PyException e) {
            throw new RuntimeException("Failed to call Python function: " + functionName, e);
        }
    }

    /**
     * Convert a Java object to a Python object
     * @param obj The Java object to convert
     * @return The equivalent Python object
     */
    public static PyObject toPython(Object obj) {
        return Py.java2py(obj);
    }

    /**
     * Convert a Python object to a Java object
     * @param pyObj The Python object to convert
     * @param targetClass The target Java class
     * @return The equivalent Java object
     */
    public static <T> T toJava(PyObject pyObj, Class<T> targetClass) {
        return (T) pyObj.__tojava__(targetClass);
    }

    /**
     * Execute arbitrary Python code
     * @param code The Python code to execute
     */
    public static void exec(String code) {
        try {
            interpreter.exec(code);
        } catch (PyException e) {
            throw new RuntimeException("Failed to execute Python code", e);
        }
    }

    /**
     * Evaluate a Python expression
     * @param expression The Python expression to evaluate
     * @return The result of the evaluation
     */
    public static Object eval(String expression) {
        try {
            PyObject result = interpreter.eval(expression);
            return result.__tojava__(Object.class);
        } catch (PyException e) {
            throw new RuntimeException("Failed to evaluate Python expression", e);
        }
    }
} 