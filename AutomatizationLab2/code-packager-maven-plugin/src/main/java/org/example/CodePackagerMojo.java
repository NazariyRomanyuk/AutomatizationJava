package org.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Mojo(name = "code-packager", defaultPhase = LifecyclePhase.VALIDATE)
public class CodePackagerMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Path output = Path.of(project.getBuild().getDirectory()).resolve("CodePackager.txt");
        try {
            Files.createDirectories(output.getParent());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output.toFile()))) {
                List<String> sources = project.getCompileSourceRoots();
                for (String source : sources) {
                    try (Stream<Path> stream = Files.walk(Path.of(source))) {
                        List<Path> javaFiles = stream.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).toList();
                        for (Path path : javaFiles) {
                            for (String line : Files.readAllLines(path)) {
                                writer.write(line);
                                writer.newLine();
                            }
                        }
                    }
                }
            }
            getLog().info("Code packaged into target/CodePackager.txt");
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

}
