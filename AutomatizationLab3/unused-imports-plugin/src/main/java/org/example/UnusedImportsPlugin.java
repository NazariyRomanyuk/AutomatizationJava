package org.example;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

public class UnusedImportsPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        TaskProvider<FindUnusedImportsTask> findTask = target.getTasks().register("findUnusedImports", FindUnusedImportsTask.class, task -> {
            SourceSetContainer sourceSets = target.getExtensions().getByType(SourceSetContainer.class);
            SourceSet mainSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
            task.getSources().from(mainSourceSet.getAllJava());
            task.getUnusedImports().convention(target.getLayout().getBuildDirectory().file("unused_imports.txt"));
        });
        target.getTasks().register("removeUnusedImports", RemoveUnusedImportsTask.class, task -> {
            SourceSetContainer sourceSets = target.getExtensions().getByType(SourceSetContainer.class);
            SourceSet mainSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
            task.getSources().from(mainSourceSet.getAllJava());
            task.getUnusedImports().convention(target.getLayout().getBuildDirectory().file("unused_imports.txt"));
            task.dependsOn(findTask);
        });
    }
}