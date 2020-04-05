package com.winter.model.event;

public enum BeanEvents {
    BALANCE_UPDATE("balanceUpdate"),
    PROJECT_FINISHED("projectFinished"),
    PROJECT_POINTS_UPDATE("projectPointsUpdate"),
    PROJECT_PROGRESS("projectProgress");


    private String event;

    BeanEvents(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
