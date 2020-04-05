package com.winter.service.rest;

import com.winter.model.Company;
import com.winter.model.User;
import com.winter.network.request.UpdateCompanyRequest;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Traced
@Path("/api/company")
@RegisterRestClient
public interface CompanyService {

    @GET
    @Path("/hash/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Company findCompanyByHash(@PathParam String id);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Company findCompanyById(@PathParam Long id);

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Company addCompanyBalance(UpdateCompanyRequest request);
}
