# Velox Token Specification

## 1. Keywords

### Control Flow
- `if` - Conditional statement
- `else` - Alternative branch
- `for` - For loop
- `while` - While loop
- `do` - Do-while loop
- `break` - Break from loop
- `continue` - Continue loop iteration
- `return` - Return from function
- `match` - Pattern matching

### Type Keywords
- `int` - Integer type
- `float` - Floating point type
- `bool` - Boolean type
- `string` - String type
- `char` - Character type
- `void` - No value type
- `Result` - Result type for error handling

### Function Keywords
- `fn` - Function declaration
- `async` - Async function
- `await` - Await async operation
- `spawn` - Spawn concurrent task

### Class Keywords
- `class` - Class declaration
- `new` - Constructor
- `this` - Current instance
- `pub` - Public access modifier

### Module Keywords
- `module` - Module declaration
- `import` - Import module
- `export` - Export symbol

### Variable Keywords
- `let` - Variable declaration
- `const` - Constant declaration

### Error Handling
- `try` - Try block
- `catch` - Catch block
- `throw` - Throw exception

## 2. Operators

### Arithmetic Operators
- `+` - Addition
- `-` - Subtraction
- `*` - Multiplication
- `/` - Division
- `%` - Modulo
- `**` - Exponentiation

### Assignment Operators
- `=` - Assignment
- `+=` - Add and assign
- `-=` - Subtract and assign
- `*=` - Multiply and assign
- `/=` - Divide and assign
- `%=` - Modulo and assign
- `**=` - Exponentiate and assign

### Comparison Operators
- `==` - Equal to
- `!=` - Not equal to
- `<` - Less than
- `>` - Greater than
- `<=` - Less than or equal to
- `>=` - Greater than or equal to

### Logical Operators
- `&&` - Logical AND
- `||` - Logical OR
- `!` - Logical NOT

### Bitwise Operators
- `&` - Bitwise AND
- `|` - Bitwise OR
- `^` - Bitwise XOR
- `~` - Bitwise NOT
- `<<` - Left shift
- `>>` - Right shift
- `>>>` - Unsigned right shift

### Other Operators
- `.` - Member access
- `->` - Function return type
- `=>` - Match arm
- `..` - Range
- `?` - Optional type
- `!` - Non-null assertion
- `::` - Namespace resolution

## 3. Literals

### Integer Literals
```
integer_literal = decimal_literal | hex_literal | binary_literal | octal_literal;
decimal_literal = digit, {digit};
hex_literal = "0x", hex_digit, {hex_digit};
binary_literal = "0b", binary_digit, {binary_digit};
octal_literal = "0o", octal_digit, {octal_digit};
```

### Float Literals
```
float_literal = decimal_literal, ".", decimal_literal, [exponent];
exponent = ("e" | "E"), ["+" | "-"], decimal_literal;
```

### String Literals
```
string_literal = '"', {string_character}, '"';
string_character = any_character_except_quote | escape_sequence;
escape_sequence = "\", ("n" | "t" | "r" | "\" | "'" | "0" | "x", hex_digit, hex_digit);
```

### Boolean Literals
- `true`
- `false`

### Null Literal
- `null`

## 4. Identifiers

### Rules
- Start with letter or underscore
- Followed by letters, digits, or underscores
- Case sensitive
- Cannot be a keyword

### Examples
```
valid_identifier
_private
camelCase
PascalCase
CONSTANT_CASE
```

## 5. Comments

### Single-line Comments
```
// This is a single-line comment
```

### Multi-line Comments
```
/* This is a
   multi-line
   comment */
```

### Documentation Comments
```
/// Function documentation
//! Module documentation
```

## 6. Whitespace

### Types
- Space (` `)
- Tab (`\t`)
- Newline (`\n`, `\r\n`)
- Form feed (`\f`)
- Vertical tab (`\v`)

### Rules
- Whitespace is ignored except when separating tokens
- Newlines are significant in some contexts (e.g., automatic semicolon insertion)
- Indentation is not significant but recommended for readability

## 7. Separators

### Punctuation
- `;` - Statement terminator
- `,` - List separator
- `.` - Decimal point, member access
- `:` - Type annotation
- `(` - Grouping, function call
- `)` - Grouping, function call
- `{` - Block start
- `}` - Block end
- `[` - Array start
- `]` - Array end
- `<` - Generic start
- `>` - Generic end

## 8. Special Tokens

### End of File
- `EOF` - Marks the end of input

### Error Tokens
- `ERROR` - Invalid token
- `UNEXPECTED` - Unexpected character

## 9. Token Categories

### Reserved Words
- All keywords listed above

### Operators
- All operators listed above

### Literals
- All literal types listed above

### Identifiers
- Variable names
- Function names
- Class names
- Module names

### Separators
- All punctuation marks listed above

### Comments
- All comment types listed above

### Whitespace
- All whitespace characters listed above 