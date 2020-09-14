//package com.example.sanad;
//
//
//import java.util.Calendar;
//
//public class Event implements IEvent {
//
//    private long mId;
//    private Calendar mStartTime;
//    private Calendar mEndTime;
//    private String mName;
//    private String mLocation;
//    private int mColor;
//    private  Appointments p;
//
//    public Event() {
//
//    }
//
//    public Event(long mId, Calendar mStartTime, Calendar mEndTime, String mName, String mLocation,
//                 int mColor, Appointments p ) {
//        this.mId = mId;
//        this.mStartTime = mStartTime;
//        this.mEndTime = mEndTime;
//        this.mName = mName;
//        this.mLocation = mLocation;
//        this.mColor = mColor;
//        this.p = p;
//    }
//
//    public Appointments getP() {
//        return p;
//    }
//
//    public long getId() {
//        return mId;
//    }
//
//    public void setId(long id) {
//        this.mId = id;
//    }
//
//    public Calendar getStartTime() {
//        return mStartTime;
//    }
//
//    public void setStartTime(Calendar startTime) {
//        this.mStartTime = startTime;
//    }
//
//    public Calendar getEndTime() {
//        return mEndTime;
//    }
//
//    public void setEndTime(Calendar endTime) {
//        this.mEndTime = endTime;
//    }
//
//    public String getName() {
//        return mName;
//    }
//
//    public void setName(String name) {
//        this.mName = name;
//    }
//
//    public String getLocation() {
//        return mLocation;
//    }
//
//    public void setLocation(String location) {
//        this.mLocation = location;
//    }
//
//    public int getColor() {
//        return mColor;
//    }
//
//    public void setColor(int color) {
//        this.mColor = color;
//    }
//}