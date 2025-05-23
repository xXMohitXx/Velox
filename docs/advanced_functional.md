# Advanced Functional Programming in Velox

## Advanced Functional Concepts

### Free Monads
```velox
// Free monad for building domain-specific languages
class Free<F, A> {
    private value: F | A;

    private constructor(value: F | A) {
        this.value = value;
    }

    static function pure<F, A>(value: A): Free<F, A> {
        return new Free(value);
    }

    static function liftF<F, A>(fa: F): Free<F, A> {
        return new Free(fa);
    }

    function flatMap<B>(f: (a: A) => Free<F, B>): Free<F, B> {
        return match (this.value) {
            case F => new Free(this.value);
            case A => f(this.value);
        };
    }
}

// Example: Database DSL
type DatabaseF<A> = 
    | Query<A>
    | Insert<A>
    | Delete<A>;

class Query<A> {
    constructor(public query: string, public next: (result: any) => A) {}
}

class Insert<A> {
    constructor(public data: any, public next: (id: string) => A) {}
}

class Delete<A> {
    constructor(public id: string, public next: (success: boolean) => A) {}
}

// Usage
let program = Free.liftF(new Query("SELECT * FROM users", 
    users => Free.liftF(new Insert({ name: "John" }, 
        id => Free.pure(id)))));
```

### Comonads
```velox
interface Comonad<W> {
    function extract(): any;
    function extend<A, B>(f: (wa: W<A>) => B): W<B>;
}

class Stream<T> implements Comonad<Stream> {
    private values: T[];
    private index: number;

    constructor(values: T[], index: number = 0) {
        this.values = values;
        this.index = index;
    }

    function extract(): T {
        return this.values[this.index];
    }

    function extend<A, B>(f: (wa: Stream<A>) => B): Stream<B> {
        return new Stream(
            this.values.map((_, i) => f(new Stream(this.values, i))),
            this.index
        );
    }
}

// Example: Moving average
function movingAverage(window: number): (stream: Stream<number>) => number {
    return stream => {
        let start = Math.max(0, stream.index - window + 1);
        let end = stream.index + 1;
        let sum = 0;
        for (let i = start; i < end; i++) {
            sum += stream.values[i];
        }
        return sum / (end - start);
    };
}
```

### Optics (Lenses, Prisms, and Traversals)
```velox
class Lens<S, A> {
    constructor(
        private get: (s: S) => A,
        private set: (s: S, a: A) => S
    ) {}

    function view(s: S): A {
        return this.get(s);
    }

    function set(s: S, a: A): S {
        return this.set(s, a);
    }

    function modify(s: S, f: (a: A) => A): S {
        return this.set(s, f(this.get(s)));
    }

    function compose<B>(other: Lens<A, B>): Lens<S, B> {
        return new Lens(
            s => other.get(this.get(s)),
            (s, b) => this.set(s, other.set(this.get(s), b))
        );
    }
}

// Example usage
class Address {
    constructor(
        public street: string,
        public city: string,
        public country: string
    ) {}
}

class Person {
    constructor(
        public name: string,
        public address: Address
    ) {}
}

let addressLens = new Lens<Person, Address>(
    p => p.address,
    (p, a) => new Person(p.name, a)
);

let streetLens = new Lens<Address, string>(
    a => a.street,
    (a, s) => new Address(s, a.city, a.country)
);

let personStreetLens = addressLens.compose(streetLens);
```

### Tagless Final
```velox
interface Monad<M> {
    function pure<A>(a: A): M<A>;
    function flatMap<A, B>(ma: M<A>, f: (a: A) => M<B>): M<B>;
}

interface Console<F> {
    function readLine(): F<string>;
    function writeLine(line: string): F<void>;
}

interface Database<F> {
    function query<T>(sql: string): F<T[]>;
    function execute(sql: string): F<void>;
}

class Program<F> {
    constructor(
        private M: Monad<F>,
        private console: Console<F>,
        private db: Database<F>
    ) {}

    function run(): F<void> {
        return this.M.flatMap(
            this.console.readLine(),
            name => this.M.flatMap(
                this.db.query(`SELECT * FROM users WHERE name = '${name}'`),
                users => this.console.writeLine(`Found ${users.length} users`)
            )
        );
    }
}
```

## Real-World Examples

### Data Processing Pipeline
```velox
// Type-safe ETL pipeline
class Pipeline<T, R> {
    private steps: ((data: any) => Promise<any>)[] = [];

    function addStep<U>(step: (data: T) => Promise<U>): Pipeline<U, R> {
        this.steps.push(step);
        return this as Pipeline<U, R>;
    }

    async function execute(data: T): Promise<R> {
        return this.steps.reduce(
            async (acc, step) => step(await acc),
            Promise.resolve(data)
        );
    }
}

// Example usage
let pipeline = new Pipeline<string, Report>()
    .addStep(async (data) => {
        // Parse CSV
        return data.split("\n").map(line => line.split(","));
    })
    .addStep(async (rows) => {
        // Transform data
        return rows.map(([name, age, score]) => ({
            name,
            age: parseInt(age),
            score: parseFloat(score)
        }));
    })
    .addStep(async (records) => {
        // Aggregate results
        return {
            total: records.length,
            averageScore: records.reduce((sum, r) => sum + r.score, 0) / records.length,
            byAge: records.reduce((acc, r) => {
                acc[r.age] = (acc[r.age] || 0) + 1;
                return acc;
            }, {})
        };
    });
```

### Event Sourcing
```velox
// Event sourcing implementation
interface Event {
    type: string;
    timestamp: number;
}

class EventStore {
    private events: Event[] = [];

    function append(event: Event): void {
        this.events.push(event);
    }

    function getEvents(): Event[] {
        return [...this.events];
    }
}

class Aggregate<T> {
    private state: T;

    constructor(initialState: T) {
        this.state = initialState;
    }

    function apply(event: Event): void {
        this.state = this.reducer(this.state, event);
    }

    protected reducer(state: T, event: Event): T {
        return match (event) {
            case { type: "Created", data } => 
                { ...state, ...data };
            case { type: "Updated", data } =>
                { ...state, ...data };
            case { type: "Deleted" } =>
                null;
            case _ => state;
        };
    }
}

// Example usage
class UserAggregate extends Aggregate<User> {
    constructor() {
        super({ name: "", email: "" });
    }

    function createUser(name: string, email: string): void {
        this.apply({
            type: "Created",
            timestamp: Date.now(),
            data: { name, email }
        });
    }

    function updateEmail(email: string): void {
        this.apply({
            type: "Updated",
            timestamp: Date.now(),
            data: { email }
        });
    }
}
```

### CQRS Pattern
```velox
// Command Query Responsibility Segregation
interface Command {
    type: string;
    data: any;
}

interface Query {
    type: string;
    params: any;
}

class CommandBus {
    private handlers: Map<string, (cmd: Command) => Promise<void>> = new Map();

    function register(type: string, handler: (cmd: Command) => Promise<void>): void {
        this.handlers.set(type, handler);
    }

    async function dispatch(cmd: Command): Promise<void> {
        let handler = this.handlers.get(cmd.type);
        if (!handler) {
            throw new Error(`No handler for command type: ${cmd.type}`);
        }
        await handler(cmd);
    }
}

class QueryBus {
    private handlers: Map<string, (query: Query) => Promise<any>> = new Map();

    function register(type: string, handler: (query: Query) => Promise<any>): void {
        this.handlers.set(type, handler);
    }

    async function dispatch(query: Query): Promise<any> {
        let handler = this.handlers.get(query.type);
        if (!handler) {
            throw new Error(`No handler for query type: ${query.type}`);
        }
        return await handler(query);
    }
}

// Example usage
let commandBus = new CommandBus();
let queryBus = new QueryBus();

commandBus.register("CreateUser", async (cmd) => {
    // Handle user creation
});

queryBus.register("GetUser", async (query) => {
    // Handle user retrieval
});
```

## Performance Considerations

### Memory Management
```velox
// Efficient memory usage with lazy evaluation
class LazyList<T> {
    private head: T | null = null;
    private tail: Lazy<LazyList<T>> | null = null;

    constructor(head: T | null = null, tail: Lazy<LazyList<T>> | null = null) {
        this.head = head;
        this.tail = tail;
    }

    function map<U>(f: (x: T) => U): LazyList<U> {
        if (this.head === null) {
            return new LazyList<U>();
        }
        return new LazyList<U>(
            f(this.head),
            new Lazy(() => this.tail?.force().map(f) ?? new LazyList<U>())
        );
    }

    function take(n: number): LazyList<T> {
        if (n <= 0 || this.head === null) {
            return new LazyList<T>();
        }
        return new LazyList<T>(
            this.head,
            new Lazy(() => this.tail?.force().take(n - 1) ?? new LazyList<T>())
        );
    }
}
```

### Parallel Processing
```velox
// Efficient parallel processing
class ParallelProcessor<T, R> {
    constructor(
        private items: T[],
        private processor: (item: T) => Promise<R>,
        private concurrency: number
    ) {}

    async function process(): Promise<R[]> {
        let results: R[] = [];
        let running = 0;
        let index = 0;

        async function runNext(): Promise<void> {
            if (index >= this.items.length) return;
            
            running++;
            let item = this.items[index++];
            
            try {
                let result = await this.processor(item);
                results.push(result);
            } finally {
                running--;
                if (running < this.concurrency) {
                    await runNext();
                }
            }
        }

        let promises = Array(Math.min(this.concurrency, this.items.length))
            .fill(null)
            .map(() => runNext());

        await Promise.all(promises);
        return results;
    }
}
```

### Caching Strategies
```velox
// Memoization with cache invalidation
class MemoizedFunction<T, R> {
    private cache: Map<string, { value: R, timestamp: number }> = new Map();
    private ttl: number;

    constructor(
        private fn: (arg: T) => Promise<R>,
        ttl: number = 60000 // 1 minute
    ) {
        this.ttl = ttl;
    }

    async function call(arg: T): Promise<R> {
        let key = JSON.stringify(arg);
        let cached = this.cache.get(key);

        if (cached && Date.now() - cached.timestamp < this.ttl) {
            return cached.value;
        }

        let result = await this.fn(arg);
        this.cache.set(key, { value: result, timestamp: Date.now() });
        return result;
    }

    function clear(): void {
        this.cache.clear();
    }
}
```

## Best Practices

1. **Immutability**
   - Use immutable data structures
   - Avoid side effects
   - Use pure functions

2. **Error Handling**
   - Use Either monad for error handling
   - Implement proper error recovery
   - Log errors appropriately

3. **Performance**
   - Use lazy evaluation for large collections
   - Implement proper caching strategies
   - Use parallel processing when appropriate

4. **Testing**
   - Write property-based tests
   - Test edge cases
   - Use test doubles effectively

5. **Documentation**
   - Document function signatures
   - Provide usage examples
   - Explain performance characteristics 