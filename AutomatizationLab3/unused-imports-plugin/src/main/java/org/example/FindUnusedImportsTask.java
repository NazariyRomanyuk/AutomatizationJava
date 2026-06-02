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
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputFiles;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.util.internal.PatternSetFactory;

import java.io.*;
import java.util.*;

public abstract class FindUnusedImportsTask extends DefaultTask {

    @InputFiles
    public abstract ConfigurableFileCollection getSources();

    @InputFiles
    public abstract RegularFileProperty getUnusedImports();

    @TaskAction
    public void findImports() {
        Map<String, List<String>> importToFileMap = new HashMap<>();

        getSources().forEach(file -> {
            try {
                CompilationUnit compilationUnit = StaticJavaParser.parse(file);
                Set<String> usedNames = new HashSet<>();
                compilationUnit.findAll(ClassOrInterfaceType.class).forEach(type -> usedNames.add(type.getNameAsString()));
                compilationUnit.findAll(AnnotationExpr.class).forEach(expr -> usedNames.add(expr.getNameAsString()));
                compilationUnit.findAll(NameExpr.class).forEach(expr -> usedNames.add(expr.getNameAsString()));
                compilationUnit.findAll(MethodCallExpr.class).forEach(expr -> usedNames.add(expr.getNameAsString()));
                NodeList<ImportDeclaration> importDeclarationNodeList = compilationUnit.getImports();
                for (ImportDeclaration declaration : importDeclarationNodeList) {
                    if (declaration.isAsterisk()) continue;
                    String name = declaration.getName().getIdentifier();
                    if (!usedNames.contains(name)) {
                        importToFileMap.computeIfAbsent(file.getAbsolutePath(), _ -> new ArrayList<>()).add(declaration.getNameAsString());
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            writeReport(importToFileMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void writeReport(Map<String, List<String>> importToFileMap) throws IOException {
        File report = getUnusedImports().get().getAsFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(report))) {
            for (String filePath : importToFileMap.keySet()) {
                writer.write("In " + filePath + ":");
                writer.newLine();
                for (String importName : importToFileMap.get(filePath)) {
                    writer.write(importName);
                    writer.newLine();
                }
                writer.newLine();
            }
        }
    }

}
