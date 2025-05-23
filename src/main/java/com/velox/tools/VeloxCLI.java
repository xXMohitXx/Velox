package com.velox.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class VeloxCLI {
    private static final String VERSION = "0.1.0";
    private static final String USAGE = """
        Velox %s - A high-performance programming language
        
        Usage: velox [options] [file]
        
        Options:
            -h, --help              Show this help message
            -v, --version           Show version information
            -f, --format <file>     Format a Velox source file
            -l, --lint <file>       Lint a Velox source file
            -r, --repl              Start the Velox REPL
            -c, --compile <file>    Compile a Velox source file
            -i, --interpret <file>  Interpret a Velox source file
        """.formatted(VERSION);
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(USAGE);
            return;
        }
        
        String command = args[0];
        
        try {
            switch (command) {
                case "-h", "--help" -> System.out.println(USAGE);
                case "-v", "--version" -> System.out.println("Velox " + VERSION);
                case "-f", "--format" -> handleFormat(args);
                case "-l", "--lint" -> handleLint(args);
                case "-r", "--repl" -> new Repl().start();
                case "-c", "--compile" -> handleCompile(args);
                case "-i", "--interpret" -> handleInterpret(args);
                default -> {
                    System.err.println("Unknown command: " + command);
                    System.out.println(USAGE);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static void handleFormat(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: No file specified for formatting");
            return;
        }
        
        try {
            String source = Files.readString(Paths.get(args[1]));
            Formatter formatter = new Formatter();
            String formatted = formatter.format(source);
            System.out.println(formatted);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    private static void handleLint(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: No file specified for linting");
            return;
        }
        
        try {
            String source = Files.readString(Paths.get(args[1]));
            Linter linter = new Linter();
            List<Linter.LintError> errors = linter.lint(source);
            
            if (errors.isEmpty()) {
                System.out.println("No linting errors found.");
            } else {
                errors.forEach(System.out::println);
                System.exit(errors.stream()
                    .anyMatch(e -> e.severity == Linter.LintError.Severity.ERROR) ? 1 : 0);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    private static void handleCompile(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: No file specified for compilation");
            return;
        }
        
        try {
            String source = Files.readString(Paths.get(args[1]));
            // TODO: Implement compilation
            System.out.println("Compilation not yet implemented");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    
    private static void handleInterpret(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: No file specified for interpretation");
            return;
        }
        
        try {
            String source = Files.readString(Paths.get(args[1]));
            // TODO: Implement interpretation
            System.out.println("Interpretation not yet implemented");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
} 