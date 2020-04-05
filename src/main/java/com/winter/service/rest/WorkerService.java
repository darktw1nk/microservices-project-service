package com.winter.service.rest;

import com.winter.model.Worker;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Traced
@Path("/api/worker")
@RegisterRestClient
public interface WorkerService {

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Worker> getWorkersByIds(@QueryParam("ids") List<Long> ids);
}
