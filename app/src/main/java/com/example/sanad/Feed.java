package com.example.sanad;

public class Feed {
    private String name, comment;
    private  float rate;

    public Feed() {
    }

    public Feed(String name, String comment, float rate) {
        this.name = name;
        this.comment = comment;
        this.rate = rate;
    }

    public float getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }
}
