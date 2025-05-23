# Advanced Usage Guide

## Advanced Language Features

### Metaprogramming

#### Code Generation
```velox
// Generate code at compile time
macro generateGetters(className: string, fields: string[]): string {
    let code = "";
    for (let field in fields) {
        code += """
            function get${field.capitalize()}(): ${field.type} {
                return this.${field.name};
            }
        """;
    }
    return code;
}

// Usage
class Person {
    private name: string;
    private age: int;
    
    // Generate getters
    ${generateGetters("Person", [
        { name: "name", type: "string" },
        { name: "age", type: "int" }
    ])}
}
```

#### Reflection
```velox
// Get class information at runtime
function inspectClass(obj: any): void {
    let classInfo = reflect.getClassInfo(obj);
    println("Class name: " + classInfo.name);
    println("Fields: " + classInfo.fields);
    println("Methods: " + classInfo.methods);
    
    // Get field value
    let value = reflect.getFieldValue(obj, "fieldName");
    
    // Call method dynamically
    let result = reflect.callMethod(obj, "methodName", [arg1, arg2]);
}
```

### Concurrency

#### Async/Await with Error Handling
```velox
async function processData(items: any[]): Promise<any[]> {
    let results = [];
    let errors = [];
    
    for (let item in items) {
        try {
            let result = await processItem(item);
            results.push(result);
        } catch (error) {
            errors.push({
                item: item,
                error: error
            });
        }
    }
    
    if (errors.length > 0) {
        throw new BatchProcessingError(errors);
    }
    
    return results;
}

// Usage with error handling
try {
    let results = await processData(items);
    println("Processing completed successfully");
} catch (error) {
    if (error instanceof BatchProcessingError) {
        println("Some items failed to process:");
        for (let failure in error.failures) {
            println("- Item: " + failure.item);
            println("  Error: " + failure.error);
        }
    }
}
```

#### Parallel Processing
```velox
// Process items in parallel
async function processInParallel(items: any[], maxConcurrent: int = 4): Promise<any[]> {
    let results = [];
    let chunks = items.chunk(maxConcurrent);
    
    for (let chunk in chunks) {
        let promises = chunk.map(item => processItem(item));
        let chunkResults = await Promise.all(promises);
        results.push(...chunkResults);
    }
    
    return results;
}

// Usage with progress tracking
async function processWithProgress(items: any[]): Promise<any[]> {
    let total = items.length;
    let processed = 0;
    
    let results = await processInParallel(items, 4);
    
    // Update progress
    processed += results.length;
    updateProgress(processed / total * 100);
    
    return results;
}
```

### Advanced Python Integration

#### Custom Python Module
```velox
// Create a custom Python module
let customModule = """
import numpy as np
from sklearn.preprocessing import StandardScaler

class CustomProcessor:
    def __init__(self):
        self.scaler = StandardScaler()
    
    def process_data(self, data):
        scaled_data = self.scaler.fit_transform(data)
        return np.mean(scaled_data, axis=1)
""";

// Save and import the module
import python;
python.saveModule("custom_processor", customModule);
import custom_processor as cp;

// Use the custom module
let processor = new cp.CustomProcessor();
let result = processor.process_data(data);
```

#### Advanced Data Processing Pipeline
```velox
// Create a data processing pipeline
class DataPipeline {
    private steps: any[];
    
    constructor() {
        this.steps = [];
    }
    
    function addStep(step: any): void {
        this.steps.push(step);
    }
    
    async function process(data: any): Promise<any> {
        let result = data;
        
        for (let step in this.steps) {
            result = await step.process(result);
        }
        
        return result;
    }
}

// Usage with Python integration
let pipeline = new DataPipeline();

// Add preprocessing step
pipeline.addStep({
    async function process(data: any): Promise<any> {
        let scaler = new sklearn.preprocessing.StandardScaler();
        return scaler.fit_transform(data);
    }
});

// Add feature extraction step
pipeline.addStep({
    async function process(data: any): Promise<any> {
        let pca = new sklearn.decomposition.PCA(n_components=2);
        return pca.fit_transform(data);
    }
});

// Add model prediction step
pipeline.addStep({
    async function process(data: any): Promise<any> {
        let model = new sklearn.ensemble.RandomForestClassifier();
        return model.predict(data);
    }
});

// Process data
let result = await pipeline.process(inputData);
```

### Advanced Error Handling

#### Custom Error Types
```velox
// Define custom error types
class ValidationError extends Error {
    constructor(message: string, field: string) {
        super(message);
        this.field = field;
    }
}

class ProcessingError extends Error {
    constructor(message: string, code: int) {
        super(message);
        this.code = code;
    }
}

// Error handling with type checking
function handleError(error: Error): void {
    if (error instanceof ValidationError) {
        println("Validation error in field: " + error.field);
        println("Message: " + error.message);
    } else if (error instanceof ProcessingError) {
        println("Processing error with code: " + error.code);
        println("Message: " + error.message);
    } else {
        println("Unexpected error: " + error.message);
    }
}
```

#### Error Recovery
```velox
// Implement error recovery
class ErrorRecovery {
    private maxRetries: int;
    private backoffMs: int;
    
    constructor(maxRetries: int = 3, backoffMs: int = 1000) {
        this.maxRetries = maxRetries;
        this.backoffMs = backoffMs;
    }
    
    async function withRetry<T>(operation: () => Promise<T>): Promise<T> {
        let lastError: Error;
        
        for (let attempt = 1; attempt <= this.maxRetries; attempt++) {
            try {
                return await operation();
            } catch (error) {
                lastError = error;
                
                if (attempt < this.maxRetries) {
                    await sleep(this.backoffMs * attempt);
                }
            }
        }
        
        throw lastError;
    }
}

// Usage
let recovery = new ErrorRecovery();

try {
    let result = await recovery.withRetry(async () => {
        return await riskyOperation();
    });
} catch (error) {
    println("Operation failed after all retries");
}
```

### Performance Optimization

#### Caching
```velox
// Implement caching
class Cache<K, V> {
    private cache: map<K, V>;
    private ttl: int;
    
    constructor(ttlSeconds: int = 3600) {
        this.cache = new map<K, V>();
        this.ttl = ttlSeconds;
    }
    
    function get(key: K): V {
        let entry = this.cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.value;
        }
        return null;
    }
    
    function set(key: K, value: V): void {
        this.cache.set(key, new CacheEntry(value, this.ttl));
    }
}

// Usage with function caching
function memoize<T, R>(fn: (T) => R, ttl: int = 3600): (T) => R {
    let cache = new Cache<T, R>(ttl);
    
    return (arg: T): R => {
        let cached = cache.get(arg);
        if (cached != null) {
            return cached;
        }
        
        let result = fn(arg);
        cache.set(arg, result);
        return result;
    };
}

// Example usage
let expensiveOperation = memoize((input: string): int => {
    // Expensive computation
    return computeHash(input);
});
```

#### Resource Management
```velox
// Implement resource management
class Resource<T> {
    private resource: T;
    private cleanup: (T) => void;
    
    constructor(resource: T, cleanup: (T) => void) {
        this.resource = resource;
        this.cleanup = cleanup;
    }
    
    function use<R>(operation: (T) => R): R {
        try {
            return operation(this.resource);
        } finally {
            this.cleanup(this.resource);
        }
    }
}

// Usage with file handling
function withFile(path: string, operation: (File) => void): void {
    let file = new File(path);
    let resource = new Resource(file, (f) => f.close());
    
    resource.use((f) => {
        // File operations
        let content = f.read();
        operation(content);
    });
}
```

### Testing and Debugging

#### Advanced Testing
```velox
// Implement property-based testing
class PropertyTest {
    function forAll<T>(
        generator: () => T,
        property: (T) => bool,
        iterations: int = 100
    ): void {
        for (let i = 0; i < iterations; i++) {
            let value = generator();
            assert(property(value), "Property failed for value: " + value);
        }
    }
}

// Usage
let test = new PropertyTest();

test.forAll(
    () => randomInt(0, 100),
    (n) => n >= 0 && n <= 100
);

// Implement fuzzing
class Fuzzer {
    function fuzz<T>(
        input: T,
        mutator: (T) => T,
        validator: (T) => bool,
        iterations: int = 1000
    ): T[] {
        let failures = [];
        
        for (let i = 0; i < iterations; i++) {
            let mutated = mutator(input);
            if (!validator(mutated)) {
                failures.push(mutated);
            }
        }
        
        return failures;
    }
}
```

#### Advanced Debugging
```velox
// Implement debug logging
class DebugLogger {
    private static instance: DebugLogger;
    private logLevel: int;
    private logFile: File;
    
    private constructor() {
        this.logLevel = 0;
        this.logFile = new File("debug.log");
    }
    
    static function getInstance(): DebugLogger {
        if (instance == null) {
            instance = new DebugLogger();
        }
        return instance;
    }
    
    function log(level: int, message: string, context: any = null): void {
        if (level >= this.logLevel) {
            let entry = {
                timestamp: new Date(),
                level: level,
                message: message,
                context: context
            };
            
            this.logFile.write(JSON.stringify(entry) + "\n");
        }
    }
}

// Usage
let logger = DebugLogger.getInstance();

logger.log(1, "Starting operation", { input: data });
try {
    // Operation
    logger.log(2, "Operation completed", { result: result });
} catch (error) {
    logger.log(0, "Operation failed", { error: error });
}
``` 