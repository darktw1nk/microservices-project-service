package com.winter.model;

public enum ProjectStatus {
    IN_PROGRESS("in_progress"),
    FINISHED("finished");

    private String status;

    ProjectStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
