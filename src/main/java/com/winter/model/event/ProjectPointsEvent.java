package com.winter.model.event;

public class ProjectPointsEvent implements BeanEvent{
    private Long companyId;
    private Integer programmingPoints;
    private Integer designPoints;
    private Integer marketingPoints;
    private String type;

    public ProjectPointsEvent() {
        this.type = BeanEvents.PROJECT_POINTS_UPDATE.getEvent();
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

    public Integer getProgrammingPoints() {
        return programmingPoints;
    }

    public void setProgrammingPoints(Integer programmingPoints) {
        this.programmingPoints = programmingPoints;
    }

    public Integer getDesignPoints() {
        return designPoints;
    }

    public void setDesignPoints(Integer designPoints) {
        this.designPoints = designPoints;
    }

    public Integer getMarketingPoints() {
        return marketingPoints;
    }

    public void setMarketingPoints(Integer marketingPoints) {
        this.marketingPoints = marketingPoints;
    }
}
