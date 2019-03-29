package com.carlosvinicius.gingasoccer.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TeamUser {

    private String userKey;
    private Boolean admin;

    public TeamUser() {
    }

    public TeamUser(String userKey, Boolean admin) {
        this.userKey = userKey;
        this.admin = admin;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
