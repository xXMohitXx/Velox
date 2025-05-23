# Velox Quick Reference Guide

## Basic Syntax

### Variables and Constants
```velox
let name = "John";              // Variable declaration
const PI = 3.14159;            // Constant declaration
let age: int = 30;             // Explicit type
```

### Basic Types
```velox
let number: int = 42;          // Integer
let decimal: float = 3.14;     // Floating-point
let text: string = "Hello";    // String
let flag: bool = true;         // Boolean
let nothing: null = null;      // Null
```

### Arrays and Maps
```velox
let numbers: int[] = [1, 2, 3];                    // Array
let matrix: float[][] = [[1.0, 2.0], [3.0, 4.0]];  // 2D Array
let person: map<string, any> = {                    // Map
    "name": "John",
    "age": 30
};
```

## Control Flow

### Conditionals
```velox
if (age >= 18) {
    println("Adult");
} else if (age >= 13) {
    println("Teenager");
} else {
    println("Child");
}

switch (day) {
    case "Monday" -> println("Start of week");
    case "Friday" -> println("End of week");
    default -> println("Weekday");
}
```

### Loops
```velox
for (let i = 0; i < 5; i++) {
    println(i);
}

for (let item in items) {
    println(item);
}

while (count > 0) {
    println(count);
    count--;
}

do {
    println("At least once");
} while (condition);
```

## Functions

### Function Declaration
```velox
function greet(name: string): string {
    return "Hello, " + name + "!";
}

function calculate(a: float, b: float, op: string): float {
    return match (op) {
        case "add" => a + b;
        case "subtract" => a - b;
        case "multiply" => a * b;
        case "divide" => a / b;
        default => throw new Error("Invalid operation");
    };
}
```

### Lambda Functions
```velox
let square = (x: int): int => x * x;
let add = (a: int, b: int): int => a + b;
let isEven = (n: int): bool => n % 2 == 0;
```

## Classes

### Class Definition
```velox
class Person {
    private name: string;
    private age: int;
    
    constructor(name: string, age: int) {
        this.name = name;
        this.age = age;
    }
    
    function getName(): string {
        return this.name;
    }
    
    function getAge(): int {
        return this.age;
    }
}
```

### Inheritance
```velox
class Employee extends Person {
    private employeeId: string;
    
    constructor(name: string, age: int, employeeId: string) {
        super(name, age);
        this.employeeId = employeeId;
    }
    
    function getEmployeeId(): string {
        return this.employeeId;
    }
}
```

## Python Integration

### Basic Python Usage
```velox
import numpy as np;
import pandas as pd;

let array = np.array([1, 2, 3, 4, 5]);
let mean = np.mean(array);

let df = pd.DataFrame({
    "name": ["John", "Alice"],
    "age": [30, 25]
});
```

### Advanced Python Integration
```velox
import sklearn;
import tensorflow as tf;

let model = sklearn.ensemble.RandomForestClassifier();
model.fit(X_train, y_train);

let nn = tf.keras.Sequential([
    tf.keras.layers.Dense(64, activation="relu"),
    tf.keras.layers.Dense(32, activation="relu"),
    tf.keras.layers.Dense(1, activation="sigmoid")
]);
```

## Error Handling

### Try-Catch
```velox
try {
    let result = divide(a, b);
    println("Result: " + result);
} catch (DivisionByZeroError e) {
    println("Error: Cannot divide by zero");
} catch (Error e) {
    println("Unexpected error: " + e.getMessage());
} finally {
    println("Operation completed");
}
```

### Custom Exceptions
```velox
class ValidationError extends Error {
    constructor(message: string) {
        super(message);
    }
}

function validateAge(age: int): void {
    if (age < 0) {
        throw new ValidationError("Age cannot be negative");
    }
}
```

## Standard Library

### String Operations
```velox
let text = "Hello, World!";
let length = text.length();
let upper = text.toUpperCase();
let lower = text.toLowerCase();
let parts = text.split(",");
let contains = text.contains("World");
```

### Array Operations
```velox
let numbers = [1, 2, 3, 4, 5];
let doubled = numbers.map(x => x * 2);
let sum = numbers.reduce((a, b) => a + b);
let filtered = numbers.filter(x => x > 3);
let sorted = numbers.sort();
```

### Map Operations
```velox
let map = new map<string, int>();
map.set("one", 1);
map.set("two", 2);
let value = map.get("one");
let hasKey = map.has("two");
map.delete("one");
let size = map.size();
```

### File Operations
```velox
let file = new File("data.txt");
file.write("Hello, World!");
let content = file.read();
let lines = file.readLines();
file.append("New line");
```

### Date and Time
```velox
let now = new Date();
let year = now.getFullYear();
let month = now.getMonth();
let day = now.getDate();
let time = now.getTime();
```

## Command Line Tools

### Compiler
```bash
velox -c script.vlx          # Compile script
velox -o output.vbc script.vlx  # Specify output file
velox -O2 script.vlx         # Enable optimizations
```

### Interpreter
```bash
velox -i script.vlx          # Interpret script
velox -d script.vlx          # Debug mode
velox --profile script.vlx   # Profiling mode
```

### REPL
```bash
velox --repl                 # Start REPL
.help                        # Show help
.exit                        # Exit REPL
.clear                       # Clear screen
```

### Formatter
```bash
velox -f script.vlx          # Format script
velox -f -i script.vlx       # Format in-place
```

### Linter
```bash
velox -l script.vlx          # Lint script
velox -l --fix script.vlx    # Fix linting issues
``` 