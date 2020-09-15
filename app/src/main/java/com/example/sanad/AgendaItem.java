package com.example.sanad;

public class AgendaItem {
    private String date;
    private String time;
    private String prov_location;
    private String pate_location;
    String pat_ID;
    String pat_name;
    private String pat_number;
    private double lng;
    private double longt;

    public AgendaItem() {
    }

    public AgendaItem(String name,String pat_ID, String date, String time, String prov_location, String pate_location, String pat_number, double lng, double longt) {
        this.date = date;
        this.time = time;
        this.lng = lng;
        this.pat_name=name;

        this.longt = longt;
        this.pat_number = pat_number;
        this.pate_location = pate_location;
        this.prov_location = prov_location;
        this.pat_ID = pat_ID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPat_ID() {
        return pat_ID;
    }

    public String getPat_name() {
        return pat_name;
    }

    public String getProv_location() {
        return prov_location;
    }

    public String getPate_location() {
        return pate_location;
    }

    public String getPat_number() {
        return pat_number;
    }

    public double getLng() {
        return lng;
    }

    public double getLongt() {
        return longt;
    }
}
