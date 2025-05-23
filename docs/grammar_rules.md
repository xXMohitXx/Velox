# Velox Grammar Rules (EBNF)

## 1. Program Structure

```
program = module_declaration, {import_statement}, {declaration};

module_declaration = "module", identifier, ";";
import_statement = "import", module_path, ";";
module_path = identifier, {".", identifier};
```

## 2. Declarations

```
declaration = function_declaration
           | class_declaration
           | variable_declaration
           | constant_declaration;

function_declaration = ["pub"], "fn", identifier, "(", [parameter_list], ")", [return_type], block;
class_declaration = ["pub"], "class", identifier, "{", {class_member}, "}";
variable_declaration = "let", identifier, [type_annotation], ["=", expression], ";";
constant_declaration = "const", identifier, "=", expression, ";";
```

## 3. Types

```
type = basic_type
     | array_type
     | function_type
     | class_type
     | generic_type;

basic_type = "int" | "float" | "bool" | "string" | "char" | "void";
array_type = "[", type, "]";
function_type = "fn", "(", [type_list], ")", ["->", type];
class_type = identifier;
generic_type = identifier, "<", type_list, ">";

type_annotation = ":", type;
type_list = type, {",", type};
```

## 4. Expressions

```
expression = assignment_expression;

assignment_expression = logical_or, {assignment_operator, logical_or};
logical_or = logical_and, {"||", logical_and};
logical_and = equality, {"&&", equality};
equality = comparison, {equality_operator, comparison};
comparison = term, {comparison_operator, term};
term = factor, {("+" | "-"), factor};
factor = unary, {("*" | "/" | "%"), unary};
unary = {"!" | "-"}, primary;
primary = literal
       | identifier
       | "(", expression, ")"
       | function_call
       | array_access
       | member_access
       | array_literal
       | object_literal;
```

## 5. Statements

```
statement = expression_statement
         | block_statement
         | if_statement
         | for_statement
         | while_statement
         | return_statement
         | break_statement
         | continue_statement
         | try_statement;

expression_statement = expression, ";";
block_statement = "{", {statement}, "}";
if_statement = "if", "(", expression, ")", statement, ["else", statement];
for_statement = "for", identifier, "in", expression, statement;
while_statement = "while", "(", expression, ")", statement;
return_statement = "return", [expression], ";";
break_statement = "break", ";";
continue_statement = "continue", ";";
try_statement = "try", block, {catch_clause};
```

## 6. Functions and Methods

```
parameter_list = parameter, {",", parameter};
parameter = identifier, type_annotation, ["=", expression];
function_call = expression, "(", [argument_list], ")";
argument_list = expression, {",", expression};
lambda_expression = "(", [parameter_list], ")", ["->", type], block;
```

## 7. Classes and Objects

```
class_member = property_declaration
            | method_declaration
            | constructor_declaration;

property_declaration = ["pub"], "let", identifier, type_annotation, ";";
method_declaration = ["pub"], "fn", identifier, "(", [parameter_list], ")", [return_type], block;
constructor_declaration = "fn", "new", "(", [parameter_list], ")", block;
member_access = expression, ".", identifier;
```

## 8. Error Handling

```
catch_clause = "catch", [identifier, [type_annotation]], block;
result_type = "Result", "<", type, ",", type, ">";
```

## 9. Concurrency

```
async_function = "async", "fn", identifier, "(", [parameter_list], ")", [return_type], block;
spawn_expression = "spawn", block;
await_expression = "await", expression;
```

## 10. Literals

```
literal = integer_literal
       | float_literal
       | string_literal
       | boolean_literal
       | null_literal;

integer_literal = digit, {digit};
float_literal = digit, {digit}, ".", digit, {digit};
string_literal = '"', {string_character}, '"';
boolean_literal = "true" | "false";
null_literal = "null";

array_literal = "[", [expression_list], "]";
object_literal = "{", [property_list], "}";
property_list = property, {",", property};
property = identifier, ":", expression;
```

## 11. Identifiers and Keywords

```
identifier = letter, {letter | digit | "_"};
letter = "A" | "B" | ... | "Z" | "a" | "b" | ... | "z";
digit = "0" | "1" | ... | "9";

keyword = "module" | "import" | "fn" | "class" | "let" | "const"
       | "if" | "else" | "for" | "while" | "return" | "break" | "continue"
       | "try" | "catch" | "async" | "await" | "spawn" | "pub" | "new";
```

## 12. Operators

```
assignment_operator = "=" | "+=" | "-=" | "*=" | "/=" | "%=";
equality_operator = "==" | "!=";
comparison_operator = "<" | ">" | "<=" | ">=";
```

## 13. Comments

```
comment = line_comment | block_comment;
line_comment = "//", {any_character}, newline;
block_comment = "/*", {any_character}, "*/";
```

## 14. Whitespace and Separators

```
whitespace = space | tab | newline;
space = " ";
tab = "\t";
newline = "\n" | "\r\n";
separator = "," | ";" | ":" | "." | "(" | ")" | "{" | "}" | "[" | "]";
``` 