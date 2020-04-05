package com.winter.service;

import com.winter.database.entity.Project;
import com.winter.database.repository.ProjectRepository;
import com.winter.model.Company;
import com.winter.model.ProjectStatus;
import com.winter.model.Worker;
import com.winter.resources.ProjectResource;
import com.winter.service.rest.CompanyService;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Traced
@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {
    private static final Logger logger = Logger.getLogger(ProjectServiceImpl.class);

    @Inject
    ProjectRepository projectRepository;

    @Inject
    @RestClient
    CompanyService companyService;

    @Override
    public List<Project> listAll() {
        return projectRepository.listAll();
    }

    @Override
    public Optional<Project> getById(Long id) {
        Project project = null;
        try {
            project = projectRepository.getById(id);
        } catch (NoResultException e) {
            logger.error("", e);
        }
        return Optional.ofNullable(project);
    }

    @Transactional
    @Override
    public Optional<Project> createNewProject(String name, String type, String genre, Long companyId) {
        Project project = new Project();
        project.setName(name);
        project.setType(type);
        project.setGenre(genre);
        project.setCompanyId(companyId);
        project.setStatus(ProjectStatus.IN_PROGRESS.getStatus());
        project.setProgress(0);
        project.setDesignPoints(0);
        project.setProgrammingPoints(0);
        project.setMarketingPoints(0);
        project.setRevenue(BigDecimal.ZERO);
        projectRepository.persist(project);

        return Optional.of(project);
    }

    @Override
    public Optional<Project> getByUserHash(String hash) {
        Company company = companyService.findCompanyByHash(hash);
        if (company != null) {
            Project project = projectRepository.getCurrentProjectForCompany(company.getId());
            return Optional.ofNullable(project);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Project> getProjectsInProgress() {
        return projectRepository.getProjectsInProgress();
    }

    public Optional<Project> save(Project project){
        return Optional.ofNullable(projectRepository.save(project));
    }
}
