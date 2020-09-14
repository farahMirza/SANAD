package com.example.sanad;

public class History {
    private String provId, patID, date,  time, patLocation,patName,proName;

    public History(String provId, String patID, String date, String time, String patLocation, String patName, String proName) {
        this.provId = provId;
        this.patID = patID;
        this.date = date;
        this.time = time;
        this.patLocation = patLocation;
        this.patName = patName;
        this.proName = proName;
    }

    public String getPatName() {
        return patName;
    }

    public String getProName() {
        return proName;
    }

    public History() {
    }

    public String getProvId() {
        return provId;
    }

    public String getPatID() {
        return patID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPatLocation() {
        return patLocation;
    }
}
