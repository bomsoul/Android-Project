package com.example.se_android.Models;

import java.io.Serializable;

public class Skill implements Serializable {
    private int id,amount,point;
    String title,description;

    public Skill(int id, int amount, String title,String description) {
        this.id = id;
        this.amount = amount;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }


    public int getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", amount=" + amount +
                ", point=" + point +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
