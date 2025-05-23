# Velox Syntax Specification

## Basic Syntax Rules

### 1. Program Structure
```velox
// Single-line comment
/* Multi-line
   comment */

// Module declaration
module example;

// Import statements
import std.io;
import std.math;

// Main function
fn main() {
    println("Hello, World!");
}
```

### 2. Variables and Types
```velox
// Variable declaration with type inference
let x = 42;
let name = "Velox";

// Explicit type declaration
let age: int = 25;
let price: float = 19.99;
let is_valid: bool = true;

// Constants
const PI = 3.14159;
```

### 3. Basic Types
- `int`: 64-bit integer
- `float`: 64-bit floating point
- `bool`: Boolean (true/false)
- `string`: UTF-8 encoded string
- `char`: Unicode character
- `void`: No value type

### 4. Control Flow
```velox
// If-else statements
if x > 0 {
    println("Positive");
} else if x < 0 {
    println("Negative");
} else {
    println("Zero");
}

// For loops
for i in 0..10 {
    println(i);
}

// While loops
while x > 0 {
    x -= 1;
}

// Match expressions (pattern matching)
match value {
    0 => println("Zero"),
    1..10 => println("Small"),
    _ => println("Large")
}
```

### 5. Functions
```velox
// Function declaration
fn add(a: int, b: int) -> int {
    return a + b;
}

// Lambda functions
let multiply = (a: int, b: int) -> int {
    return a * b;
};

// Function with default parameters
fn greet(name: string = "World") {
    println("Hello, {name}!");
}
```

### 6. Classes and Objects
```velox
// Class definition
class Person {
    // Properties
    let name: string;
    let age: int;

    // Constructor
    fn new(name: string, age: int) {
        this.name = name;
        this.age = age;
    }

    // Methods
    fn greet() {
        println("Hello, I'm {this.name}");
    }
}

// Object creation
let person = Person.new("Alice", 30);
```

### 7. Error Handling
```velox
// Try-catch blocks
try {
    let result = divide(a, b);
} catch DivisionByZero {
    println("Cannot divide by zero");
} catch e: Error {
    println("An error occurred: {e.message}");
}

// Result type
fn divide(a: float, b: float) -> Result<float, Error> {
    if b == 0 {
        return Error("Division by zero");
    }
    return a / b;
}
```

### 8. Concurrency
```velox
// Async functions
async fn fetch_data() -> string {
    // Async operations
    return await http.get("https://api.example.com");
}

// Spawn tasks
let task = spawn {
    // Concurrent code
    process_data();
};

// Wait for completion
await task;
```

### 9. Modules and Packages
```velox
// Module declaration
module my_package;

// Public exports
pub fn public_function() {
    // ...
}

// Private function
fn private_function() {
    // ...
}
```

### 10. Standard Library Usage
```velox
import std.io;
import std.collections;
import std.time;

// File operations
let file = File.open("data.txt");
let content = file.read_all();

// Collections
let list = List<int>.new();
list.add(1);
list.add(2);

// Time operations
let now = Time.now();
```

## Syntax Conventions

1. **Naming Conventions**
   - Variables and functions: camelCase
   - Classes and types: PascalCase
   - Constants: UPPER_SNAKE_CASE
   - Private members: _prefix

2. **Formatting**
   - 4 spaces for indentation
   - Curly braces on new lines
   - Spaces around operators
   - No semicolons at line ends

3. **Documentation**
   - Function documentation using /// comments
   - Module documentation using //! comments
   - Inline documentation using // comments

## Best Practices

1. **Code Organization**
   - One class per file
   - Related functions grouped together
   - Clear module hierarchy
   - Logical file structure

2. **Error Handling**
   - Use Result type for operations that can fail
   - Proper error propagation
   - Meaningful error messages
   - Appropriate error types

3. **Performance**
   - Use appropriate data structures
   - Minimize memory allocations
   - Profile critical code paths
   - Use async/await for I/O operations 