package org.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "codepackager")
public class CodePackagerMojo extends AbstractMojo {

    private static MavenProject project;
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

    }
    static void main(String[] args) {
        System.out.println(project);
    }
}
