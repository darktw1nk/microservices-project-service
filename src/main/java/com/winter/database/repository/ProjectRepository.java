package com.winter.database.repository;

import com.winter.database.entity.Project;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface ProjectRepository extends PanacheRepository<Project> {

    Project getById(Long id);

    Project getCurrentProjectForCompany(Long companyId);

    List<Project> getProjectsInProgress();

    Project save(Project project);
}
