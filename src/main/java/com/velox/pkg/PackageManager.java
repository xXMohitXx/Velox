package com.velox.pkg;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;
import com.velox.std.JSON;

public class PackageManager {
    private static final String PACKAGE_REGISTRY = "https://packages.velox-lang.org";
    private static final String LOCAL_PACKAGES_DIR = System.getProperty("user.home") + "/.velox/packages";
    
    public static class Package {
        private String name;
        private String version;
        private String[] dependencies;
        private String description;
        private String author;
        
        public Package(String name, String version, String[] dependencies, 
                      String description, String author) {
            this.name = name;
            this.version = version;
            this.dependencies = dependencies;
            this.description = description;
            this.author = author;
        }
        
        // Getters
        public String getName() { return name; }
        public String getVersion() { return version; }
        public String[] getDependencies() { return dependencies; }
        public String getDescription() { return description; }
        public String getAuthor() { return author; }
    }
    
    public static void install(String packageName) {
        try {
            // Create packages directory if it doesn't exist
            Files.createDirectories(Paths.get(LOCAL_PACKAGES_DIR));
            
            // Get package info from registry
            String packageInfo = downloadPackageInfo(packageName);
            Package pkg = parsePackageInfo(packageInfo);
            
            // Download and install package
            String packageUrl = PACKAGE_REGISTRY + "/download/" + packageName + "/" + pkg.getVersion();
            downloadAndInstallPackage(packageUrl, packageName);
            
            // Install dependencies
            for (String dep : pkg.getDependencies()) {
                install(dep);
            }
            
            System.out.println("Successfully installed " + packageName + " v" + pkg.getVersion());
        } catch (Exception e) {
            throw new RuntimeException("Failed to install package: " + packageName, e);
        }
    }
    
    public static void uninstall(String packageName) {
        try {
            Path packageDir = Paths.get(LOCAL_PACKAGES_DIR, packageName);
            if (Files.exists(packageDir)) {
                deleteDirectory(packageDir);
                System.out.println("Successfully uninstalled " + packageName);
            } else {
                System.out.println("Package " + packageName + " is not installed");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to uninstall package: " + packageName, e);
        }
    }
    
    public static void update(String packageName) {
        try {
            // Get latest version info
            String packageInfo = downloadPackageInfo(packageName);
            Package pkg = parsePackageInfo(packageInfo);
            
            // Uninstall current version
            uninstall(packageName);
            
            // Install latest version
            install(packageName);
            
            System.out.println("Successfully updated " + packageName + " to v" + pkg.getVersion());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update package: " + packageName, e);
        }
    }
    
    public static List<Package> listInstalled() {
        try {
            List<Package> packages = new ArrayList<>();
            File packagesDir = new File(LOCAL_PACKAGES_DIR);
            
            if (packagesDir.exists() && packagesDir.isDirectory()) {
                for (File pkgDir : packagesDir.listFiles()) {
                    if (pkgDir.isDirectory()) {
                        File infoFile = new File(pkgDir, "package.json");
                        if (infoFile.exists()) {
                            String content = new String(Files.readAllBytes(infoFile.toPath()));
                            packages.add(parsePackageInfo(content));
                        }
                    }
                }
            }
            
            return packages;
        } catch (Exception e) {
            throw new RuntimeException("Failed to list installed packages", e);
        }
    }
    
    public static List<Package> search(String query) {
        try {
            String searchUrl = PACKAGE_REGISTRY + "/search?q=" + URLEncoder.encode(query, "UTF-8");
            String response = downloadUrl(searchUrl);
            return parsePackageList(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search packages", e);
        }
    }
    
    private static String downloadPackageInfo(String packageName) throws IOException {
        String url = PACKAGE_REGISTRY + "/package/" + packageName;
        return downloadUrl(url);
    }
    
    private static void downloadAndInstallPackage(String url, String packageName) throws IOException {
        // Create package directory
        Path packageDir = Paths.get(LOCAL_PACKAGES_DIR, packageName);
        Files.createDirectories(packageDir);
        
        // Download package
        String packageData = downloadUrl(url);
        
        // Extract and install
        Map<String, Object> packageMap = (Map<String, Object>) JSON.parse(packageData);
        String sourceCode = (String) packageMap.get("source");
        
        // Write source files
        for (Map.Entry<String, Object> entry : packageMap.entrySet()) {
            if (entry.getKey().endsWith(".vlx")) {
                String fileName = entry.getKey();
                String content = (String) entry.getValue();
                Files.write(packageDir.resolve(fileName), content.getBytes());
            }
        }
        
        // Write package info
        Files.write(packageDir.resolve("package.json"), packageData.getBytes());
    }
    
    private static String downloadUrl(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
    
    private static Package parsePackageInfo(String json) {
        Map<String, Object> info = (Map<String, Object>) JSON.parse(json);
        return new Package(
            (String) info.get("name"),
            (String) info.get("version"),
            ((List<String>) info.get("dependencies")).toArray(new String[0]),
            (String) info.get("description"),
            (String) info.get("author")
        );
    }
    
    private static List<Package> parsePackageList(String json) {
        List<Package> packages = new ArrayList<>();
        List<Map<String, Object>> list = (List<Map<String, Object>>) JSON.parse(json);
        for (Map<String, Object> info : list) {
            packages.add(new Package(
                (String) info.get("name"),
                (String) info.get("version"),
                ((List<String>) info.get("dependencies")).toArray(new String[0]),
                (String) info.get("description"),
                (String) info.get("author")
            ));
        }
        return packages;
    }
    
    private static void deleteDirectory(Path directory) throws IOException {
        Files.walk(directory)
            .sorted(Comparator.reverseOrder())
            .forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete: " + path, e);
                }
            });
    }
} 