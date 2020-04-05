package com.winter.model.event;

public class ProjectFinishedEvent implements BeanEvent {
    private String type;
    private Long companyId;
    private String status;

    public ProjectFinishedEvent() {
        this.type = BeanEvents.PROJECT_FINISHED.getEvent();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
