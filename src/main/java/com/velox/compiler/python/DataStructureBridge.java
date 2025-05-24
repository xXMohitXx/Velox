package com.velox.compiler.python;

import org.python.core.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Bridge class for handling complex data structures between Velox and Python.
 */
public class DataStructureBridge {
    
    /**
     * Convert Velox data structures to Python equivalents
     */
    public static PyObject toPythonStructure(Object obj) {
        if (obj == null) {
            return Py.None;
        }
        
        if (obj instanceof Map) {
            return toPythonDict((Map<?, ?>) obj);
        } else if (obj instanceof List) {
            return toPythonList((List<?>) obj);
        } else if (obj instanceof Set) {
            return toPythonSet((Set<?>) obj);
        } else if (obj instanceof Number) {
            return Py.java2py(obj);
        } else if (obj instanceof String) {
            return new PyString((String) obj);
        } else if (obj instanceof Boolean) {
            return Py.java2py(obj);
        } else if (obj.getClass().isArray()) {
            return toPythonArray(obj);
        }
        
        throw new IllegalArgumentException("Unsupported data structure type: " + obj.getClass());
    }

    /**
     * Convert Python data structures to Velox equivalents
     */
    public static Object toVeloxStructure(Object obj) {
        if (obj instanceof PyObject) {
            return toVeloxStructure((PyObject) obj);
        }
        return obj;
    }

    public static Object toVeloxStructure(PyObject pyObj) {
        if (pyObj == null || pyObj == Py.None) {
            return null;
        }
        
        if (pyObj instanceof PyDictionary) {
            return toVeloxMap((PyDictionary) pyObj);
        } else if (pyObj instanceof PyList) {
            return toVeloxList((PyList) pyObj);
        } else if (pyObj instanceof PySet) {
            return toVeloxSet((PySet) pyObj);
        } else if (pyObj instanceof PyString) {
            return pyObj.__str__().toString();
        } else if (pyObj instanceof PyInteger) {
            return pyObj.__int__();
        } else if (pyObj instanceof PyFloat) {
            return pyObj.__float__();
        } else if (pyObj instanceof PyBoolean) {
            return pyObj.__nonzero__();
        } else if (pyObj instanceof PyArray) {
            return toVeloxArray((PyArray) pyObj);
        }
        
        return pyObj.__tojava__(Object.class);
    }

    private static PyDictionary toPythonDict(Map<?, ?> map) {
        PyDictionary dict = new PyDictionary();
        map.forEach((k, v) -> dict.__setitem__(
            toPythonStructure(k),
            toPythonStructure(v)
        ));
        return dict;
    }

    private static PyList toPythonList(List<?> list) {
        PyList pyList = new PyList();
        list.forEach(item -> pyList.append(toPythonStructure(item)));
        return pyList;
    }

    private static PySet toPythonSet(Set<?> set) {
        PySet pySet = new PySet();
        set.forEach(item -> pySet.add(toPythonStructure(item)));
        return pySet;
    }

    private static PyArray toPythonArray(Object array) {
        if (array instanceof int[]) {
            return new PyArray(PyInteger.class, (int[]) array);
        } else if (array instanceof double[]) {
            return new PyArray(PyFloat.class, (double[]) array);
        } else if (array instanceof String[]) {
            return new PyArray(PyString.class, (String[]) array);
        }
        throw new IllegalArgumentException("Unsupported array type: " + array.getClass());
    }

    @SuppressWarnings("unchecked")
    private static Map<Object, Object> toVeloxMap(PyDictionary dict) {
        Map<Object, Object> map = new HashMap<>();
        dict.items().forEach(item -> {
            PyTuple tuple = (PyTuple) item;
            map.put(
                toVeloxStructure(tuple.__getitem__(0)),
                toVeloxStructure(tuple.__getitem__(1))
            );
        });
        return map;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> toVeloxList(PyList list) {
        return (List<Object>) list.stream()
            .map(DataStructureBridge::toVeloxStructure)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static Set<Object> toVeloxSet(PySet set) {
        return (Set<Object>) set.stream()
            .map(DataStructureBridge::toVeloxStructure)
            .collect(Collectors.toSet());
    }

    private static Object toVeloxArray(PyArray array) {
        if (array.getType().equals(PyInteger.TYPE)) {
            return array.__tojava__(Integer[].class);
        } else if (array.getType().equals(PyFloat.TYPE)) {
            return array.__tojava__(Double[].class);
        } else if (array.getType().equals(PyString.TYPE)) {
            return array.__tojava__(String[].class);
        }
        throw new IllegalArgumentException("Unsupported array type: " + array.getType());
    }
} 