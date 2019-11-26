package com.example.se_android.Models;

import java.io.Serializable;

public class Skill implements Serializable {
    private int id,amount;
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
}
