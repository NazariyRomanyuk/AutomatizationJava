package org.example;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputFiles;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class RemoveUnusedImportsTask extends DefaultTask {

    @InputFiles
    public abstract ConfigurableFileCollection getSources();

    @InputFiles
    public abstract RegularFileProperty getUnusedImports();

    @TaskAction
    public void removeImports() {
        try {
            Map<String, List<String>> imports = unusedImports();
            getSources().forEach(file -> {
                try {
                    if (imports.containsKey(file.getAbsolutePath())) {
                        List<String> filteredLines;
                        try (Stream<String> lines = Files.lines(file.toPath())) {
                            List<String> unusedForFile = imports.get(file.getAbsolutePath());
                            filteredLines = lines.filter(line -> unusedForFile.stream().noneMatch(line::contains)).collect(Collectors.toList());
                        }
                        Files.write(file.toPath(), filteredLines);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, List<String>> unusedImports() throws IOException {
        Map<String, List<String>> imports = new HashMap<>();
        try (Stream<String> lines = Files.lines(getUnusedImports().get().getAsFile().toPath())) {
            String currentFile = "";
            for (String line: lines.toList()) {
                if (line.startsWith("In")) {
                    String[] parsed = line.split(" ");
                    currentFile = parsed[1].substring(0, parsed[1].length()-1);
                }
                else if (!line.isEmpty()) {
                    imports.computeIfAbsent(currentFile, _ -> new ArrayList<>()).add(line);
                }
            }
        }
        return imports;
    }



}
