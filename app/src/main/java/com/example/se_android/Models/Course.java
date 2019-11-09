package com.example.se_android.Models;

public class Course {

    private int id;
    private String name,PIN;
    private static final Course ourInstance = new Course();

    static Course getInstance() {
        return ourInstance;
    }

    private Course() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public static Course getOurInstance() {
        return ourInstance;
    }
}
