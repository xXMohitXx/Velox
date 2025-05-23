package com.velox.tools;

import com.velox.compiler.lexer.Lexer;
import com.velox.compiler.lexer.Token;
import com.velox.compiler.python.PythonBridge;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Repl {
    private static final String PROMPT = "velox> ";
    private static final String CONTINUATION_PROMPT = "....> ";
    private static final String VERSION = "0.1.0";
    private static final String WELCOME_MESSAGE = """
        Welcome to Velox %s
        Type .help for available commands
        Type .exit to quit
        """.formatted(VERSION);
    
    private final Scanner scanner;
    private final List<String> history;
    private boolean running;
    
    public Repl() {
        this.scanner = new Scanner(System.in);
        this.history = new ArrayList<>();
        this.running = true;
    }
    
    public void start() {
        System.out.println(WELCOME_MESSAGE);
        
        while (running) {
            try {
                String input = readInput();
                if (input == null) continue;
                
                if (input.startsWith(".")) {
                    handleCommand(input.substring(1));
                } else {
                    evaluateInput(input);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
    private String readInput() {
        System.out.print(PROMPT);
        StringBuilder input = new StringBuilder();
        String line = scanner.nextLine().trim();
        
        // Handle multi-line input
        while (line.endsWith("\\")) {
            input.append(line, 0, line.length() - 1).append("\n");
            System.out.print(CONTINUATION_PROMPT);
            line = scanner.nextLine().trim();
        }
        input.append(line);
        
        String result = input.toString().trim();
        if (!result.isEmpty()) {
            history.add(result);
        }
        return result.isEmpty() ? null : result;
    }
    
    private void evaluateInput(String input) {
        try {
            // Tokenize input
            Lexer lexer = new Lexer(input);
            List<Token> tokens = lexer.scanTokens();
            
            // Print tokens for debugging
            System.out.println("Tokens:");
            for (Token token : tokens) {
                System.out.println(token);
            }
            
            // TODO: Add parser and interpreter evaluation
            // For now, just echo the input
            System.out.println("Evaluating: " + input);
            
        } catch (Exception e) {
            System.err.println("Error evaluating input: " + e.getMessage());
        }
    }
    
    private void handleCommand(String command) {
        String[] parts = command.split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";
        
        switch (cmd) {
            case "help" -> showHelp();
            case "exit", "quit" -> running = false;
            case "clear" -> clearScreen();
            case "history" -> showHistory();
            case "load" -> loadFile(args);
            case "save" -> saveHistory(args);
            case "version" -> System.out.println("Velox " + VERSION);
            default -> System.out.println("Unknown command: " + cmd);
        }
    }
    
    private void showHelp() {
        System.out.println("""
            Available commands:
            .help              - Show this help message
            .exit/.quit        - Exit the REPL
            .clear             - Clear the screen
            .history           - Show command history
            .load <file>       - Load and execute a file
            .save <file>       - Save command history to a file
            .version           - Show version information
            """);
    }
    
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private void showHistory() {
        for (int i = 0; i < history.size(); i++) {
            System.out.printf("%d: %s%n", i + 1, history.get(i));
        }
    }
    
    private void loadFile(String filename) {
        if (filename.isEmpty()) {
            System.out.println("Please specify a file to load");
            return;
        }
        
        try {
            String content = Files.readString(Paths.get(filename));
            System.out.println("Loading file: " + filename);
            evaluateInput(content);
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
    }
    
    private void saveHistory(String filename) {
        if (filename.isEmpty()) {
            System.out.println("Please specify a file to save to");
            return;
        }
        
        try {
            Files.write(Paths.get(filename), history);
            System.out.println("History saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving history: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        new Repl().start();
    }
} 