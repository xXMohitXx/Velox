# Velox Pattern Matching and Functional Programming

## Pattern Matching

### Basic Pattern Matching
```velox
function processValue(value: any): string {
    return match (value) {
        case string => "String: " + value;
        case number => "Number: " + value;
        case boolean => "Boolean: " + value;
        case null => "Null";
        case undefined => "Undefined";
        case [] => "Empty array";
        case [first, ...rest] => "Array starting with: " + first;
        case { type: "user", name } => "User: " + name;
        case { type: "admin", name, role } => "Admin: " + name + ", Role: " + role;
        case _ => "Unknown type";
    };
}
```

### Destructuring Patterns
```velox
function processUser(user: User): string {
    let { name, age, address: { city, country } } = user;
    return `${name} lives in ${city}, ${country}`;
}

function processCoordinates([x, y, z = 0]: [number, number, number?]): string {
    return `Point at (${x}, ${y}, ${z})`;
}
```

### Guard Clauses
```velox
function processNumber(n: number): string {
    return match (n) {
        case n if n < 0 => "Negative number";
        case n if n === 0 => "Zero";
        case n if n > 0 && n < 10 => "Single digit";
        case n if n >= 10 => "Double digit or more";
    };
}
```

### Nested Patterns
```velox
function processData(data: any): string {
    return match (data) {
        case { type: "user", profile: { name, age } } => 
            `User ${name} is ${age} years old`;
        case { type: "post", author: { name }, content } =>
            `Post by ${name}: ${content}`;
        case { type: "comment", post: { id }, text } =>
            `Comment on post ${id}: ${text}`;
    };
}
```

### Pattern Matching with Types
```velox
type Result<T> = Success<T> | Error;

function handleResult<T>(result: Result<T>): string {
    return match (result) {
        case Success(value) => "Success: " + value;
        case Error(message) => "Error: " + message;
    };
}
```

## Functional Programming

### Higher-Order Functions
```velox
function compose<T>(...fns: ((x: T) => T)[]): (x: T) => T {
    return (x: T) => fns.reduceRight((y, f) => f(y), x);
}

function pipe<T>(...fns: ((x: T) => T)[]): (x: T) => T {
    return (x: T) => fns.reduce((y, f) => f(y), x);
}

// Example usage
let process = pipe(
    (x: number) => x * 2,
    (x: number) => x + 1,
    (x: number) => x.toString()
);
```

### Currying
```velox
function curry<T, U, V>(fn: (x: T, y: U) => V): (x: T) => (y: U) => V {
    return (x: T) => (y: U) => fn(x, y);
}

// Example usage
let add = curry((a: number, b: number) => a + b);
let addFive = add(5);
let result = addFive(3);  // 8
```

### Partial Application
```velox
function partial<T, U, V>(fn: (x: T, y: U) => V, x: T): (y: U) => V {
    return (y: U) => fn(x, y);
}

// Example usage
let multiply = (a: number, b: number) => a * b;
let double = partial(multiply, 2);
let result = double(4);  // 8
```

### Function Composition
```velox
class Function<T, U> {
    private fn: (x: T) => U;

    constructor(fn: (x: T) => U) {
        this.fn = fn;
    }

    function apply(x: T): U {
        return this.fn(x);
    }

    function compose<V>(g: (y: U) => V): Function<T, V> {
        return new Function(x => g(this.fn(x)));
    }

    function andThen<V>(g: (y: U) => V): Function<T, V> {
        return this.compose(g);
    }
}
```

### Monads
```velox
class Maybe<T> {
    private value: T | null;

    private constructor(value: T | null) {
        this.value = value;
    }

    static function just<T>(value: T): Maybe<T> {
        return new Maybe(value);
    }

    static function nothing<T>(): Maybe<T> {
        return new Maybe<T>(null);
    }

    function map<U>(fn: (x: T) => U): Maybe<U> {
        return this.value === null 
            ? Maybe.nothing<U>()
            : Maybe.just(fn(this.value));
    }

    function flatMap<U>(fn: (x: T) => Maybe<U>): Maybe<U> {
        return this.value === null 
            ? Maybe.nothing<U>()
            : fn(this.value);
    }
}

class Either<L, R> {
    private left: L | null;
    private right: R | null;

    private constructor(left: L | null, right: R | null) {
        this.left = left;
        this.right = right;
    }

    static function left<L, R>(value: L): Either<L, R> {
        return new Either(value, null);
    }

    static function right<L, R>(value: R): Either<L, R> {
        return new Either(null, value);
    }

    function map<U>(fn: (x: R) => U): Either<L, U> {
        return this.right === null
            ? Either.left<L, U>(this.left!)
            : Either.right<L, U>(fn(this.right));
    }

    function flatMap<U>(fn: (x: R) => Either<L, U>): Either<L, U> {
        return this.right === null
            ? Either.left<L, U>(this.left!)
            : fn(this.right);
    }
}
```

### Functors
```velox
interface Functor<T> {
    function map<U>(fn: (x: T) => U): Functor<U>;
}

class List<T> implements Functor<T> {
    private items: T[];

    constructor(items: T[]) {
        this.items = items;
    }

    function map<U>(fn: (x: T) => U): List<U> {
        return new List(this.items.map(fn));
    }
}

class Tree<T> implements Functor<T> {
    private value: T;
    private children: Tree<T>[];

    constructor(value: T, children: Tree<T>[] = []) {
        this.value = value;
        this.children = children;
    }

    function map<U>(fn: (x: T) => U): Tree<U> {
        return new Tree(
            fn(this.value),
            this.children.map(child => child.map(fn))
        );
    }
}
```

### Applicative Functors
```velox
interface Applicative<T> extends Functor<T> {
    static function pure<U>(value: U): Applicative<U>;
    function ap<U>(fn: Applicative<(x: T) => U>): Applicative<U>;
}

class Maybe<T> implements Applicative<T> {
    // ... existing implementation ...

    static function pure<U>(value: U): Maybe<U> {
        return Maybe.just(value);
    }

    function ap<U>(fn: Maybe<(x: T) => U>): Maybe<U> {
        return this.value === null || fn.value === null
            ? Maybe.nothing<U>()
            : Maybe.just(fn.value(this.value));
    }
}
```

### Monoids
```velox
interface Monoid<T> {
    static function empty(): T;
    function combine(other: T): T;
}

class Sum implements Monoid<number> {
    private value: number;

    constructor(value: number) {
        this.value = value;
    }

    static function empty(): Sum {
        return new Sum(0);
    }

    function combine(other: Sum): Sum {
        return new Sum(this.value + other.value);
    }
}

class Product implements Monoid<number> {
    private value: number;

    constructor(value: number) {
        this.value = value;
    }

    static function empty(): Product {
        return new Product(1);
    }

    function combine(other: Product): Product {
        return new Product(this.value * other.value);
    }
}
```

### Lazy Evaluation
```velox
class Lazy<T> {
    private thunk: () => T;
    private value: T | null = null;

    constructor(thunk: () => T) {
        this.thunk = thunk;
    }

    function force(): T {
        if (this.value === null) {
            this.value = this.thunk();
        }
        return this.value;
    }

    function map<U>(fn: (x: T) => U): Lazy<U> {
        return new Lazy(() => fn(this.force()));
    }
}

// Example usage
let lazyValue = new Lazy(() => {
    println("Computing value...");
    return 42;
});

let result = lazyValue.force();  // Prints "Computing value..."
let result2 = lazyValue.force(); // No output, returns cached value
```

### Immutable Data Structures
```velox
class ImmutableList<T> {
    private items: T[];

    constructor(items: T[] = []) {
        this.items = [...items];
    }

    function push(item: T): ImmutableList<T> {
        return new ImmutableList([...this.items, item]);
    }

    function pop(): [ImmutableList<T>, T | undefined] {
        let newItems = [...this.items];
        let item = newItems.pop();
        return [new ImmutableList(newItems), item];
    }

    function map<U>(fn: (x: T) => U): ImmutableList<U> {
        return new ImmutableList(this.items.map(fn));
    }
}

class ImmutableMap<K, V> {
    private map: Map<K, V>;

    constructor(entries: [K, V][] = []) {
        this.map = new Map(entries);
    }

    function set(key: K, value: V): ImmutableMap<K, V> {
        let newMap = new Map(this.map);
        newMap.set(key, value);
        return new ImmutableMap([...newMap.entries()]);
    }

    function delete(key: K): ImmutableMap<K, V> {
        let newMap = new Map(this.map);
        newMap.delete(key);
        return new ImmutableMap([...newMap.entries()]);
    }
}
```

### Type Classes
```velox
interface Eq<T> {
    function equals(other: T): boolean;
}

interface Ord<T> extends Eq<T> {
    function compare(other: T): number;
}

interface Show<T> {
    function show(): string;
}

class Person implements Eq<Person>, Ord<Person>, Show<Person> {
    private name: string;
    private age: number;

    constructor(name: string, age: number) {
        this.name = name;
        this.age = age;
    }

    function equals(other: Person): boolean {
        return this.name === other.name && this.age === other.age;
    }

    function compare(other: Person): number {
        if (this.age < other.age) return -1;
        if (this.age > other.age) return 1;
        return this.name.localeCompare(other.name);
    }

    function show(): string {
        return `${this.name} (${this.age})`;
    }
}
``` 