package com.winter.service.processing;

import com.winter.database.entity.Project;
import com.winter.model.ProjectStatus;
import com.winter.service.ProjectService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ProjectProcessor {
    ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Inject
    ProjectService projectService;
    @Inject
    ProjectProcessTask projectProcessTask;

    @Scheduled(every = "10s")
    public void processProjects() {
        List<Project> projects = projectService.getProjectsInProgress();

        projects.forEach(project -> {
            projectProcessTask.setProject(project);
            executorService.execute(projectProcessTask);
        });
    }

}
