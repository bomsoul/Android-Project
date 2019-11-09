package com.example.se_android.Models;

public class UserCourse {
    int course_id,user_id;
    String PIN;

    public UserCourse(int course_id, int user_id, String PIN) {
        this.course_id = course_id;
        this.user_id = user_id;
        this.PIN = PIN;
    }

    public int getCourse_id() {
        return course_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPIN() {
        return PIN;
    }
}
