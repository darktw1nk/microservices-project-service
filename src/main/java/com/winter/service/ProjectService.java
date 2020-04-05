package com.winter.service;

import com.winter.database.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    List<Project> listAll();

    Optional<Project> getById(Long id);

    Optional<Project> createNewProject(String name,String type,String genre,Long companyId);

    Optional<Project> getByUserHash(String hash);

    List<Project> getProjectsInProgress();

    Optional<Project> save(Project project);
}
