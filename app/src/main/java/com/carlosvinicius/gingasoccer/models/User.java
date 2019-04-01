package com.carlosvinicius.gingasoccer.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User implements Serializable {

    private String fullname;
    private String nickname;
    private String email;
    private String birthdate;
    private String password;
    private Boolean active;
    private String playerKey;
    private Boolean attend;

    public User() {
    }

    public User(String fullname, String nickname, String email, String birthdate, String password) {
        this.fullname = fullname;
        this.nickname = nickname;
        this.email = email;
        this.birthdate = birthdate;
        this.password = password;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("fullname", fullname);
        result.put("nickname", nickname);
        result.put("email", email);
        result.put("birthdate", birthdate);
        result.put("password", password);

        return result;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPlayerKey() {
        return playerKey;
    }

    public void setPlayerKey(String playerKey) {
        this.playerKey = playerKey;
    }

    public Boolean isAttend() {
        return attend;
    }

    public void setAttend(Boolean attend) {
        this.attend = attend;
    }
}
