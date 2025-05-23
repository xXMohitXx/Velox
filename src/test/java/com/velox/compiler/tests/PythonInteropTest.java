package com.velox.compiler.tests;

import com.velox.compiler.python.PythonBridge;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.python.core.PyObject;
import java.util.List;
import java.util.Map;

class PythonInteropTest {
    @Test
    void testNumpyOperations() {
        // Import numpy
        PyObject np = PythonBridge.importModule("numpy");
        
        // Create arrays
        Object array1 = PythonBridge.callFunction("numpy", "array", new double[]{1.0, 2.0, 3.0});
        Object array2 = PythonBridge.callFunction("numpy", "array", new double[]{4.0, 5.0, 6.0});
        
        // Perform operations
        Object sum = PythonBridge.callFunction("numpy", "add", array1, array2);
        Object dot = PythonBridge.callFunction("numpy", "dot", array1, array2);
        
        // Verify results
        assertNotNull(sum);
        assertNotNull(dot);
    }

    @Test
    void testPandasOperations() {
        // Import pandas
        PyObject pd = PythonBridge.importModule("pandas");
        
        // Create DataFrame
        Object df = PythonBridge.callFunction("pandas", "DataFrame", 
            Map.of(
                "name", new String[]{"John", "Alice", "Bob"},
                "age", new int[]{30, 25, 35}
            )
        );
        
        // Perform operations
        Object meanAge = PythonBridge.callFunction("pandas", "mean", df);
        
        // Verify results
        assertNotNull(df);
        assertNotNull(meanAge);
    }

    @Test
    void testMatplotlibPlotting() {
        // Import matplotlib
        PyObject plt = PythonBridge.importModule("matplotlib.pyplot");
        
        // Create plot
        PythonBridge.exec("""
            import numpy as np
            x = np.linspace(0, 10, 100)
            y = np.sin(x)
            plt.plot(x, y)
            plt.title('Sine Wave')
            plt.savefig('test_plot.png')
            plt.close()
        """);
        
        // Verify plot was created
        assertTrue(new java.io.File("test_plot.png").exists());
        new java.io.File("test_plot.png").delete();
    }

    @Test
    void testScikitLearn() {
        // Import scikit-learn
        PyObject sklearn = PythonBridge.importModule("sklearn");
        
        // Create and train a simple model
        PythonBridge.exec("""
            from sklearn.datasets import make_classification
            from sklearn.model_selection import train_test_split
            from sklearn.linear_model import LogisticRegression
            
            X, y = make_classification(n_samples=100, n_features=2, n_classes=2)
            X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)
            
            model = LogisticRegression()
            model.fit(X_train, y_train)
            
            score = model.score(X_test, y_test)
        """);
        
        // Get the score
        Object score = PythonBridge.eval("score");
        assertNotNull(score);
        assertTrue(score instanceof Double);
    }

    @Test
    void testTensorFlow() {
        // Import TensorFlow
        PyObject tf = PythonBridge.importModule("tensorflow");
        
        // Create a simple neural network
        PythonBridge.exec("""
            import tensorflow as tf
            
            model = tf.keras.Sequential([
                tf.keras.layers.Dense(10, activation='relu', input_shape=(5,)),
                tf.keras.layers.Dense(1)
            ])
            
            model.compile(optimizer='adam', loss='mse')
        """);
        
        // Verify model was created
        Object model = PythonBridge.eval("model");
        assertNotNull(model);
    }

    @Test
    void testCustomPythonCode() {
        // Define a Python function
        PythonBridge.exec("""
            def process_data(data):
                return [x * 2 for x in data]
        """);
        
        // Call the function with Java data
        List<Integer> result = (List<Integer>) PythonBridge.callFunction(
            "__main__", 
            "process_data", 
            new int[]{1, 2, 3, 4, 5}
        );
        
        // Verify results
        assertEquals(5, result.size());
        assertEquals(2, result.get(0));
        assertEquals(4, result.get(1));
        assertEquals(6, result.get(2));
        assertEquals(8, result.get(3));
        assertEquals(10, result.get(4));
    }
} 