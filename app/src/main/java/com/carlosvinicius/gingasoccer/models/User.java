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
    private String birthDate;
    private String password;

    public User() {
    }

    public User(String fullname, String nickname, String email, String birthDate, String password) {
        this.fullname = fullname;
        this.nickname = nickname;
        this.email = email;
        this.birthDate = birthDate;
        this.password = password;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("fullname", fullname);
        result.put("nickname", nickname);
        result.put("email", email);
        result.put("birthdate", birthDate);
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
