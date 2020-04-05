package com.winter.database.repository;

import com.winter.database.entity.Project;
import com.winter.model.ProjectStatus;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import io.quarkus.panache.common.Parameters;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@Traced
@ApplicationScoped
public class ProjectRepositoryImpl implements ProjectRepository {

    public Project getById(Long id) throws NoResultException {
        return find("id=:id", Parameters.with("id", id)).singleResult();
    }

    @Override
    public Project getCurrentProjectForCompany(Long companyId) {
        return find("status = :status and companyId = :companyId", Parameters.with("status", ProjectStatus.IN_PROGRESS.getStatus()).and("companyId", companyId)).singleResult();
    }

    @Override
    public List<Project> getProjectsInProgress() {
        return find("status", ProjectStatus.IN_PROGRESS.getStatus()).list();
    }

    @Transactional
    public Project save(Project project){
        EntityManager em = JpaOperations.getEntityManager();
        if (project.getId() == null) {
            em.persist(project);
            return project;
        } else {
            return em.merge(project);

        }
    }
}
