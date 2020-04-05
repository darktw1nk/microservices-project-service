package com.winter.model.event;

public class ProjectProgressEvent implements BeanEvent {
    private Long companyId;
    private Integer progress;
    private String type;

    public ProjectProgressEvent() {
        this.type=BeanEvents.PROJECT_PROGRESS.getEvent();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
