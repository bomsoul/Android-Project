package com.example.se_android.Models;

public class Classroom {
    private int id;
    private String classname;
    private String teacher;
    private String code;
    private String PIN;

    public Classroom(int id, String classname, String teacher,String code,String PIN) {
        this.id = id;
        this.classname = classname;
        this.teacher = teacher;
        this.code = code;
        this.PIN = PIN;
    }

    public int getId() {
        return id;
    }

    public String getClassname() {
        return classname;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getCode() {
        return code;
    }

    public String getPIN() {
        return PIN;
    }
}
