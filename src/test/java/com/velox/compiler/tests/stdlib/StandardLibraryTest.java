package com.velox.compiler.tests.stdlib;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StandardLibraryTest {
    @Test
    void testMathOperations() {
        // Test basic arithmetic
        assertEquals(4, 2 + 2);
        assertEquals(0, 2 - 2);
        assertEquals(4, 2 * 2);
        assertEquals(1, 2 / 2);
        assertEquals(0, 2 % 2);

        // Test advanced math functions
        assertEquals(4, Math.pow(2, 2));
        assertEquals(2, Math.sqrt(4));
        assertEquals(1, Math.sin(Math.PI/2), 0.0001);
        assertEquals(1, Math.cos(0), 0.0001);
    }

    @Test
    void testStringOperations() {
        String str = "Hello, World!";
        
        // Test string manipulation
        assertEquals("HELLO, WORLD!", str.toUpperCase());
        assertEquals("hello, world!", str.toLowerCase());
        assertEquals(13, str.length());
        assertEquals("World", str.substring(7, 12));
        assertTrue(str.contains("World"));
        assertEquals("Hello, Velox!", str.replace("World", "Velox"));
    }

    @Test
    void testIOOperations() {
        // Test file operations
        String testFile = "test.txt";
        String content = "Hello, Velox!";
        
        // Write test
        try {
            java.nio.file.Files.writeString(
                java.nio.file.Paths.get(testFile),
                content
            );
            assertTrue(java.nio.file.Files.exists(java.nio.file.Paths.get(testFile)));
            
            // Read test
            String readContent = java.nio.file.Files.readString(
                java.nio.file.Paths.get(testFile)
            );
            assertEquals(content, readContent);
            
            // Cleanup
            java.nio.file.Files.delete(java.nio.file.Paths.get(testFile));
        } catch (java.io.IOException e) {
            fail("IO operation failed: " + e.getMessage());
        }
    }

    @Test
    void testCollections() {
        // Test List operations
        java.util.List<String> list = new java.util.ArrayList<>();
        list.add("Hello");
        list.add("World");
        assertEquals(2, list.size());
        assertEquals("Hello", list.get(0));
        assertEquals("World", list.get(1));
        
        // Test Map operations
        java.util.Map<String, Integer> map = new java.util.HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        assertEquals(2, map.size());
        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
    }

    @Test
    void testErrorHandling() {
        // Test division by zero
        assertThrows(ArithmeticException.class, () -> {
            int result = 1 / 0;
        });
        
        // Test null pointer
        String str = null;
        assertThrows(NullPointerException.class, () -> {
            str.length();
        });
        
        // Test array index out of bounds
        int[] arr = new int[1];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            int value = arr[1];
        });
    }
} 