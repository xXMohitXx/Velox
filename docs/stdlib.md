# Velox Standard Library API Documentation

## Core Types

### String
```velox
class String {
    // Constructors
    constructor(value: string);
    constructor(value: string, encoding: string);
    
    // Properties
    readonly length: int;
    
    // Methods
    function charAt(index: int): string;
    function charCodeAt(index: int): int;
    function concat(...strings: string[]): string;
    function contains(substring: string): bool;
    function endsWith(suffix: string): bool;
    function indexOf(substring: string, startIndex?: int): int;
    function lastIndexOf(substring: string, startIndex?: int): int;
    function padStart(length: int, padString?: string): string;
    function padEnd(length: int, padString?: string): string;
    function replace(search: string, replace: string): string;
    function replaceAll(search: string, replace: string): string;
    function slice(start: int, end?: int): string;
    function split(separator: string, limit?: int): string[];
    function startsWith(prefix: string): bool;
    function substring(start: int, end?: int): string;
    function toLowerCase(): string;
    function toUpperCase(): string;
    function trim(): string;
    function trimStart(): string;
    function trimEnd(): string;
}
```

### Array
```velox
class Array<T> {
    // Constructors
    constructor();
    constructor(length: int);
    constructor(...elements: T[]);
    
    // Properties
    readonly length: int;
    
    // Methods
    function push(...elements: T[]): int;
    function pop(): T;
    function shift(): T;
    function unshift(...elements: T[]): int;
    function slice(start?: int, end?: int): T[];
    function splice(start: int, deleteCount?: int, ...items: T[]): T[];
    function concat(...arrays: T[][]): T[];
    function join(separator?: string): string;
    function reverse(): T[];
    function sort(compareFn?: (a: T, b: T) => int): T[];
    
    // Iteration methods
    function forEach(callback: (element: T, index: int, array: T[]) => void): void;
    function map<U>(callback: (element: T, index: int, array: T[]) => U): U[];
    function filter(callback: (element: T, index: int, array: T[]) => bool): T[];
    function reduce<U>(callback: (accumulator: U, element: T, index: int, array: T[]) => U, initialValue: U): U;
    function reduceRight<U>(callback: (accumulator: U, element: T, index: int, array: T[]) => U, initialValue: U): U;
    function some(callback: (element: T, index: int, array: T[]) => bool): bool;
    function every(callback: (element: T, index: int, array: T[]) => bool): bool;
    function find(callback: (element: T, index: int, array: T[]) => bool): T;
    function findIndex(callback: (element: T, index: int, array: T[]) => bool): int;
    function includes(element: T, fromIndex?: int): bool;
    function indexOf(element: T, fromIndex?: int): int;
    function lastIndexOf(element: T, fromIndex?: int): int;
}
```

### Map
```velox
class Map<K, V> {
    // Constructors
    constructor();
    constructor(entries: [K, V][]);
    
    // Methods
    function set(key: K, value: V): void;
    function get(key: K): V;
    function has(key: K): bool;
    function delete(key: K): bool;
    function clear(): void;
    function size(): int;
    function keys(): K[];
    function values(): V[];
    function entries(): [K, V][];
    function forEach(callback: (value: V, key: K, map: Map<K, V>) => void): void;
}
```

### Set
```velox
class Set<T> {
    // Constructors
    constructor();
    constructor(values: T[]);
    
    // Methods
    function add(value: T): void;
    function delete(value: T): bool;
    function has(value: T): bool;
    function clear(): void;
    function size(): int;
    function values(): T[];
    function forEach(callback: (value: T, set: Set<T>) => void): void;
}
```

## File System

### File
```velox
class File {
    // Constructors
    constructor(path: string);
    constructor(path: string, mode: string);
    
    // Methods
    function read(): string;
    function readLines(): string[];
    function write(content: string): void;
    function append(content: string): void;
    function delete(): void;
    function exists(): bool;
    function size(): int;
    function lastModified(): Date;
    function copyTo(destination: string): void;
    function moveTo(destination: string): void;
    function createDirectory(): void;
    function listFiles(): string[];
    function isDirectory(): bool;
    function isFile(): bool;
}
```

### Directory
```velox
class Directory {
    // Constructors
    constructor(path: string);
    
    // Methods
    function create(): void;
    function delete(): void;
    function exists(): bool;
    function listFiles(): string[];
    function listDirectories(): string[];
    function copyTo(destination: string): void;
    function moveTo(destination: string): void;
    function size(): int;
    function lastModified(): Date;
}
```

## Date and Time

### Date
```velox
class Date {
    // Constructors
    constructor();
    constructor(timestamp: int);
    constructor(year: int, month: int, day: int);
    constructor(year: int, month: int, day: int, hour: int, minute: int, second: int);
    
    // Methods
    function getFullYear(): int;
    function getMonth(): int;
    function getDate(): int;
    function getDay(): int;
    function getHours(): int;
    function getMinutes(): int;
    function getSeconds(): int;
    function getMilliseconds(): int;
    function getTime(): int;
    function setFullYear(year: int): void;
    function setMonth(month: int): void;
    function setDate(date: int): void;
    function setHours(hours: int): void;
    function setMinutes(minutes: int): void;
    function setSeconds(seconds: int): void;
    function setMilliseconds(milliseconds: int): void;
    function setTime(timestamp: int): void;
    function toISOString(): string;
    function toLocaleString(): string;
    function toString(): string;
}
```

## Math

### Math
```velox
class Math {
    // Constants
    static readonly PI: float;
    static readonly E: float;
    
    // Methods
    static function abs(x: float): float;
    static function acos(x: float): float;
    static function asin(x: float): float;
    static function atan(x: float): float;
    static function atan2(y: float, x: float): float;
    static function ceil(x: float): float;
    static function cos(x: float): float;
    static function exp(x: float): float;
    static function floor(x: float): float;
    static function log(x: float): float;
    static function max(...values: float[]): float;
    static function min(...values: float[]): float;
    static function pow(x: float, y: float): float;
    static function random(): float;
    static function round(x: float): float;
    static function sin(x: float): float;
    static function sqrt(x: float): float;
    static function tan(x: float): float;
}
```

## Regular Expressions

### RegExp
```velox
class RegExp {
    // Constructors
    constructor(pattern: string);
    constructor(pattern: string, flags: string);
    
    // Properties
    readonly source: string;
    readonly flags: string;
    readonly global: bool;
    readonly ignoreCase: bool;
    readonly multiline: bool;
    
    // Methods
    function test(string: string): bool;
    function exec(string: string): string[];
    function match(string: string): string[];
    function matchAll(string: string): string[][];
    function replace(string: string, replacement: string): string;
    function replaceAll(string: string, replacement: string): string;
    function search(string: string): int;
    function split(string: string, limit?: int): string[];
}
```

## JSON

### JSON
```velox
class JSON {
    // Methods
    static function parse(text: string): any;
    static function stringify(value: any): string;
    static function stringify(value: any, replacer: (key: string, value: any) => any): string;
    static function stringify(value: any, replacer: (key: string, value: any) => any, space: int): string;
}
```

## HTTP

### HttpClient
```velox
class HttpClient {
    // Methods
    function get(url: string): Promise<HttpResponse>;
    function post(url: string, data: any): Promise<HttpResponse>;
    function put(url: string, data: any): Promise<HttpResponse>;
    function delete(url: string): Promise<HttpResponse>;
    function head(url: string): Promise<HttpResponse>;
    function options(url: string): Promise<HttpResponse>;
    function patch(url: string, data: any): Promise<HttpResponse>;
}

class HttpResponse {
    // Properties
    readonly status: int;
    readonly statusText: string;
    readonly headers: map<string, string>;
    readonly body: string;
    
    // Methods
    function json(): any;
    function text(): string;
    function blob(): Blob;
    function arrayBuffer(): ArrayBuffer;
}
```

## Logging

### Logger
```velox
class Logger {
    // Constructors
    constructor(name: string);
    
    // Methods
    function debug(message: string, ...args: any[]): void;
    function info(message: string, ...args: any[]): void;
    function warn(message: string, ...args: any[]): void;
    function error(message: string, ...args: any[]): void;
    function fatal(message: string, ...args: any[]): void;
    
    // Configuration
    function setLevel(level: LogLevel): void;
    function addHandler(handler: LogHandler): void;
    function removeHandler(handler: LogHandler): void;
}

enum LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL
}
```

## Testing

### Test
```velox
class Test {
    // Methods
    static function assert(condition: bool, message?: string): void;
    static function assertEquals(expected: any, actual: any, message?: string): void;
    static function assertNotEquals(expected: any, actual: any, message?: string): void;
    static function assertNull(value: any, message?: string): void;
    static function assertNotNull(value: any, message?: string): void;
    static function assertTrue(condition: bool, message?: string): void;
    static function assertFalse(condition: bool, message?: string): void;
    static function assertThrows(fn: () => void, errorType?: Error): void;
    static function assertDoesNotThrow(fn: () => void, message?: string): void;
}

class TestSuite {
    // Methods
    function addTest(name: string, test: () => void): void;
    function run(): TestResult;
    function skip(name: string): void;
    function only(name: string): void;
}
```

## Concurrency

### Promise
```velox
class Promise<T> {
    // Constructors
    constructor(executor: (resolve: (value: T) => void, reject: (reason: any) => void) => void);
    
    // Methods
    function then<U>(onFulfilled: (value: T) => U): Promise<U>;
    function catch<U>(onRejected: (reason: any) => U): Promise<U>;
    function finally(onFinally: () => void): Promise<T>;
    
    // Static methods
    static function all(promises: Promise<any>[]): Promise<any[]>;
    static function race(promises: Promise<any>[]): Promise<any>;
    static function resolve<T>(value: T): Promise<T>;
    static function reject<T>(reason: any): Promise<T>;
}
```

### Async
```velox
class Async {
    // Methods
    static function sleep(milliseconds: int): Promise<void>;
    static function timeout<T>(promise: Promise<T>, milliseconds: int): Promise<T>;
    static function retry<T>(fn: () => Promise<T>, options: RetryOptions): Promise<T>;
}

class RetryOptions {
    maxAttempts: int;
    delay: int;
    backoff: float;
    onRetry: (error: any, attempt: int) => void;
}
``` 