package com.winter.model;

public enum UserStatus {
    REGISTERED("registered"),
    ACTIVATED("activated");

    private String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
