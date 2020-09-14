package com.example.sanad;

public class Appointments {
    private String patientID;
    private String providerID;
    private String provName;
    private String patientName;
    private String special;
    private String date;
    private String time;
    private String prov_location;
    private String pate_location;
    private String prov_number;
    private String pat_number;

    private double lng;
    private double longt;





    public Appointments() {
    }


    public Appointments(String patientID, String providerID, String provName, String patientName, String special, String date, String time, String prov_location, String pate_location, String prov_number, double lng, double longt) {
        this.patientID = patientID;
        this.providerID = providerID;
        this.provName = provName;
        this.patientName = patientName;
        this.special = special;
        this.date = date;
        this.time = time;
        this.prov_location = prov_location;
        this.pate_location = pate_location;
        this.prov_number = prov_number;

        this.lng = lng;
        this.longt = longt;
    }

    public double getLng() {
        return lng;
    }

    public double getLongt() {
        return longt;
    }

    public String getProv_location() {
        return prov_location;
    }

    public String getPate_location() {
        return pate_location;
    }

    public String getProv_number() {
        return prov_number;
    }

    public String getPatientName() {
        return patientName;
    }



    public String getProvName() {
        return provName;
    }

    public String getSpecial() {
        return special;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getProviderID() {
        return providerID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }


}
