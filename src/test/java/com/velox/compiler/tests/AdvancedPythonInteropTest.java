package com.velox.compiler.tests;

import com.velox.compiler.python.PythonBridge;
import com.velox.compiler.python.DataStructureBridge;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.python.core.PyObject;
import java.util.*;

class AdvancedPythonInteropTest {
    
    @Test
    void testNumpyAdvancedOperations() {
        // Import numpy
        PyObject np = PythonBridge.importModule("numpy");
        
        // Create complex arrays
        Object matrix = PythonBridge.callFunction("numpy", "array", new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });
        
        // Perform matrix operations
        Object transpose = PythonBridge.callFunction("numpy", "transpose", matrix);
        Object determinant = PythonBridge.callFunction("numpy", "linalg.det", matrix);
        Object eigenvalues = PythonBridge.callFunction("numpy", "linalg.eigvals", matrix);
        
        // Verify results
        assertNotNull(transpose);
        assertNotNull(determinant);
        assertNotNull(eigenvalues);
    }
    
    @Test
    void testPandasAdvancedOperations() {
        // Import pandas
        PyObject pd = PythonBridge.importModule("pandas");
        
        // Create complex DataFrame
        Map<String, Object> data = new HashMap<>();
        data.put("name", new String[]{"John", "Alice", "Bob", "Charlie"});
        data.put("age", new int[]{30, 25, 35, 40});
        data.put("salary", new double[]{50000.0, 60000.0, 70000.0, 80000.0});
        data.put("department", new String[]{"IT", "HR", "Finance", "IT"});
        
        Object df = PythonBridge.callFunction("pandas", "DataFrame", data);
        
        // Perform advanced operations
        PythonBridge.exec("""
            # Group by department and calculate statistics
            grouped = df.groupby('department').agg({
                'age': 'mean',
                'salary': ['mean', 'min', 'max']
            })
            
            # Create pivot table
            pivot = df.pivot_table(
                values='salary',
                index='department',
                columns='age',
                aggfunc='mean'
            )
        """);
        
        // Get results
        Object grouped = PythonBridge.eval("grouped");
        Object pivot = PythonBridge.eval("pivot");
        
        assertNotNull(grouped);
        assertNotNull(pivot);
    }
    
    @Test
    void testScikitLearnAdvanced() {
        // Import scikit-learn
        PyObject sklearn = PythonBridge.importModule("sklearn");
        
        // Create and train multiple models
        PythonBridge.exec("""
            from sklearn.datasets import make_classification
            from sklearn.model_selection import train_test_split
            from sklearn.ensemble import RandomForestClassifier
            from sklearn.svm import SVC
            from sklearn.metrics import classification_report
            
            # Generate data
            X, y = make_classification(
                n_samples=1000,
                n_features=20,
                n_informative=15,
                n_redundant=5,
                random_state=42
            )
            
            # Split data
            X_train, X_test, y_train, y_test = train_test_split(
                X, y, test_size=0.2, random_state=42
            )
            
            # Train models
            rf_model = RandomForestClassifier(n_estimators=100)
            svm_model = SVC(kernel='rbf')
            
            rf_model.fit(X_train, y_train)
            svm_model.fit(X_train, y_train)
            
            # Make predictions
            rf_pred = rf_model.predict(X_test)
            svm_pred = svm_model.predict(X_test)
            
            # Get metrics
            rf_report = classification_report(y_test, rf_pred)
            svm_report = classification_report(y_test, svm_pred)
        """);
        
        // Get results
        Object rfReport = PythonBridge.eval("rf_report");
        Object svmReport = PythonBridge.eval("svm_report");
        
        assertNotNull(rfReport);
        assertNotNull(svmReport);
    }
    
    @Test
    void testTensorFlowAdvanced() {
        // Import TensorFlow
        PyObject tf = PythonBridge.importModule("tensorflow");
        
        // Create and train a complex neural network
        PythonBridge.exec("""
            import tensorflow as tf
            from tensorflow.keras import layers, models
            
            # Create a CNN model
            model = models.Sequential([
                layers.Conv2D(32, (3, 3), activation='relu', input_shape=(28, 28, 1)),
                layers.MaxPooling2D((2, 2)),
                layers.Conv2D(64, (3, 3), activation='relu'),
                layers.MaxPooling2D((2, 2)),
                layers.Conv2D(64, (3, 3), activation='relu'),
                layers.Flatten(),
                layers.Dense(64, activation='relu'),
                layers.Dense(10, activation='softmax')
            ])
            
            # Compile model
            model.compile(
                optimizer='adam',
                loss='sparse_categorical_crossentropy',
                metrics=['accuracy']
            )
            
            # Generate dummy data
            import numpy as np
            x_train = np.random.random((1000, 28, 28, 1))
            y_train = np.random.randint(0, 10, (1000,))
            
            # Train model
            history = model.fit(
                x_train, y_train,
                epochs=2,
                batch_size=32,
                validation_split=0.2
            )
        """);
        
        // Get training history
        Object history = PythonBridge.eval("history.history");
        assertNotNull(history);
    }
    
    @Test
    void testComplexDataStructures() {
        // Create complex nested data structure
        Map<String, Object> complexData = new HashMap<>();
        complexData.put("name", "John");
        complexData.put("age", 30);
        complexData.put("scores", new double[]{85.5, 90.0, 88.5});
        complexData.put("address", Map.of(
            "street", "123 Main St",
            "city", "Boston",
            "zip", "02108"
        ));
        complexData.put("contacts", List.of(
            Map.of("type", "email", "value", "john@example.com"),
            Map.of("type", "phone", "value", "555-0123")
        ));
        
        // Convert to Python structure
        PyObject pyData = DataStructureBridge.toPythonStructure(complexData);
        
        // Process in Python
        PythonBridge.exec("""
            def process_complex_data(data):
                # Calculate average score
                avg_score = sum(data['scores']) / len(data['scores'])
                
                # Format address
                address = f"{data['address']['street']}, {data['address']['city']}, {data['address']['zip']}"
                
                # Get contact info
                email = next(c['value'] for c in data['contacts'] if c['type'] == 'email')
                phone = next(c['value'] for c in data['contacts'] if c['type'] == 'phone')
                
                return {
                    'name': data['name'],
                    'age': data['age'],
                    'avg_score': avg_score,
                    'formatted_address': address,
                    'email': email,
                    'phone': phone
                }
        """);
        
        // Call Python function and get result
        Object result = PythonBridge.callFunction("__main__", "process_complex_data", pyData);
        
        // Convert back to Java structure
        Map<String, Object> processedData = (Map<String, Object>) DataStructureBridge.toVeloxStructure((PyObject) result);
        
        // Verify results
        assertEquals("John", processedData.get("name"));
        assertEquals(30, processedData.get("age"));
        assertTrue(processedData.get("avg_score") instanceof Double);
        assertTrue(processedData.get("formatted_address") instanceof String);
        assertTrue(processedData.get("email") instanceof String);
        assertTrue(processedData.get("phone") instanceof String);
    }
} 