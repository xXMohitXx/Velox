package com.velox.compiler;

import com.velox.compiler.ast.*;
import com.velox.compiler.bytecode.*;
import com.velox.compiler.optimizer.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CompilerTest {
    private Compiler compiler;
    private VirtualMachine vm;
    private Optimizer optimizer;
    
    @BeforeEach
    void setUp() {
        compiler = new Compiler();
        vm = new VirtualMachine();
        optimizer = new Optimizer();
    }
    
    @Test
    void testBasicArithmetic() {
        String source = "var x = 5 + 3 * 2;";
        ASTNode ast = compiler.parse(source);
        optimizer.optimize(ast);
        Bytecode bytecode = compiler.generate(ast);
        vm.execute(bytecode);
        assertEquals(11, vm.getGlobal("x"));
    }
    
    @Test
    void testFunctionCall() {
        String source = """
            function add(a, b) {
                return a + b;
            }
            var result = add(5, 3);
            """;
        ASTNode ast = compiler.parse(source);
        optimizer.optimize(ast);
        Bytecode bytecode = compiler.generate(ast);
        vm.execute(bytecode);
        assertEquals(8, vm.getGlobal("result"));
    }
    
    @Test
    void testLoopOptimization() {
        String source = """
            var sum = 0;
            for (var i = 0; i < 5; i++) {
                sum = sum + i;
            }
            """;
        ASTNode ast = compiler.parse(source);
        optimizer.optimize(ast);
        Bytecode bytecode = compiler.generate(ast);
        vm.execute(bytecode);
        assertEquals(10, vm.getGlobal("sum"));
    }
    
    @Test
    void testErrorHandling() {
        String source = """
            try {
                throw "Error";
            } catch (e) {
                var message = e;
            }
            """;
        ASTNode ast = compiler.parse(source);
        optimizer.optimize(ast);
        Bytecode bytecode = compiler.generate(ast);
        vm.execute(bytecode);
        assertEquals("Error", vm.getGlobal("message"));
    }
    
    @Test
    void testStandardLibrary() {
        String source = """
            var list = std.collections.list(1, 2, 3);
            var sum = std.math.sum(list);
            """;
        ASTNode ast = compiler.parse(source);
        optimizer.optimize(ast);
        Bytecode bytecode = compiler.generate(ast);
        vm.execute(bytecode);
        assertEquals(6, vm.getGlobal("sum"));
    }
    
    @Test
    void testPackageManagement() {
        String source = """
            import "math";
            var result = math.sqrt(16);
            """;
        ASTNode ast = compiler.parse(source);
        optimizer.optimize(ast);
        Bytecode bytecode = compiler.generate(ast);
        vm.execute(bytecode);
        assertEquals(4.0, vm.getGlobal("result"));
    }
    
    @Test
    void testIDEIntegration() {
        String source = "var x = 5;";
        IDESupport ide = new IDESupport();
        ide.updateFile("test.vlx", source);
        
        List<Diagnostic> diagnostics = ide.getDiagnostics("test.vlx");
        assertTrue(diagnostics.isEmpty());
        
        List<CompletionItem> completions = ide.getCompletions("test.vlx", 5);
        assertFalse(completions.isEmpty());
    }
    
    @Test
    void testOptimizationPasses() {
        String source = """
            var x = 2 + 2;  // Should be constant folded
            if (true) {     // Should be dead code eliminated
                var y = 5;
            }
            function small() { return 1; }
            var z = small(); // Should be inlined
            """;
        ASTNode ast = compiler.parse(source);
        optimizer.optimize(ast);
        Bytecode bytecode = compiler.generate(ast);
        vm.execute(bytecode);
        assertEquals(4, vm.getGlobal("x"));
        assertEquals(1, vm.getGlobal("z"));
    }
    
    @Test
    void testComplexDataTypes() {
        String source = """
            var map = {"key": "value"};
            var list = [1, 2, 3];
            var tuple = (1, "string", true);
            """;
        ASTNode ast = compiler.parse(source);
        optimizer.optimize(ast);
        Bytecode bytecode = compiler.generate(ast);
        vm.execute(bytecode);
        assertNotNull(vm.getGlobal("map"));
        assertNotNull(vm.getGlobal("list"));
        assertNotNull(vm.getGlobal("tuple"));
    }
} 