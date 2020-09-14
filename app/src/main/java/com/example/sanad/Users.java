package com.example.sanad;

public class Users {
    private String NAME;
    private String PHOTOURI;
    private String MOBILE;
    private  String ID;
    private String GENDER;
    private String BIRTHDATE;
    private String ADDRESS;
    private String TYPE;
    private String fees;
    private String fromday;
    private String toDay;
    private String fromHour;
    private String toHour;
    private String specialization;
    private String description;
    private  String waiting_time;
    private int RatingBar_value;

    public int getRatingBar_value() {
        return RatingBar_value;
    }

    public String getWaiting_time() {
        return waiting_time;
    }

    public String getDescription() {
        return description;
    }

    public String getFees() {
        return fees;
    }

    public String getFromday() {
        return fromday;
    }

    public String getToDay() {
        return toDay;
    }

    public String getFromHour() {
        return fromHour;
    }

    public String getToHour() {
        return toHour;
    }

    public String getSpecialization() {
        return specialization;
    }

    public Users( String NAME, String PHOTOURI, String MOBILE, String ID, String GENDER, String BIRTHDATE, String ADDRESS, String TYPE, String fees, String fromday, String toDay, String fromHour, String toHour, String specialization, String description, String waiting_time, int ratingBar_value) {

        this.NAME = NAME;
        this.PHOTOURI = PHOTOURI;
        this.MOBILE = MOBILE;
        this.ID = ID;
        this.GENDER = GENDER;
        this.BIRTHDATE = BIRTHDATE;
        this.ADDRESS = ADDRESS;
        this.TYPE = TYPE;
        this.fees = fees;
        this.fromday = fromday;
        this.toDay = toDay;
        this.fromHour = fromHour;
        this.toHour = toHour;
        this.specialization = specialization;
        this.description = description;
        this.waiting_time = waiting_time;
        RatingBar_value = ratingBar_value;
    }


    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setPHOTOURI(String PHOTOURI) {
        this.PHOTOURI = PHOTOURI;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public void setBIRTHDATE(String BIRTHDATE) {
        this.BIRTHDATE = BIRTHDATE;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }


    public String getNAME() {
        return NAME;
    }

    public String getPHOTOURI() {
        return PHOTOURI;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public String getID() {
        return ID;
    }

    public String getGENDER() {
        return GENDER;
    }

    public String getBIRTHDATE() {
        return BIRTHDATE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public String getTYPE() {
        return TYPE;
    }

    public Users( String NAME, String PHOTOURI, String MOBILE, String ID, String GENDER, String BIRTHDATE, String ADDRESS, String TYPE) {

        this.NAME = NAME;
        this.PHOTOURI = PHOTOURI;
        this.MOBILE = MOBILE;
        this.ID = ID;
        this.GENDER = GENDER;
        this.BIRTHDATE = BIRTHDATE;
        this.ADDRESS = ADDRESS;
        this.TYPE = TYPE;
    }

    public Users() {
    }

}
