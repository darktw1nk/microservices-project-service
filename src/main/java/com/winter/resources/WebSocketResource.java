package com.winter.resources;

import com.winter.model.Company;
import com.winter.model.event.*;
import com.winter.service.processing.ProjectProcessTask;
import com.winter.service.rest.CompanyService;
import io.quarkus.vertx.ConsumeEvent;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/api/project/websocket/{username}")
@ApplicationScoped
public class WebSocketResource {
    private static final Logger logger = Logger.getLogger(WebSocketResource.class);

    @Inject
    @RestClient
    CompanyService companyService;

    //for now we store it indefinitely, in production it should be cache with calls to two different services if value not found
    Map<Long, String> companyUserCache = new ConcurrentHashMap<>();
    Map<String, Session> sessions = new ConcurrentHashMap<>();
    private Jsonb jsonb = JsonbBuilder.create();

    @ConsumeEvent(value = "balanceUpdate", blocking = true)
    public void balanceUpdate(BalanceUpdateEvent event) {
        sendEvent(event);
    }

    @ConsumeEvent(value = "projectFinished", blocking = true)
    public void projectFinished(ProjectFinishedEvent event) {
        sendEvent(event);
    }

    @ConsumeEvent(value = "projectPointsUpdate", blocking = true)
    public void projectPointsUpdate(ProjectPointsEvent event) {
        sendEvent(event);
    }

    @ConsumeEvent(value = "projectProgress", blocking = true)
    public void projectProgressUpdate(ProjectProgressEvent event) {
        sendEvent(event);
    }

    private void sendEvent(BeanEvent event) {
        String user = companyUserCache.get(event.getCompanyId());
        if (user != null) {
            Session session = sessions.get(user);
            if (session != null) {
                String eventJson = jsonb.toJson(event);
                logger.error("sending json: " + eventJson);
                session.getAsyncRemote().sendObject(eventJson, result -> {
                    if (result.getException() != null) {
                        logger.error("Unable to send message: " + result.getException());
                    }
                });
            }
        }

    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        Company company = companyService.findCompanyByHash(username);
        companyUserCache.put(company.getId(), username);
        sessions.put(username, session);
        //  broadcast("User " + username + " joined");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        //  broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        //  broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        //broadcast(">> " + username + ": " + message);
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    logger.error("Unable to send message: " + result.getException());
                }
            });
        });
    }

}
