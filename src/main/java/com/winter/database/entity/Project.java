package com.winter.database.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String genre;
    private Long companyId;
    private String status;
    private Integer progress;
    private Integer designPoints;
    private Integer programmingPoints;
    private Integer marketingPoints;
    private BigDecimal revenue;

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getDesignPoints() {
        return designPoints;
    }

    public void setDesignPoints(Integer designPoints) {
        this.designPoints = designPoints;
    }

    public Integer getProgrammingPoints() {
        return programmingPoints;
    }

    public void setProgrammingPoints(Integer programmingPoints) {
        this.programmingPoints = programmingPoints;
    }

    public Integer getMarketingPoints() {
        return marketingPoints;
    }

    public void setMarketingPoints(Integer marketingPoints) {
        this.marketingPoints = marketingPoints;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
