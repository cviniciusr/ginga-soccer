package com.carlosvinicius.gingasoccer.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

@IgnoreExtraProperties
public class Team implements Serializable {

    private String name;
    private String weekday;
    private String startTime;
    private String endTime;
    private String address;

    public Team() {
    }

    public Team(String name, String weekday, String startTime, String endTime, String address) {
        this.name = name;
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
