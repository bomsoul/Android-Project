package com.example.se_android.Models;

public class Student {
    private int id;
    private String name;
    private int point;

    public Student(int id, String name, int point) {
        this.id = id;
        this.name = name;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }

    public void addScore(int score){
        this.point += score;
    }
}
