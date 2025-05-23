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

    private static List<Object> toVeloxList(PyList list) {
        return list.stream()
            .map(DataStructureBridge::toVeloxStructure)
            .collect(Collectors.toList());
    }

    private static Set<Object> toVeloxSet(PySet set) {
        return set.stream()
            .map(DataStructureBridge::toVeloxStructure)
            .collect(Collectors.toSet());
    }

    private static Object toVeloxArray(PyArray array) {
        if (array.getType() == PyInteger.class) {
            return array.toArray(new Integer[0]);
        } else if (array.getType() == PyFloat.class) {
            return array.toArray(new Double[0]);
        } else if (array.getType() == PyString.class) {
            return array.toArray(new String[0]);
        }
        throw new IllegalArgumentException("Unsupported array type: " + array.getType());
    }
} 