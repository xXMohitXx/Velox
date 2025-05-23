package com.velox.compiler.tests;

import com.velox.compiler.python.PythonBridge;
import com.velox.compiler.python.DataStructureBridge;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.python.core.PyObject;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ExtendedPythonInteropTest {
    
    @Test
    void testNumpyExtendedOperations() {
        // Import numpy
        PyObject np = PythonBridge.importModule("numpy");
        
        // Test array creation with different data types
        Object intArray = PythonBridge.callFunction("numpy", "array", new int[]{1, 2, 3, 4, 5});
        Object floatArray = PythonBridge.callFunction("numpy", "array", new float[]{1.1f, 2.2f, 3.3f});
        Object complexArray = PythonBridge.callFunction("numpy", "array", new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        
        // Test array operations
        Object reshaped = PythonBridge.callFunction("numpy", "reshape", intArray, new int[]{5, 1});
        Object transposed = PythonBridge.callFunction("numpy", "transpose", complexArray);
        Object concatenated = PythonBridge.callFunction("numpy", "concatenate", new Object[]{intArray, intArray});
        
        // Test statistical operations
        Object mean = PythonBridge.callFunction("numpy", "mean", floatArray);
        Object std = PythonBridge.callFunction("numpy", "std", floatArray);
        Object percentile = PythonBridge.callFunction("numpy", "percentile", intArray, 75);
        
        // Test linear algebra operations
        Object dotProduct = PythonBridge.callFunction("numpy", "dot", complexArray, complexArray);
        Object eigenvals = PythonBridge.callFunction("numpy", "linalg.eigvals", complexArray);
        Object svd = PythonBridge.callFunction("numpy", "linalg.svd", complexArray);
        
        // Verify results
        assertNotNull(reshaped);
        assertNotNull(transposed);
        assertNotNull(concatenated);
        assertNotNull(mean);
        assertNotNull(std);
        assertNotNull(percentile);
        assertNotNull(dotProduct);
        assertNotNull(eigenvals);
        assertNotNull(svd);
    }
    
    @Test
    void testPandasExtendedOperations() {
        // Import pandas
        PyObject pd = PythonBridge.importModule("pandas");
        
        // Create complex DataFrame with mixed data types
        Map<String, Object> data = new HashMap<>();
        data.put("id", new int[]{1, 2, 3, 4, 5});
        data.put("name", new String[]{"John", "Alice", "Bob", "Charlie", "David"});
        data.put("age", new int[]{30, 25, 35, 40, 28});
        data.put("salary", new double[]{50000.0, 60000.0, 70000.0, 80000.0, 55000.0});
        data.put("department", new String[]{"IT", "HR", "Finance", "IT", "Marketing"});
        data.put("join_date", new String[]{"2020-01-01", "2020-02-15", "2019-12-01", "2021-03-01", "2020-06-15"});
        data.put("is_active", new boolean[]{true, true, false, true, true});
        
        Object df = PythonBridge.callFunction("pandas", "DataFrame", data);
        
        // Perform advanced operations
        PythonBridge.exec("""
            # Convert join_date to datetime
            df['join_date'] = pd.to_datetime(df['join_date'])
            
            # Calculate tenure in months
            df['tenure_months'] = ((pd.Timestamp.now() - df['join_date']) / pd.Timedelta(days=30)).astype(int)
            
            # Create age groups
            df['age_group'] = pd.cut(df['age'], bins=[0, 30, 40, 50], labels=['Young', 'Middle', 'Senior'])
            
            # Calculate department statistics
            dept_stats = df.groupby('department').agg({
                'age': ['mean', 'min', 'max'],
                'salary': ['mean', 'min', 'max'],
                'tenure_months': 'mean',
                'is_active': 'sum'
            })
            
            # Create pivot tables
            salary_pivot = df.pivot_table(
                values='salary',
                index='department',
                columns='age_group',
                aggfunc=['mean', 'min', 'max']
            )
            
            # Time series analysis
            monthly_joins = df.groupby(df['join_date'].dt.to_period('M')).size()
            
            # Correlation analysis
            numeric_cols = df.select_dtypes(include=['int64', 'float64']).columns
            correlation_matrix = df[numeric_cols].corr()
        """);
        
        // Get results
        Object deptStats = PythonBridge.eval("dept_stats");
        Object salaryPivot = PythonBridge.eval("salary_pivot");
        Object monthlyJoins = PythonBridge.eval("monthly_joins");
        Object correlationMatrix = PythonBridge.eval("correlation_matrix");
        
        assertNotNull(deptStats);
        assertNotNull(salaryPivot);
        assertNotNull(monthlyJoins);
        assertNotNull(correlationMatrix);
    }
    
    @Test
    void testScikitLearnExtended() {
        // Import scikit-learn
        PyObject sklearn = PythonBridge.importModule("sklearn");
        
        // Create and train multiple models with different algorithms
        PythonBridge.exec("""
            from sklearn.datasets import make_classification, make_regression
            from sklearn.model_selection import train_test_split, cross_val_score
            from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier
            from sklearn.svm import SVC
            from sklearn.neural_network import MLPClassifier
            from sklearn.preprocessing import StandardScaler
            from sklearn.pipeline import Pipeline
            from sklearn.metrics import classification_report, confusion_matrix
            
            # Generate classification data
            X_clf, y_clf = make_classification(
                n_samples=1000,
                n_features=20,
                n_informative=15,
                n_redundant=5,
                n_classes=3,
                random_state=42
            )
            
            # Generate regression data
            X_reg, y_reg = make_regression(
                n_samples=1000,
                n_features=20,
                n_informative=15,
                noise=0.1,
                random_state=42
            )
            
            # Split data
            X_clf_train, X_clf_test, y_clf_train, y_clf_test = train_test_split(
                X_clf, y_clf, test_size=0.2, random_state=42
            )
            
            # Create and train models
            models = {
                'rf': RandomForestClassifier(n_estimators=100),
                'gb': GradientBoostingClassifier(n_estimators=100),
                'svm': SVC(kernel='rbf'),
                'mlp': MLPClassifier(hidden_layer_sizes=(100, 50))
            }
            
            # Train and evaluate each model
            results = {}
            for name, model in models.items():
                # Create pipeline with scaling
                pipeline = Pipeline([
                    ('scaler', StandardScaler()),
                    ('classifier', model)
                ])
                
                # Train model
                pipeline.fit(X_clf_train, y_clf_train)
                
                # Make predictions
                y_pred = pipeline.predict(X_clf_test)
                
                # Calculate metrics
                results[name] = {
                    'report': classification_report(y_clf_test, y_pred),
                    'confusion': confusion_matrix(y_clf_test, y_pred),
                    'cv_scores': cross_val_score(pipeline, X_clf, y_clf, cv=5)
                }
        """);
        
        // Get results for each model
        for (String model : new String[]{"rf", "gb", "svm", "mlp"}) {
            Object report = PythonBridge.eval("results['" + model + "']['report']");
            Object confusion = PythonBridge.eval("results['" + model + "']['confusion']");
            Object cvScores = PythonBridge.eval("results['" + model + "']['cv_scores']");
            
            assertNotNull(report);
            assertNotNull(confusion);
            assertNotNull(cvScores);
        }
    }
    
    @Test
    void testTensorFlowExtended() {
        // Import TensorFlow
        PyObject tf = PythonBridge.importModule("tensorflow");
        
        // Create and train multiple neural network architectures
        PythonBridge.exec("""
            import tensorflow as tf
            from tensorflow.keras import layers, models
            import numpy as np
            
            # Generate data
            x_train = np.random.random((1000, 28, 28, 1))
            y_train = np.random.randint(0, 10, (1000,))
            
            # Create CNN model
            cnn_model = models.Sequential([
                layers.Conv2D(32, (3, 3), activation='relu', input_shape=(28, 28, 1)),
                layers.MaxPooling2D((2, 2)),
                layers.Conv2D(64, (3, 3), activation='relu'),
                layers.MaxPooling2D((2, 2)),
                layers.Conv2D(64, (3, 3), activation='relu'),
                layers.Flatten(),
                layers.Dense(64, activation='relu'),
                layers.Dropout(0.5),
                layers.Dense(10, activation='softmax')
            ])
            
            # Create RNN model
            rnn_model = models.Sequential([
                layers.LSTM(64, input_shape=(28, 28)),
                layers.Dense(32, activation='relu'),
                layers.Dropout(0.3),
                layers.Dense(10, activation='softmax')
            ])
            
            # Create Autoencoder
            autoencoder = models.Sequential([
                layers.Dense(128, activation='relu', input_shape=(784,)),
                layers.Dense(64, activation='relu'),
                layers.Dense(32, activation='relu'),
                layers.Dense(64, activation='relu'),
                layers.Dense(128, activation='relu'),
                layers.Dense(784, activation='sigmoid')
            ])
            
            # Compile models
            cnn_model.compile(
                optimizer='adam',
                loss='sparse_categorical_crossentropy',
                metrics=['accuracy']
            )
            
            rnn_model.compile(
                optimizer='adam',
                loss='sparse_categorical_crossentropy',
                metrics=['accuracy']
            )
            
            autoencoder.compile(
                optimizer='adam',
                loss='binary_crossentropy'
            )
            
            # Train models
            cnn_history = cnn_model.fit(
                x_train, y_train,
                epochs=2,
                batch_size=32,
                validation_split=0.2
            )
            
            rnn_history = rnn_model.fit(
                x_train.reshape(1000, 28, 28), y_train,
                epochs=2,
                batch_size=32,
                validation_split=0.2
            )
            
            autoencoder_history = autoencoder.fit(
                x_train.reshape(1000, 784),
                x_train.reshape(1000, 784),
                epochs=2,
                batch_size=32,
                validation_split=0.2
            )
        """);
        
        // Get training histories
        Object cnnHistory = PythonBridge.eval("cnn_history.history");
        Object rnnHistory = PythonBridge.eval("rnn_history.history");
        Object autoencoderHistory = PythonBridge.eval("autoencoder_history.history");
        
        assertNotNull(cnnHistory);
        assertNotNull(rnnHistory);
        assertNotNull(autoencoderHistory);
    }
    
    @Test
    void testErrorHandling() {
        // Test invalid data type conversion
        assertThrows(IllegalArgumentException.class, () -> {
            DataStructureBridge.toPythonStructure(new Object());
        });
        
        // Test invalid array type
        assertThrows(IllegalArgumentException.class, () -> {
            DataStructureBridge.toPythonStructure(new boolean[][]{{true, false}, {false, true}});
        });
        
        // Test Python runtime errors
        assertThrows(RuntimeException.class, () -> {
            PythonBridge.exec("""
                def divide(a, b):
                    return a / b
                
                result = divide(1, 0)  # This will raise ZeroDivisionError
            """);
        });
        
        // Test invalid module import
        assertThrows(RuntimeException.class, () -> {
            PythonBridge.importModule("nonexistent_module");
        });
        
        // Test invalid function call
        assertThrows(RuntimeException.class, () -> {
            PythonBridge.callFunction("numpy", "nonexistent_function", new Object[]{});
        });
    }
    
    @Test
    void testComplexDataStructuresExtended() {
        // Create complex nested data structure with various types
        Map<String, Object> complexData = new HashMap<>();
        
        // Add basic types
        complexData.put("string", "Hello, World!");
        complexData.put("integer", 42);
        complexData.put("float", 3.14159);
        complexData.put("boolean", true);
        complexData.put("null", null);
        
        // Add arrays
        complexData.put("int_array", new int[]{1, 2, 3, 4, 5});
        complexData.put("double_array", new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
        complexData.put("string_array", new String[]{"apple", "banana", "cherry"});
        
        // Add nested maps
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("name", "John Doe");
        nestedMap.put("age", 30);
        nestedMap.put("address", Map.of(
            "street", "123 Main St",
            "city", "Boston",
            "zip", "02108"
        ));
        complexData.put("person", nestedMap);
        
        // Add lists of maps
        List<Map<String, Object>> transactions = new ArrayList<>();
        transactions.add(Map.of(
            "id", 1,
            "amount", 100.50,
            "date", "2024-01-01"
        ));
        transactions.add(Map.of(
            "id", 2,
            "amount", 200.75,
            "date", "2024-01-02"
        ));
        complexData.put("transactions", transactions);
        
        // Add sets
        complexData.put("unique_ids", new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)));
        
        // Convert to Python structure
        PyObject pyData = DataStructureBridge.toPythonStructure(complexData);
        
        // Process in Python
        PythonBridge.exec("""
            def process_complex_data(data):
                result = {}
                
                # Process basic types
                result['string_upper'] = data['string'].upper()
                result['integer_double'] = data['integer'] * 2
                result['float_rounded'] = round(data['float'], 2)
                result['boolean_negated'] = not data['boolean']
                result['null_check'] = data['null'] is None
                
                # Process arrays
                result['int_array_sum'] = sum(data['int_array'])
                result['double_array_mean'] = sum(data['double_array']) / len(data['double_array'])
                result['string_array_joined'] = '-'.join(data['string_array'])
                
                # Process nested map
                person = data['person']
                result['person_info'] = f"{person['name']} is {person['age']} years old"
                result['address'] = f"{person['address']['street']}, {person['address']['city']}"
                
                # Process transactions
                total_amount = sum(t['amount'] for t in data['transactions'])
                result['total_amount'] = total_amount
                result['transaction_count'] = len(data['transactions'])
                
                # Process set
                result['unique_count'] = len(data['unique_ids'])
                result['contains_three'] = 3 in data['unique_ids']
                
                return result
        """);
        
        // Call Python function and get result
        Object result = PythonBridge.callFunction("__main__", "process_complex_data", pyData);
        
        // Convert back to Java structure
        Map<String, Object> processedData = (Map<String, Object>) DataStructureBridge.toVeloxStructure((PyObject) result);
        
        // Verify results
        assertEquals("HELLO, WORLD!", processedData.get("string_upper"));
        assertEquals(84, processedData.get("integer_double"));
        assertEquals(3.14, processedData.get("float_rounded"));
        assertEquals(false, processedData.get("boolean_negated"));
        assertEquals(true, processedData.get("null_check"));
        
        assertEquals(15, processedData.get("int_array_sum"));
        assertTrue(processedData.get("double_array_mean") instanceof Double);
        assertEquals("apple-banana-cherry", processedData.get("string_array_joined"));
        
        assertEquals("John Doe is 30 years old", processedData.get("person_info"));
        assertEquals("123 Main St, Boston", processedData.get("address"));
        
        assertEquals(301.25, processedData.get("total_amount"));
        assertEquals(2, processedData.get("transaction_count"));
        
        assertEquals(5, processedData.get("unique_count"));
        assertEquals(true, processedData.get("contains_three"));
    }
} 