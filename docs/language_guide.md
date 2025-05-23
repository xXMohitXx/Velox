# Velox Language Guide

## Table of Contents
1. [Basic Syntax](#basic-syntax)
2. [Data Types](#data-types)
3. [Control Flow](#control-flow)
4. [Functions](#functions)
5. [Classes and Objects](#classes-and-objects)
6. [Modules and Imports](#modules-and-imports)
7. [Python Interoperability](#python-interoperability)
8. [Error Handling](#error-handling)
9. [Advanced Features](#advanced-features)

## Basic Syntax

### Variables and Constants

```velox
// Variable declaration with type inference
let name = "John";
let age = 30;
let isActive = true;

// Explicit type declaration
let score: float = 95.5;
let items: string[] = ["apple", "banana", "orange"];

// Constants
const PI = 3.14159;
const MAX_SIZE = 100;
```

### Basic Operations

```velox
// Arithmetic operations
let sum = 5 + 3;
let difference = 10 - 4;
let product = 6 * 7;
let quotient = 20 / 4;
let remainder = 17 % 5;

// String operations
let firstName = "John";
let lastName = "Doe";
let fullName = firstName + " " + lastName;
let greeting = "Hello, ${fullName}!";

// Comparison operators
let isEqual = a == b;
let isNotEqual = a != b;
let isGreater = a > b;
let isLessOrEqual = a <= b;
```

## Data Types

### Primitive Types

```velox
// Integer types
let small: int8 = 127;
let medium: int16 = 32767;
let large: int32 = 2147483647;
let huge: int64 = 9223372036854775807;

// Floating-point types
let float32: float = 3.14159;
let float64: double = 3.141592653589793;

// Boolean
let isTrue: bool = true;
let isFalse: bool = false;

// String
let text: string = "Hello, World!";
```

### Complex Types

```velox
// Arrays
let numbers: int[] = [1, 2, 3, 4, 5];
let matrix: float[][] = [[1.0, 2.0], [3.0, 4.0]];

// Maps
let person: map<string, any> = {
    "name": "John",
    "age": 30,
    "scores": [85, 90, 95]
};

// Sets
let uniqueNumbers: set<int> = {1, 2, 3, 4, 5};
```

## Control Flow

### Conditional Statements

```velox
// If-else
if (age >= 18) {
    println("Adult");
} else if (age >= 13) {
    println("Teenager");
} else {
    println("Child");
}

// Switch statement
switch (day) {
    case "Monday" -> println("Start of week");
    case "Friday" -> println("End of week");
    case "Saturday", "Sunday" -> println("Weekend");
    default -> println("Weekday");
}
```

### Loops

```velox
// For loop
for (let i = 0; i < 5; i++) {
    println(i);
}

// For-each loop
for (let item in items) {
    println(item);
}

// While loop
while (count > 0) {
    println(count);
    count--;
}

// Do-while loop
do {
    println("At least once");
} while (condition);
```

## Functions

### Function Declaration

```velox
// Basic function
function greet(name: string): string {
    return "Hello, " + name + "!";
}

// Function with multiple parameters
function calculate(a: float, b: float, operation: string): float {
    switch (operation) {
        case "add" -> return a + b;
        case "subtract" -> return a - b;
        case "multiply" -> return a * b;
        case "divide" -> return a / b;
        default -> throw new Error("Invalid operation");
    }
}

// Function with default parameters
function createUser(name: string, age: int = 18, isActive: bool = true): User {
    return new User(name, age, isActive);
}
```

### Lambda Functions

```velox
// Simple lambda
let square = (x: int): int => x * x;

// Lambda with multiple parameters
let add = (a: int, b: int): int => a + b;

// Lambda in array operations
let numbers = [1, 2, 3, 4, 5];
let squares = numbers.map(x => x * x);
let sum = numbers.reduce((a, b) => a + b);
```

## Classes and Objects

### Class Definition

```velox
class Person {
    // Properties
    private name: string;
    private age: int;
    private address: Address;

    // Constructor
    constructor(name: string, age: int, address: Address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    // Methods
    function getName(): string {
        return this.name;
    }

    function getAge(): int {
        return this.age;
    }

    function updateAddress(newAddress: Address): void {
        this.address = newAddress;
    }

    // Static method
    static function createAdult(name: string, address: Address): Person {
        return new Person(name, 18, address);
    }
}
```

### Inheritance

```velox
class Employee extends Person {
    private employeeId: string;
    private department: string;
    private salary: float;

    constructor(
        name: string,
        age: int,
        address: Address,
        employeeId: string,
        department: string,
        salary: float
    ) {
        super(name, age, address);
        this.employeeId = employeeId;
        this.department = department;
        this.salary = salary;
    }

    function getSalary(): float {
        return this.salary;
    }

    function giveRaise(percentage: float): void {
        this.salary *= (1 + percentage / 100);
    }
}
```

## Python Interoperability

### Basic Python Integration

```velox
// Import Python modules
import numpy as np;
import pandas as pd;
import matplotlib.pyplot as plt;

// Create NumPy array
let array = np.array([1, 2, 3, 4, 5]);
let matrix = np.array([[1, 2], [3, 4]]);

// Perform operations
let mean = np.mean(array);
let std = np.std(array);
let dotProduct = np.dot(matrix, matrix);

// Create Pandas DataFrame
let data = {
    "name": ["John", "Alice", "Bob"],
    "age": [30, 25, 35],
    "salary": [50000, 60000, 70000]
};
let df = pd.DataFrame(data);

// Data analysis
let summary = df.describe();
let ageStats = df["age"].mean();
let salaryByAge = df.groupby("age")["salary"].mean();

// Create plot
plt.figure(figsize=(10, 6));
plt.scatter(df["age"], df["salary"]);
plt.title("Age vs Salary");
plt.xlabel("Age");
plt.ylabel("Salary");
plt.show();
```

### Advanced Python Integration

```velox
// Machine Learning with scikit-learn
import sklearn;
import sklearn.model_selection;
import sklearn.ensemble;

// Load and prepare data
let X = np.array([[1, 2], [3, 4], [5, 6]]);
let y = np.array([0, 1, 0]);

// Split data
let (X_train, X_test, y_train, y_test) = sklearn.model_selection.train_test_split(
    X, y, test_size=0.2
);

// Train model
let model = sklearn.ensemble.RandomForestClassifier();
model.fit(X_train, y_train);

// Make predictions
let predictions = model.predict(X_test);
let score = model.score(X_test, y_test);

// Deep Learning with TensorFlow
import tensorflow as tf;

// Create model
let model = tf.keras.Sequential([
    tf.keras.layers.Dense(64, activation="relu"),
    tf.keras.layers.Dropout(0.2),
    tf.keras.layers.Dense(32, activation="relu"),
    tf.keras.layers.Dense(1, activation="sigmoid")
]);

// Compile and train
model.compile(
    optimizer="adam",
    loss="binary_crossentropy",
    metrics=["accuracy"]
);

let history = model.fit(
    X_train, y_train,
    epochs=10,
    batch_size=32,
    validation_split=0.2
);
```

## Error Handling

### Try-Catch Blocks

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
    if (age > 150) {
        throw new ValidationError("Age seems invalid");
    }
}
```

## Advanced Features

### Generics

```velox
class Container<T> {
    private value: T;

    constructor(value: T) {
        this.value = value;
    }

    function getValue(): T {
        return this.value;
    }

    function setValue(value: T): void {
        this.value = value;
    }
}

// Usage
let numberContainer = new Container<int>(42);
let stringContainer = new Container<string>("Hello");
```

### Async/Await

```velox
async function fetchData(url: string): Promise<any> {
    try {
        let response = await http.get(url);
        return response.json();
    } catch (error) {
        println("Error fetching data: " + error.getMessage());
        throw error;
    }
}

// Usage
async function main() {
    let data = await fetchData("https://api.example.com/data");
    println(data);
}
```

### Pattern Matching

```velox
function processValue(value: any): string {
    return match (value) {
        case string s => "String: " + s;
        case int i => "Integer: " + i;
        case float f => "Float: " + f;
        case bool b => "Boolean: " + b;
        case null => "Null value";
        default => "Unknown type";
    };
}
``` 