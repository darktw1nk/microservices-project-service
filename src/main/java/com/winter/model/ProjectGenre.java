package com.winter.model;

public enum ProjectGenre {
    NINJA("ninja"),
    MWSS("mwss"),
    GREAT_WAR("great war"),
    LIMONS("limons"),
    LOVESTORY("lovestory"),
    MEDIEVAL("medieval"),
    PRISON("prison"),
    PIGEONS("pigeons");

    private String genre;

    ProjectGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }


}
