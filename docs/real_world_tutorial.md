# Real-World Functional Programming Tutorial

## Building a Data Processing System

This tutorial demonstrates how to build a robust data processing system using Velox's functional programming features. We'll create a system that processes user activity data, applies transformations, and generates reports.

### 1. Setting Up the Domain Model

```velox
// Define our domain types
type UserId = string;
type Timestamp = number;
type ActivityType = "login" | "logout" | "purchase" | "view";

interface UserActivity {
    userId: UserId;
    type: ActivityType;
    timestamp: Timestamp;
    metadata: Map<string, any>;
}

interface UserProfile {
    userId: UserId;
    name: string;
    email: string;
    preferences: Map<string, any>;
}

interface ActivityReport {
    userId: UserId;
    totalActivities: number;
    activityTypes: Map<ActivityType, number>;
    lastActivity: Timestamp;
    metadata: Map<string, any>;
}
```

### 2. Creating the Processing Pipeline

```velox
// Define our processing pipeline
class ActivityProcessor {
    private pipeline: Pipeline<UserActivity, ActivityReport>;

    constructor() {
        this.pipeline = new Pipeline<UserActivity, ActivityReport>()
            .addStep(this.validateActivity)
            .addStep(this.enrichWithProfile)
            .addStep(this.aggregateActivities)
            .addStep(this.generateReport);
    }

    private async function validateActivity(
        activity: UserActivity
    ): Promise<Either<Error, UserActivity>> {
        return match (activity) {
            case { userId: "", type, timestamp } => 
                Either.left(new Error("Invalid user ID"));
            case { userId, type: "", timestamp } =>
                Either.left(new Error("Invalid activity type"));
            case { userId, type, timestamp: 0 } =>
                Either.left(new Error("Invalid timestamp"));
            case activity => Either.right(activity);
        };
    }

    private async function enrichWithProfile(
        activity: UserActivity
    ): Promise<Either<Error, [UserActivity, UserProfile]>> {
        try {
            let profile = await this.fetchUserProfile(activity.userId);
            return Either.right([activity, profile]);
        } catch (error) {
            return Either.left(error);
        }
    }

    private async function aggregateActivities(
        [activity, profile]: [UserActivity, UserProfile]
    ): Promise<Either<Error, ActivityReport>> {
        try {
            let existingReport = await this.fetchActivityReport(activity.userId);
            let updatedReport = this.updateReport(existingReport, activity);
            return Either.right(updatedReport);
        } catch (error) {
            return Either.left(error);
        }
    }

    private async function generateReport(
        report: ActivityReport
    ): Promise<Either<Error, ActivityReport>> {
        try {
            await this.saveReport(report);
            return Either.right(report);
        } catch (error) {
            return Either.left(error);
        }
    }

    async function processActivity(
        activity: UserActivity
    ): Promise<Either<Error, ActivityReport>> {
        return this.pipeline.execute(activity);
    }
}
```

### 3. Implementing Event Sourcing

```velox
// Define our events
type ActivityEvent = 
    | ActivityCreated
    | ActivityValidated
    | ActivityEnriched
    | ActivityAggregated
    | ReportGenerated;

interface ActivityCreated {
    type: "ActivityCreated";
    activity: UserActivity;
    timestamp: Timestamp;
}

interface ActivityValidated {
    type: "ActivityValidated";
    activity: UserActivity;
    timestamp: Timestamp;
}

// Event store implementation
class ActivityEventStore {
    private events: ActivityEvent[] = [];
    private eventBus: EventBus<ActivityEvent>;

    constructor(eventBus: EventBus<ActivityEvent>) {
        this.eventBus = eventBus;
    }

    async function append(event: ActivityEvent): Promise<void> {
        this.events.push(event);
        await this.eventBus.publish(event);
    }

    function getEvents(): ActivityEvent[] {
        return [...this.events];
    }

    function getEventsByUserId(userId: UserId): ActivityEvent[] {
        return this.events.filter(event => 
            event.activity?.userId === userId
        );
    }
}
```

### 4. Adding Caching and Performance Optimization

```velox
// Implement caching for user profiles
class CachedUserProfileService {
    private cache: MemoizedFunction<UserId, UserProfile>;
    private db: Database;

    constructor(db: Database) {
        this.db = db;
        this.cache = new MemoizedFunction(
            this.fetchProfileFromDb,
            300000 // 5 minutes TTL
        );
    }

    private async function fetchProfileFromDb(
        userId: UserId
    ): Promise<UserProfile> {
        return this.db.query(
            "SELECT * FROM user_profiles WHERE user_id = ?",
            [userId]
        );
    }

    async function getProfile(userId: UserId): Promise<UserProfile> {
        return this.cache.call(userId);
    }

    function invalidateCache(userId: UserId): void {
        this.cache.clear();
    }
}
```

### 5. Implementing Parallel Processing

```velox
// Process multiple activities in parallel
class BatchActivityProcessor {
    private processor: ActivityProcessor;
    private parallelProcessor: ParallelProcessor<UserActivity, ActivityReport>;

    constructor(processor: ActivityProcessor) {
        this.processor = processor;
        this.parallelProcessor = new ParallelProcessor(
            [],
            this.processActivity.bind(this),
            5 // Process 5 activities concurrently
        );
    }

    private async function processActivity(
        activity: UserActivity
    ): Promise<ActivityReport> {
        let result = await this.processor.processActivity(activity);
        return match (result) {
            case Either.Right(report) => report;
            case Either.Left(error) => throw error;
        };
    }

    async function processBatch(
        activities: UserActivity[]
    ): Promise<ActivityReport[]> {
        this.parallelProcessor.items = activities;
        return this.parallelProcessor.process();
    }
}
```

### 6. Adding Error Recovery

```velox
// Implement retry logic for failed operations
class RetryableActivityProcessor {
    private processor: ActivityProcessor;
    private retryPolicy: RetryPolicy;

    constructor(processor: ActivityProcessor) {
        this.processor = processor;
        this.retryPolicy = new RetryPolicy(
            3, // Max 3 attempts
            attempt => Math.pow(2, attempt) * 1000, // Exponential backoff
            error => error instanceof NetworkError
        );
    }

    async function processWithRetry(
        activity: UserActivity
    ): Promise<ActivityReport> {
        return this.retryPolicy.execute(async () => {
            let result = await this.processor.processActivity(activity);
            return match (result) {
                case Either.Right(report) => report;
                case Either.Left(error) => throw error;
            };
        });
    }
}
```

### 7. Putting It All Together

```velox
// Main application class
class ActivityProcessingSystem {
    private eventStore: ActivityEventStore;
    private processor: RetryableActivityProcessor;
    private batchProcessor: BatchActivityProcessor;
    private profileService: CachedUserProfileService;

    constructor(
        eventBus: EventBus<ActivityEvent>,
        db: Database
    ) {
        this.eventStore = new ActivityEventStore(eventBus);
        this.profileService = new CachedUserProfileService(db);
        
        let baseProcessor = new ActivityProcessor();
        this.processor = new RetryableActivityProcessor(baseProcessor);
        this.batchProcessor = new BatchActivityProcessor(baseProcessor);
    }

    async function processActivity(
        activity: UserActivity
    ): Promise<void> {
        // Record the event
        await this.eventStore.append({
            type: "ActivityCreated",
            activity,
            timestamp: Date.now()
        });

        // Process the activity
        let report = await this.processor.processWithRetry(activity);

        // Record the result
        await this.eventStore.append({
            type: "ReportGenerated",
            report,
            timestamp: Date.now()
        });
    }

    async function processBatch(
        activities: UserActivity[]
    ): Promise<void> {
        // Process activities in parallel
        let reports = await this.batchProcessor.processBatch(activities);

        // Record results
        for (let report of reports) {
            await this.eventStore.append({
                type: "ReportGenerated",
                report,
                timestamp: Date.now()
            });
        }
    }
}
```

## Usage Example

```velox
// Create and use the system
async function main() {
    let eventBus = new EventBus<ActivityEvent>();
    let db = new Database();
    let system = new ActivityProcessingSystem(eventBus, db);

    // Process a single activity
    await system.processActivity({
        userId: "user123",
        type: "login",
        timestamp: Date.now(),
        metadata: new Map([
            ["ip", "192.168.1.1"],
            ["device", "mobile"]
        ])
    });

    // Process a batch of activities
    let activities = [
        {
            userId: "user123",
            type: "purchase",
            timestamp: Date.now(),
            metadata: new Map([
                ["product", "item1"],
                ["amount", 99.99]
            ])
        },
        {
            userId: "user456",
            type: "view",
            timestamp: Date.now(),
            metadata: new Map([
                ["page", "product"],
                ["duration", 30]
            ])
        }
    ];

    await system.processBatch(activities);
}
```

## Key Features Demonstrated

1. **Functional Programming**
   - Immutable data structures
   - Pure functions
   - Function composition
   - Error handling with Either monad

2. **Event Sourcing**
   - Event-driven architecture
   - Event store
   - Event replay capability

3. **Performance Optimization**
   - Caching with TTL
   - Parallel processing
   - Lazy evaluation

4. **Error Handling**
   - Retry policies
   - Error recovery
   - Type-safe error handling

5. **Testing Considerations**
   - Pure functions are easily testable
   - Event sourcing enables replay testing
   - Caching can be tested for invalidation

## Best Practices Applied

1. **Immutability**
   - All data structures are immutable
   - State changes are handled through events

2. **Error Handling**
   - Comprehensive error handling with Either monad
   - Retry policies for transient failures

3. **Performance**
   - Caching for frequently accessed data
   - Parallel processing for batch operations
   - Lazy evaluation for large datasets

4. **Maintainability**
   - Clear separation of concerns
   - Type-safe interfaces
   - Composable components

5. **Scalability**
   - Event-driven architecture
   - Parallel processing capabilities
   - Caching for performance 