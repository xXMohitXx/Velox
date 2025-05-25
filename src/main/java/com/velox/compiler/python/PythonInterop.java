package com.velox.compiler.python;

import com.velox.compiler.error.RuntimeError;

import java.util.*;
import java.lang.reflect.Method;

/**
 * Provides Velox syntax for Python interoperability.
 * This class will be used by the Velox compiler to generate appropriate code.
 */
public class PythonInterop {
    private static final Map<String, Object> pythonGlobals = new HashMap<>();
    private static final Map<String, Method> cachedMethods = new HashMap<>();
    private static boolean isInitialized = false;

    public static void initialize() {
        if (!isInitialized) {
            try {
                // Initialize Python interpreter
                System.loadLibrary("python3");
                isInitialized = true;
            } catch (UnsatisfiedLinkError e) {
                throw new RuntimeError("Failed to initialize Python interpreter", e);
            }
        }
    }

    public static Object callPythonFunction(String functionName, Object... args) {
        if (!isInitialized) {
            initialize();
        }

        try {
            Method method = getCachedMethod(functionName);
            if (method != null) {
                return method.invoke(null, args);
            }

            // Try to find the function in Python globals
            Object func = pythonGlobals.get(functionName);
            if (func == null) {
                throw new RuntimeError("Python function '" + functionName + "' not found");
            }

            return invokePythonFunction(func, args);
        } catch (Exception e) {
            throw new RuntimeError("Error calling Python function", e);
        }
    }

    private static Method getCachedMethod(String functionName) {
        return cachedMethods.computeIfAbsent(functionName, name -> {
            try {
                return PythonBridge.class.getMethod(name, Object[].class);
            } catch (NoSuchMethodException e) {
                return null;
            }
        });
    }

    private static Object invokePythonFunction(Object func, Object... args) {
        try {
            // Convert Java objects to Python objects
            Object[] pythonArgs = Arrays.stream(args)
                .map(PythonInterop::toPythonObject)
                .toArray();

            // Call Python function
            return fromPythonObject(callPythonFunctionNative(func, pythonArgs));
        } catch (Exception e) {
            throw new RuntimeError("Error invoking Python function", e);
        }
    }

    private static native Object callPythonFunctionNative(Object func, Object[] args);

    public static Object toPythonObject(Object javaObj) {
        if (javaObj == null) return null;
        
        if (javaObj instanceof Number || 
            javaObj instanceof Boolean || 
            javaObj instanceof String) {
            return javaObj;
        }

        if (javaObj instanceof List) {
            return convertListToPython((List<?>) javaObj);
        }

        if (javaObj instanceof Map) {
            return convertMapToPython((Map<?, ?>) javaObj);
        }

        return convertObjectToPython(javaObj);
    }

    private static native Object convertObjectToPython(Object obj);

    private static Object convertListToPython(List<?> list) {
        List<Object> pythonList = new ArrayList<>();
        for (Object item : list) {
            pythonList.add(toPythonObject(item));
        }
        return pythonList;
    }

    private static Object convertMapToPython(Map<?, ?> map) {
        Map<Object, Object> pythonDict = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            pythonDict.put(
                toPythonObject(entry.getKey()),
                toPythonObject(entry.getValue())
            );
        }
        return pythonDict;
    }

    public static Object fromPythonObject(Object pythonObj) {
        if (pythonObj == null) return null;

        if (pythonObj instanceof Number || 
            pythonObj instanceof Boolean || 
            pythonObj instanceof String) {
            return pythonObj;
        }

        if (isPythonList(pythonObj)) {
            return convertPythonListToJava(pythonObj);
        }

        if (isPythonDict(pythonObj)) {
            return convertPythonDictToJava(pythonObj);
        }

        return convertPythonObjectToJava(pythonObj);
    }

    private static native boolean isPythonList(Object obj);
    private static native boolean isPythonDict(Object obj);
    private static native Object convertPythonObjectToJava(Object obj);

    private static List<Object> convertPythonListToJava(Object pythonList) {
        List<Object> javaList = new ArrayList<>();
        int length = getPythonListLength(pythonList);
        for (int i = 0; i < length; i++) {
            javaList.add(fromPythonObject(getPythonListItem(pythonList, i)));
        }
        return javaList;
    }

    private static Map<Object, Object> convertPythonDictToJava(Object pythonDict) {
        Map<Object, Object> javaMap = new HashMap<>();
        Object[] keys = getPythonDictKeys(pythonDict);
        for (Object key : keys) {
            javaMap.put(
                fromPythonObject(key),
                fromPythonObject(getPythonDictItem(pythonDict, key))
            );
        }
        return javaMap;
    }

    private static native int getPythonListLength(Object list);
    private static native Object getPythonListItem(Object list, int index);
    private static native Object[] getPythonDictKeys(Object dict);
    private static native Object getPythonDictItem(Object dict, Object key);

    public static void registerPythonFunction(String name, Object function) {
        pythonGlobals.put(name, function);
    }

    public static void unregisterPythonFunction(String name) {
        pythonGlobals.remove(name);
    }

    public static void cleanup() {
        if (isInitialized) {
            try {
                // Cleanup Python interpreter
                cleanupPythonInterpreter();
                isInitialized = false;
            } catch (Exception e) {
                throw new RuntimeError("Error cleaning up Python interpreter: " + e.getMessage());
            }
        }
    }

    private static native void cleanupPythonInterpreter();
} 