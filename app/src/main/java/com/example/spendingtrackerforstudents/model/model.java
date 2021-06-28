package com.example.spendingtrackerforstudents.model;

public class model {

    private int amount;
    private String type;
    private String note;
    private String id;

    public model(int amount, String type, String note, String id, String date) {
        this.amount = amount;
        this.date = date;
        this.id = id;
        this.note = note;
        this.type = type;



    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public model(){}


}