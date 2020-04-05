package com.winter.resources;

import com.winter.database.entity.Project;
import com.winter.network.request.CreateProjectRequest;
import com.winter.service.ProjectService;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/api/project")
public class ProjectResource {
    private static final Logger logger = Logger.getLogger(ProjectResource.class);

    @Inject
    ProjectService projectService;

    @Counted(name = "countGetProjects", description = "Counts how many times the GetProjects method has been invoked")
    @Timed(name = "timeGetProjects", description = "Times how long it takes to invoke GetProjects method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        logger.debug("get all");
        List<Project> workers = projectService.listAll();
        return Response.ok(workers).build();
    }

    @Counted(name = "countGetProject", description = "Counts how many times the countGetProject method has been invoked")
    @Timed(name = "timeGetProject", description = "Times how long it takes to invoke timeGetProject method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProject(@PathParam("id") Long id) {
        return projectService.getById(id)
                .map(worker -> Response.ok(worker).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @Counted(name = "countCreateProject", description = "Counts how many times the countCreateProject method has been invoked")
    @Timed(name = "timeCreateProject", description = "Times how long it takes to invoke timeCreateProject method", unit = MetricUnits.MILLISECONDS)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProject(CreateProjectRequest request) {
        return projectService.createNewProject(request.getName(),request.getType(),request.getGenre(),request.getCompanyId())
                .map(project -> Response.ok(project).build())
                .orElseGet(() -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }

    @Counted(name = "countGetCurrentProject", description = "Counts how many times the GetCurrentProject method has been invoked")
    @Timed(name = "timeGetCurrentProject", description = "Times how long it takes to invoke GetCurrentProject method", unit = MetricUnits.MILLISECONDS)
    @GET
    @Path("/currentProject/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentProject(@PathParam("id") String hash) {
        try {
            return projectService.getByUserHash(hash)
                    .map(project -> Response.ok(project).build())
                    .orElseGet(() -> Response.status(Response.Status.NO_CONTENT).build());
        } catch (Exception e) {
            logger.error("", e);
            return Response.serverError().build();
        }
    }
}
