# Velox Installation Guide

## Prerequisites

Before installing Velox, ensure you have the following installed:
- Java Development Kit (JDK) 17 or later
- Maven 3.8 or later
- Git (optional, for cloning the repository)

## Platform-Specific Installation

### Windows
1. Install JDK 17 or later:
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [AdoptOpenJDK](https://adoptopenjdk.net/)
   - Set JAVA_HOME environment variable:
     ```powershell
     setx JAVA_HOME "C:\Program Files\Java\jdk-17"
     ```
   - Add to PATH:
     ```powershell
     setx PATH "%PATH%;%JAVA_HOME%\bin"
     ```

2. Install Maven:
   - Download from [Maven website](https://maven.apache.org/download.cgi)
   - Extract to a directory (e.g., `C:\Program Files\Apache\maven`)
   - Add to PATH:
     ```powershell
     setx PATH "%PATH%;C:\Program Files\Apache\maven\bin"
     ```

### macOS
1. Using Homebrew:
   ```bash
   brew install openjdk@17
   brew install maven
   ```

2. Manual installation:
   - Download JDK from [Oracle](https://www.oracle.com/java/technologies/downloads/)
   - Set JAVA_HOME:
     ```bash
     echo 'export JAVA_HOME=$(/usr/libexec/java_home)' >> ~/.zshrc
     echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
     ```

### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-17-jdk
sudo apt install maven
```

## Installation Steps

### 1. Clone the Repository (Optional)

If you want to build from source:
```bash
git clone https://github.com/xXMohitXx/velox.git
cd velox
```

### 2. Build from Source

Using Maven:
```bash
mvn clean install
```

This will:
- Compile the source code
- Run all tests
- Create the distribution package
- Install the package in your local Maven repository

### 3. Verify Installation

After installation, you can verify that Velox is working correctly by running the test suite:
```bash
mvn test
```

## Quick Start Guide

### 1. Basic Program
```velox
fn main() {
    print("Hello, Velox!");
}
```

### 2. Variables and Types
```velox
fn main() {
    let x = 42;              // Integer
    let y = 3.14;            // Float
    let name = "Velox";      // String
    let isActive = true;     // Boolean
    
    print(x);
    print(y);
    print(name);
    print(isActive);
}
```

### 3. Functions
```velox
fn add(a: int, b: int) -> int {
    return a + b;
}

fn main() {
    let result = add(5, 3);
    print(result);  // Output: 8
}
```

### 4. Control Flow
```velox
fn main() {
    let age = 18;
    
    if age >= 18 {
        print("Adult");
    } else {
        print("Minor");
    }
    
    let i = 0;
    while i < 5 {
        print(i);
        i = i + 1;
    }
}
```

### 5. Arrays and Maps
```velox
fn main() {
    let numbers = [1, 2, 3, 4, 5];
    let person = {
        "name": "John",
        "age": 30
    };
    
    print(numbers[0]);     // Output: 1
    print(person["name"]); // Output: John
}
```

## Common Development Tasks

### 1. Creating a New Project
```bash
mkdir my-velox-project
cd my-velox-project
velox init
```

### 2. Running Tests
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=MyTestClass

# Run with debug output
mvn test -Dvelox.debug=true
```

### 3. Building for Production
```bash
mvn clean package -P production
```

### 4. Code Formatting
```bash
mvn velox:format
```

### 5. Documentation Generation
```bash
mvn javadoc:javadoc
```

## Advanced Examples

### 1. Error Handling
```velox
fn divide(a: int, b: int) -> int {
    if b == 0 {
        throw "Division by zero";
    }
    return a / b;
}

fn main() {
    try {
        let result = divide(10, 0);
    } catch error {
        print("Error: " + error);
    }
}
```

### 2. File Operations
```velox
fn main() {
    let file = open("data.txt", "r");
    let content = file.read();
    file.close();
    
    print(content);
}
```

### 3. Network Programming
```velox
fn main() {
    let server = Server.new(8080);
    server.on("request", fn(request) {
        return Response.new(200, "Hello, World!");
    });
    server.start();
}
```

## Advanced Use Cases

### Database Operations
```velox
// SQLite Example
fn main() {
    let db = Database.connect("sqlite://data.db");
    
    // Create table
    db.execute("""
        CREATE TABLE users (
            id INTEGER PRIMARY KEY,
            name TEXT,
            email TEXT
        )
    """);
    
    // Insert data
    db.execute("""
        INSERT INTO users (name, email)
        VALUES (?, ?)
    """, ["John Doe", "john@example.com"]);
    
    // Query data
    let users = db.query("SELECT * FROM users");
    for user in users {
        print(user["name"] + ": " + user["email"]);
    }
    
    db.close();
}

// MongoDB Example
fn main() {
    let client = MongoClient.connect("mongodb://localhost:27017");
    let db = client.getDatabase("mydb");
    let collection = db.getCollection("users");
    
    // Insert document
    collection.insert({
        "name": "John Doe",
        "email": "john@example.com",
        "age": 30
    });
    
    // Find documents
    let users = collection.find({"age": {"$gt": 25}});
    for user in users {
        print(user);
    }
}
```

### GUI Development
```velox
// Basic Window
fn main() {
    let window = Window.new("My App", 800, 600);
    
    // Add button
    let button = Button.new("Click Me");
    button.onClick(fn() {
        print("Button clicked!");
    });
    window.add(button);
    
    // Add text input
    let input = TextInput.new();
    input.onChange(fn(text) {
        print("Text changed: " + text);
    });
    window.add(input);
    
    window.show();
}

// Custom Widget
class CustomWidget extends Widget {
    fn draw() {
        // Custom drawing code
        this.drawRectangle(0, 0, 100, 100);
        this.drawText("Custom", 10, 10);
    }
}
```

### Web Development
```velox
// HTTP Server
fn main() {
    let server = HttpServer.new(8080);
    
    server.get("/", fn(request) {
        return Response.html("<h1>Hello, Velox!</h1>");
    });
    
    server.post("/api/data", fn(request) {
        let data = request.json();
        // Process data
        return Response.json({"status": "success"});
    });
    
    server.start();
}

// WebSocket Server
fn main() {
    let ws = WebSocketServer.new(8080);
    
    ws.onConnection(fn(client) {
        client.send("Welcome!");
        
        client.onMessage(fn(message) {
            print("Received: " + message);
            client.send("Echo: " + message);
        });
    });
    
    ws.start();
}
```

## Performance Optimization

### 1. Memory Management
```velox
// Use object pooling
let pool = ObjectPool.new(1000);
let obj = pool.acquire();
// Use object
pool.release(obj);

// Implement weak references
let cache = WeakMap.new();
cache.set(key, value);
```

### 2. Concurrency
```velox
// Parallel processing
fn main() {
    let tasks = [1, 2, 3, 4, 5];
    let results = parallel.map(tasks, fn(x) {
        return x * x;
    });
}

// Async operations
fn main() {
    async fn fetchData() {
        let data = await http.get("https://api.example.com/data");
        return data;
    }
    
    let result = await fetchData();
}
```

### 3. Caching
```velox
// Implement LRU cache
let cache = LRUCache.new(1000);
cache.set("key", value);
let value = cache.get("key");

// Use memoization
fn fibonacci(n: int) -> int {
    static let memo = Map.new();
    
    if memo.has(n) {
        return memo.get(n);
    }
    
    let result = n <= 1 ? n : fibonacci(n-1) + fibonacci(n-2);
    memo.set(n, result);
    return result;
}
```

## Debugging Techniques

### 1. Logging
```velox
// Configure logging
Logger.setLevel(LogLevel.DEBUG);
Logger.setOutput("app.log");

// Use different log levels
Logger.debug("Debug message");
Logger.info("Info message");
Logger.warn("Warning message");
Logger.error("Error message");
```

### 2. Breakpoints
```velox
// Set breakpoints in code
debug.breakpoint();

// Conditional breakpoints
debug.breakpointIf(x > 100);

// Watch variables
debug.watch("variableName");
```

### 3. Profiling
```velox
// Start profiling
let profiler = Profiler.start();

// Your code here

// Stop and analyze
let results = profiler.stop();
print(results.getHotspots());
print(results.getMemoryUsage());
```

## Troubleshooting Decision Tree

```
Start
  │
  ├─ Is the program compiling?
  │   ├─ Yes → Check runtime errors
  │   └─ No → Check syntax and dependencies
  │
  ├─ Are there runtime errors?
  │   ├─ Yes → Check error messages and stack traces
  │   └─ No → Check performance issues
  │
  ├─ Is performance acceptable?
  │   ├─ Yes → Check memory usage
  │   └─ No → Profile and optimize
  │
  ├─ Is memory usage normal?
  │   ├─ Yes → Check for logical errors
  │   └─ No → Check for memory leaks
  │
  └─ Are there logical errors?
      ├─ Yes → Debug with logging and breakpoints
      └─ No → Program is working correctly
```

### Common Solutions

1. **Compilation Errors**
   - Check syntax
   - Verify dependencies
   - Update compiler
   - Clear build cache

2. **Runtime Errors**
   - Check error messages
   - Review stack traces
   - Verify input data
   - Check environment

3. **Performance Issues**
   - Profile code
   - Optimize algorithms
   - Check resource usage
   - Implement caching

4. **Memory Issues**
   - Check for leaks
   - Optimize data structures
   - Implement garbage collection
   - Monitor memory usage

5. **Logical Errors**
   - Add logging
   - Use debugger
   - Write unit tests
   - Review business logic

## Getting Help

If you encounter any issues:
1. Check the [documentation](docs/)
2. Search existing [issues](https://github.com/yourusername/velox/issues)
3. Create a new issue with:
   - Detailed error message
   - Steps to reproduce
   - Environment information
   - System logs
   - Stack traces

## System Requirements

### Minimum Requirements
- CPU: 1 GHz or faster
- RAM: 2 GB minimum
- Disk Space: 500 MB free space
- OS: Windows 10+, macOS 10.15+, or Linux

### Recommended Requirements
- CPU: 2 GHz or faster
- RAM: 4 GB or more
- Disk Space: 1 GB free space
- OS: Latest version of Windows, macOS, or Linux

## Security Considerations

1. **File Permissions**
   - Ensure proper file permissions for the installation directory
   - Run with appropriate user privileges
   - Use secure file paths
   - Implement proper access controls

2. **Network Access**
   - Configure firewall rules if needed
   - Use secure connections for remote operations
   - Implement SSL/TLS
   - Use secure protocols

3. **Dependencies**
   - Regularly update dependencies
   - Verify dependency integrity
   - Check for security vulnerabilities
   - Use trusted sources

## Updating Velox

### Automatic Update
```bash
mvn clean install -U
```

### Manual Update
1. Pull latest changes:
```bash
git pull origin main
```

2. Rebuild:
```bash
mvn clean install
```

## Uninstallation

### Windows
1. Remove the installation directory
2. Remove environment variables
3. Clean Maven repository (optional)
4. Remove from PATH

### macOS/Linux
```bash
rm -rf /usr/local/velox
rm -rf ~/.m2/repository/com/velox
```

## Support

For additional support:
- Documentation: [docs/](docs/)
- Issues: [GitHub Issues](https://github.com/yourusername/velox/issues)
- Community: [Discord/Slack Channel]
- Email: support@velox-lang.org

## License

Velox is licensed under the [MIT License](LICENSE). See the LICENSE file for details. 