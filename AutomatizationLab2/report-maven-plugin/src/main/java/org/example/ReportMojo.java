package org.example;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Mojo(name = "report", defaultPhase = LifecyclePhase.VALIDATE)
public class ReportMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        for (String sourceRoot : project.getCompileSourceRoots()) {
            builder.addSourceTree(new File(sourceRoot));
        }

        getLog().info("=== PROJECT REPORT ===");
        int classCount = builder.getClasses().size();
        Collection<JavaClass> javaClasses = builder.getClasses();
        int methodCount = 0;
        for (JavaClass javaClass : javaClasses) {
            methodCount += javaClass.getMethods().size();
        }
        getLog().info("Classes count: " + classCount);
        getLog().info("Methods count: " + methodCount);
        getLog().info("=== TODO IN FILES ===");

        List<String> sources = project.getCompileSourceRoots();
        for (String source : sources) {
            try (Stream<Path> stream = Files.walk(Path.of(source))) {
                List<Path> javaFiles = stream.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).toList();
                for (Path path : javaFiles) {
                    getLog().info("TODO in " + path.toString() + ": " + countTodoInFile(path));
                }
            } catch (IOException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        }
    }

    private int countTodoInFile(Path path) throws IOException {
        boolean inBlockComment = false;
        int todoCount = 0;
        for (String line : Files.readAllLines(path)) {
            String trimmed = line.stripLeading();
            if (inBlockComment && trimmed.contains("TODO")) {
                todoCount++;
                if (trimmed.contains("*/")) inBlockComment = false;
            }
            else if ((trimmed.startsWith("/*") || trimmed.startsWith("/**") && trimmed.contains("TODO"))) {
                todoCount++;
                if (!trimmed.contains("*/")) inBlockComment = true;
            }
            else if (trimmed.startsWith("//") && trimmed.contains("TODO")) {
                todoCount++;
            }
        }
        return todoCount;
    }

}
