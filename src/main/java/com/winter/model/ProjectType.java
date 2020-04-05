package com.winter.model;

public enum ProjectType {
    RPG("rpg"),
    SHOOTER("shooter"),
    STRATEGY("strategy"),
    ACTION("action"),
    PLATFORMER("platformer");

    private String type;

    ProjectType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
