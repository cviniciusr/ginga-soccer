package com.carlosvinicius.gingasoccer.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class UsersTeam {

    private String teamKey;
    private List<TeamUser> teamUsers;

    public UsersTeam() {
    }

    public UsersTeam(String teamKey, List<TeamUser> teamUsers) {
        this.teamKey = teamKey;
        this.teamUsers = teamUsers;
    }

    public String getTeamKey() {
        return teamKey;
    }

    public void setTeamKey(String teamKey) {
        this.teamKey = teamKey;
    }

    public List<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }
}
