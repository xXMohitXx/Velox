package com.velox.std;

import java.util.*;
import java.io.*;
import java.net.*;
import java.time.*;
import java.math.*;

public class StandardLibrary {
    // I/O Module
    public static class IO {
        public static void print(Object value) {
            System.out.println(value);
        }
        
        public static String readLine() {
            try {
                return new BufferedReader(new InputStreamReader(System.in)).readLine();
            } catch (IOException e) {
                throw new RuntimeException("Failed to read input", e);
            }
        }
        
        public static void writeFile(String path, String content) {
            try (FileWriter writer = new FileWriter(path)) {
                writer.write(content);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write file: " + path, e);
            }
        }
        
        public static String readFile(String path) {
            try {
                return new String(Files.readAllBytes(Paths.get(path)));
            } catch (IOException e) {
                throw new RuntimeException("Failed to read file: " + path, e);
            }
        }
    }
    
    // Collections Module
    public static class Collections {
        public static <T> List<T> list(T... elements) {
            return Arrays.asList(elements);
        }
        
        public static <K,V> Map<K,V> map(K[] keys, V[] values) {
            Map<K,V> result = new HashMap<>();
            for (int i = 0; i < keys.length; i++) {
                result.put(keys[i], values[i]);
            }
            return result;
        }
        
        public static <T> Set<T> set(T... elements) {
            return new HashSet<>(Arrays.asList(elements));
        }
    }
    
    // String Module
    public static class Strings {
        public static String format(String template, Object... args) {
            return String.format(template, args);
        }
        
        public static String[] split(String str, String delimiter) {
            return str.split(delimiter);
        }
        
        public static String join(String[] parts, String delimiter) {
            return String.join(delimiter, parts);
        }
        
        public static boolean contains(String str, String substring) {
            return str.contains(substring);
        }
    }
    
    // Math Module
    public static class Math {
        public static double sin(double x) {
            return java.lang.Math.sin(x);
        }
        
        public static double cos(double x) {
            return java.lang.Math.cos(x);
        }
        
        public static double sqrt(double x) {
            return java.lang.Math.sqrt(x);
        }
        
        public static double pow(double x, double y) {
            return java.lang.Math.pow(x, y);
        }
        
        public static int random(int min, int max) {
            return min + (int)(java.lang.Math.random() * (max - min));
        }
    }
    
    // Time Module
    public static class Time {
        public static long now() {
            return System.currentTimeMillis();
        }
        
        public static String formatDate(long timestamp) {
            return Instant.ofEpochMilli(timestamp)
                         .atZone(ZoneId.systemDefault())
                         .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        
        public static void sleep(long milliseconds) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep interrupted", e);
            }
        }
    }
    
    // Network Module
    public static class Network {
        public static String httpGet(String url) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } catch (IOException e) {
                throw new RuntimeException("HTTP GET failed: " + url, e);
            }
        }
        
        public static String httpPost(String url, String data) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(data.getBytes());
                }
                
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            } catch (IOException e) {
                throw new RuntimeException("HTTP POST failed: " + url, e);
            }
        }
    }
    
    // File System Module
    public static class FileSystem {
        public static boolean exists(String path) {
            return new File(path).exists();
        }
        
        public static void createDirectory(String path) {
            if (!new File(path).mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + path);
            }
        }
        
        public static void delete(String path) {
            if (!new File(path).delete()) {
                throw new RuntimeException("Failed to delete: " + path);
            }
        }
        
        public static String[] listDirectory(String path) {
            File dir = new File(path);
            if (!dir.isDirectory()) {
                throw new RuntimeException("Not a directory: " + path);
            }
            return dir.list();
        }
    }
    
    // JSON Module
    public static class JSON {
        public static String stringify(Object obj) {
            // Simple JSON stringification
            if (obj == null) return "null";
            if (obj instanceof String) return "\"" + obj + "\"";
            if (obj instanceof Number || obj instanceof Boolean) return obj.toString();
            if (obj instanceof List) {
                List<?> list = (List<?>) obj;
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append(stringify(list.get(i)));
                }
                return sb.append("]").toString();
            }
            if (obj instanceof Map) {
                Map<?,?> map = (Map<?,?>) obj;
                StringBuilder sb = new StringBuilder("{");
                boolean first = true;
                for (Map.Entry<?,?> entry : map.entrySet()) {
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("\"").append(entry.getKey()).append("\":")
                      .append(stringify(entry.getValue()));
                }
                return sb.append("}").toString();
            }
            throw new RuntimeException("Cannot stringify: " + obj.getClass());
        }
        
        public static Object parse(String json) {
            // Simple JSON parsing
            json = json.trim();
            if (json.equals("null")) return null;
            if (json.startsWith("\"") && json.endsWith("\"")) {
                return json.substring(1, json.length() - 1);
            }
            if (json.equals("true")) return true;
            if (json.equals("false")) return false;
            if (json.matches("-?\\d+")) return Long.parseLong(json);
            if (json.matches("-?\\d+\\.\\d+")) return Double.parseDouble(json);
            if (json.startsWith("[") && json.endsWith("]")) {
                List<Object> list = new ArrayList<>();
                String content = json.substring(1, json.length() - 1).trim();
                if (!content.isEmpty()) {
                    // Simple array parsing
                    String[] elements = content.split(",");
                    for (String element : elements) {
                        list.add(parse(element.trim()));
                    }
                }
                return list;
            }
            if (json.startsWith("{") && json.endsWith("}")) {
                Map<String,Object> map = new HashMap<>();
                String content = json.substring(1, json.length() - 1).trim();
                if (!content.isEmpty()) {
                    // Simple object parsing
                    String[] pairs = content.split(",");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split(":");
                        String key = keyValue[0].trim();
                        if (key.startsWith("\"") && key.endsWith("\"")) {
                            key = key.substring(1, key.length() - 1);
                        }
                        map.put(key, parse(keyValue[1].trim()));
                    }
                }
                return map;
            }
            throw new RuntimeException("Invalid JSON: " + json);
        }
    }
} 