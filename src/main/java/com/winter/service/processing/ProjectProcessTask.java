package com.winter.service.processing;

import com.winter.database.entity.Project;
import com.winter.model.*;
import com.winter.model.event.*;
import com.winter.model.kafka.ProjectStatistic;
import com.winter.network.request.UpdateCompanyRequest;
import com.winter.resources.ProjectResource;
import com.winter.service.ProjectService;
import com.winter.service.rest.CompanyService;
import com.winter.service.rest.WorkerService;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import io.vertx.core.eventbus.EventBus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Dependent
public class ProjectProcessTask implements Runnable {
    private static final Logger logger = Logger.getLogger(ProjectProcessTask.class);

    @Inject
    @RestClient
    CompanyService companyService;

    @Inject
    @RestClient
    WorkerService workerService;

    @Inject
    ProjectService projectService;

    @Inject
    EventBus eventBus;

    @Inject
    @Channel("project-statistics")
    Emitter<ProjectStatistic> emitter;

    private Project project;


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public void run() {
        try {
            logger.error("run projectUpdate task for project: " + project.getId());
            Company company = companyService.findCompanyById(project.getCompanyId());
            logger.error("got company");
            List<Long> programmersIds = company.getProgrammers().stream().map(Programmer::getWorkerId).collect(Collectors.toList());
            List<Long> designersIds = company.getDesigners().stream().map(Designer::getWorkerId).collect(Collectors.toList());
            List<Long> marketersIds = company.getMarketers().stream().map(Marketer::getWorkerId).collect(Collectors.toList());

            List<Long> allIds = new ArrayList<>();
            allIds.addAll(programmersIds);
            allIds.addAll(designersIds);
            allIds.addAll(marketersIds);

            //should be faster then 3 network calls, while with updateProjectPoints it`s worst o(n^2), but n should be 60 max
            List<Worker> workers = workerService.getWorkersByIds(allIds);


            project.setProgress(project.getProgress() + 20);
            sendProjectProgressEvent(project.getCompanyId(),project.getProgress());
            if (project.getProgress() >= 100) {
                project.setProgress(100);
                project.setStatus(ProjectStatus.FINISHED.getStatus());
            }

            updateProjectPoints(project, workers, programmersIds, designersIds, marketersIds);
            projectService.save(project);

            sendSalaryUpdate(project, workers);
            if (project.getStatus().equals(ProjectStatus.FINISHED.getStatus())) {
                processRevenueUpdate(project);
                sendProjectFinishedEvent(company.getId());
                sendProjectStatistic(project,company);
            }
            logger.error("project update success");
        } catch (Exception e){
            logger.error("",e);
        }
    }

    private void sendProjectStatistic(Project project,Company company){
        ProjectStatistic statistic = new ProjectStatistic();
        statistic.setCompanyName(company.getName());
        statistic.setProjectId(project.getId());
        statistic.setName(project.getName());
        statistic.setRevenue(project.getRevenue());
        emitter.send(statistic);
    }

    private void sendProjectProgressEvent(Long companyId,Integer progress){
        ProjectProgressEvent event = new ProjectProgressEvent();
        event.setCompanyId(companyId);
        event.setProgress(progress);
        eventBus.send(BeanEvents.PROJECT_PROGRESS.getEvent(),event);
    }

    private void sendBalanceUpdate(Long companyId, BigDecimal balanceUpdate) {
        BalanceUpdateEvent event = new BalanceUpdateEvent();
        event.setCompanyId(companyId);
        event.setBalanceUpdate(balanceUpdate);
        eventBus.send(BeanEvents.BALANCE_UPDATE.getEvent(), event);
    }

    private void sendPointsUpdateEvent(Long companyId, Project project) {
        ProjectPointsEvent event = new ProjectPointsEvent();
        event.setCompanyId(companyId);
        event.setDesignPoints(project.getDesignPoints());
        event.setProgrammingPoints(project.getProgrammingPoints());
        event.setMarketingPoints(project.getMarketingPoints());
        eventBus.send(BeanEvents.PROJECT_POINTS_UPDATE.getEvent(), event);
    }

    private void sendProjectFinishedEvent(Long companyId) {
        ProjectFinishedEvent event = new ProjectFinishedEvent();
        event.setCompanyId(companyId);
        event.setStatus(ProjectStatus.FINISHED.getStatus());
        eventBus.send(BeanEvents.PROJECT_FINISHED.getEvent(), event);
    }

    private void updateProjectPoints(Project project, List<Worker> workers, List<Long> programmersIds, List<Long> designersIds, List<Long> marketersIds) {
        List<Worker> programmers = workers.stream().filter(worker -> programmersIds.contains(worker.getId())).collect(Collectors.toList());
        List<Worker> designers = workers.stream().filter(worker -> designersIds.contains(worker.getId())).collect(Collectors.toList());
        List<Worker> marketers = workers.stream().filter(worker -> marketersIds.contains(worker.getId())).collect(Collectors.toList());

        long programmingPoints = programmers.stream().mapToLong(Worker::getProgramming).sum();
        long designPoints = designers.stream().mapToLong(Worker::getDesign).sum();
        long marketingPoints = marketers.stream().mapToLong(Worker::getMarketing).sum();

        project.setProgrammingPoints(project.getProgrammingPoints() + (int) programmingPoints);
        project.setDesignPoints(project.getDesignPoints() + (int) designPoints);
        project.setMarketingPoints(project.getMarketingPoints() + (int) marketingPoints);

        sendPointsUpdateEvent(project.getCompanyId(), project);
    }

    private void processRevenueUpdate(Project project) {
        BigDecimal revenue = new BigDecimal("0").setScale(2, RoundingMode.HALF_DOWN);
        revenue = revenue.add(new BigDecimal(String.valueOf(project.getProgrammingPoints() * 100)));
        revenue = revenue.add(new BigDecimal(String.valueOf(project.getDesignPoints() * 100)));
        revenue = revenue.add(new BigDecimal(String.valueOf(project.getMarketingPoints() * 100)));
        UpdateCompanyRequest request = new UpdateCompanyRequest();
        request.setCompanyId(project.getCompanyId());
        request.setMoney(revenue);
        companyService.addCompanyBalance(request);
        project.setRevenue(revenue);
        projectService.save(project);
        sendBalanceUpdate(project.getCompanyId(),revenue);
    }

    public void sendSalaryUpdate(Project project, List<Worker> workers) {
        BigDecimal salary = getSalary(workers);
        UpdateCompanyRequest request = new UpdateCompanyRequest();
        request.setCompanyId(project.getCompanyId());
        request.setMoney(salary.negate());
        companyService.addCompanyBalance(request);

        sendBalanceUpdate(project.getCompanyId(),salary.negate());
    }

    public BigDecimal getSalary(List<Worker> workers) {
        BigDecimal salary = new BigDecimal("0").setScale(2);
        for (int i = 0; i < workers.size(); i++) {
            salary = salary.add(workers.get(i).getSalary());
        }
        return salary;
    }
}
