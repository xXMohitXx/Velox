package com.velox.compiler.parser;

import com.velox.compiler.lexer.Lexer;
import com.velox.compiler.ast.*;
import com.velox.compiler.token.Token;
import com.velox.compiler.error.ParseError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ParserTest {
    private Lexer lexer;
    private ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        lexer = new Lexer();
        errorHandler = new ErrorHandler();
    }

    @Test
    void testParseModule() {
        String source = """
            module test;
            
            fn main() {
                let x = 42;
                return x;
            }
            """;

        List<Token> tokens = lexer.tokenize(source);
        Parser parser = new Parser(tokens, errorHandler);
        ModuleNode module = parser.parse();

        assertNotNull(module);
        assertEquals("test", module.getName());
        assertEquals(1, module.getDeclarations().size());
        assertTrue(module.getDeclarations().get(0) instanceof FunctionNode);
    }

    @Test
    void testParseFunction() {
        String source = """
            module test;
            
            fn add(a: int, b: int) -> int {
                return a + b;
            }
            """;

        List<Token> tokens = lexer.tokenize(source);
        Parser parser = new Parser(tokens, errorHandler);
        ModuleNode module = parser.parse();

        assertNotNull(module);
        FunctionNode function = (FunctionNode) module.getDeclarations().get(0);
        assertEquals("add", function.getName());
        assertEquals(2, function.getParameters().size());
        assertNotNull(function.getReturnType());
        assertEquals("int", function.getReturnType().getTypeName());
    }

    @Test
    void testParseClass() {
        String source = """
            module test;
            
            class Point {
                let x: int;
                let y: int;
                
                fn new(x: int, y: int) {
                    this.x = x;
                    this.y = y;
                }
                
                fn distance(other: Point) -> float {
                    let dx = this.x - other.x;
                    let dy = this.y - other.y;
                    return sqrt(dx * dx + dy * dy);
                }
            }
            """;

        List<Token> tokens = lexer.tokenize(source);
        Parser parser = new Parser(tokens, errorHandler);
        ModuleNode module = parser.parse();

        assertNotNull(module);
        ClassNode classNode = (ClassNode) module.getDeclarations().get(0);
        assertEquals("Point", classNode.getName());
        assertEquals(2, classNode.getFields().size());
        assertEquals(2, classNode.getMethods().size());
    }

    @Test
    void testParseError() {
        String source = """
            module test;
            
            fn main() {
                let x = 42
                return x;
            }
            """;

        List<Token> tokens = lexer.tokenize(source);
        Parser parser = new Parser(tokens, errorHandler);
        
        assertThrows(ParseError.class, () -> {
            parser.parse();
        });
    }

    @Test
    void testParseComplexExpression() {
        String source = """
            module test;
            
            fn main() {
                let x = 2 + 3 * 4;
                let y = (1 + 2) * 3;
                return x + y;
            }
            """;

        List<Token> tokens = lexer.tokenize(source);
        Parser parser = new Parser(tokens, errorHandler);
        ModuleNode module = parser.parse();

        assertNotNull(module);
        FunctionNode function = (FunctionNode) module.getDeclarations().get(0);
        BlockNode block = function.getBody();
        assertEquals(3, block.getStatements().size());
    }

    @Test
    void testParseControlFlow() {
        String source = """
            module test;
            
            fn main() {
                if x > 0 {
                    return 1;
                } else if x < 0 {
                    return -1;
                } else {
                    return 0;
                }
            }
            """;

        List<Token> tokens = lexer.tokenize(source);
        Parser parser = new Parser(tokens, errorHandler);
        ModuleNode module = parser.parse();

        assertNotNull(module);
        FunctionNode function = (FunctionNode) module.getDeclarations().get(0);
        BlockNode block = function.getBody();
        assertEquals(1, block.getStatements().size());
        assertTrue(block.getStatements().get(0) instanceof IfNode);
    }
} 