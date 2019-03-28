package com.carlosvinicius.gingasoccer.models;

import java.util.List;

public class UsersTeam {

    private String teamKey;
    private List<String> usersKey;

    public UsersTeam() {
    }

    public UsersTeam(String teamKey, List<String> usersKey) {
        this.teamKey = teamKey;
        this.usersKey = usersKey;
    }

    public String getTeamKey() {
        return teamKey;
    }

    public void setTeamKey(String teamKey) {
        this.teamKey = teamKey;
    }

    public List<String> getUsersKey() {
        return usersKey;
    }

    public void setUsersKey(List<String> usersKey) {
        this.usersKey = usersKey;
    }
}
