# Velox Advanced Type System

## Type System Features

### Union Types
```velox
// Union types allow a value to be one of several types
type StringOrNumber = string | number;
type Result<T> = Success<T> | Error;

function process(value: StringOrNumber): void {
    match (value) {
        case string => println("String: " + value);
        case number => println("Number: " + value);
    }
}
```

### Intersection Types
```velox
// Intersection types combine multiple types
type Employee = Person & Worker;
type Logger = ConsoleLogger & FileLogger;

class Person {
    name: string;
    age: number;
}

class Worker {
    employeeId: string;
    department: string;
}
```

### Generic Constraints
```velox
// Generic type constraints
interface Comparable<T> {
    function compareTo(other: T): number;
}

function sort<T extends Comparable<T>>(items: T[]): T[] {
    return items.sort((a, b) => a.compareTo(b));
}
```

### Type Inference
```velox
// Type inference for complex expressions
let numbers = [1, 2, 3].map(x => x * 2);  // Inferred as number[]
let mixed = [1, "hello", true];  // Inferred as (number | string | boolean)[]
```

### Type Guards
```velox
// Type guards for runtime type checking
function isString(value: any): value is string {
    return typeof value === "string";
}

function process(value: any): void {
    if (isString(value)) {
        // value is now typed as string
        println(value.toUpperCase());
    }
}
```

### Mapped Types
```velox
// Mapped types for transforming types
type Readonly<T> = {
    readonly [P in keyof T]: T[P];
};

type Optional<T> = {
    [P in keyof T]?: T[P];
};

type Nullable<T> = {
    [P in keyof T]: T[P] | null;
};
```

### Conditional Types
```velox
// Conditional types based on type relationships
type NonNullable<T> = T extends null | undefined ? never : T;
type Extract<T, U> = T extends U ? T : never;
type Exclude<T, U> = T extends U ? never : T;
```

### Type Aliases
```velox
// Type aliases for complex types
type Point = {
    x: number;
    y: number;
};

type Matrix = number[][];
type Callback<T> = (value: T) => void;
```

### Literal Types
```velox
// Literal types for exact values
type Direction = "north" | "south" | "east" | "west";
type Status = 200 | 404 | 500;
type Binary = 0 | 1;
```

### Template Literal Types
```velox
// Template literal types for string manipulation
type HttpMethod = "GET" | "POST" | "PUT" | "DELETE";
type ApiEndpoint = `/api/${string}`;
type EventName = `on${string}`;
```

### Recursive Types
```velox
// Recursive type definitions
type JsonValue = 
    | string
    | number
    | boolean
    | null
    | JsonValue[]
    | { [key: string]: JsonValue };

type Tree<T> = {
    value: T;
    children: Tree<T>[];
};
```

### Type Assertions
```velox
// Type assertions for type casting
let value: any = "hello";
let length: number = (value as string).length;
let num: number = <number>value;  // Alternative syntax
```

### Type Predicates
```velox
// Type predicates for custom type guards
interface Animal {
    name: string;
}

interface Dog extends Animal {
    breed: string;
}

function isDog(animal: Animal): animal is Dog {
    return "breed" in animal;
}
```

### Type Parameters
```velox
// Type parameters with constraints and defaults
class Container<T extends object = any> {
    private value: T;
    
    constructor(value: T) {
        this.value = value;
    }
    
    function getValue(): T {
        return this.value;
    }
}
```

### Type Utilities
```velox
// Built-in type utilities
type Partial<T> = { [P in keyof T]?: T[P] };
type Required<T> = { [P in keyof T]-?: T[P] };
type Pick<T, K extends keyof T> = { [P in K]: T[P] };
type Omit<T, K extends keyof T> = Pick<T, Exclude<keyof T, K>>;
type Record<K extends keyof any, T> = { [P in K]: T };
```

### Type Inference with Generics
```velox
// Advanced type inference with generics
function createTuple<T, U>(first: T, second: U): [T, U] {
    return [first, second];
}

let tuple = createTuple("hello", 42);  // Inferred as [string, number]
```

### Type-safe Event Handling
```velox
// Type-safe event handling
type EventMap = {
    "click": MouseEvent;
    "keypress": KeyboardEvent;
    "load": Event;
};

function addEventListener<K extends keyof EventMap>(
    type: K,
    handler: (event: EventMap[K]) => void
): void {
    // Implementation
}
```

### Type-safe API Calls
```velox
// Type-safe API calls
type ApiResponse<T> = {
    data: T;
    status: number;
    message: string;
};

async function fetchData<T>(url: string): Promise<ApiResponse<T>> {
    // Implementation
}
```

### Type-safe Database Operations
```velox
// Type-safe database operations
interface Database<T> {
    function find(id: string): Promise<T>;
    function create(data: Omit<T, "id">): Promise<T>;
    function update(id: string, data: Partial<T>): Promise<T>;
    function delete(id: string): Promise<void>;
}
```

### Type-safe Configuration
```velox
// Type-safe configuration
type Config = {
    database: {
        host: string;
        port: number;
        credentials: {
            username: string;
            password: string;
        };
    };
    server: {
        port: number;
        ssl: boolean;
    };
};

function validateConfig(config: Config): boolean {
    // Implementation
}
``` 