package com.velox.compiler.lexer;

import java.util.List;
import com.velox.compiler.token.Token;

/**
 * Test class for the Velox lexer.
 */
public class LexerTest {
    public static void main(String[] args) {
        // Test source code
        String source = """
            module example;
            
            // Function to calculate factorial
            fn factorial(n: int) -> int {
                if n <= 1 {
                    return 1;
                }
                return n * factorial(n - 1);
            }
            
            // Main function
            fn main() {
                let result = factorial(5);
                println("Factorial of 5 is: {result}");
            }
            """;

        // Create lexer and scan tokens
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize(source);

        // Print tokens
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
} 