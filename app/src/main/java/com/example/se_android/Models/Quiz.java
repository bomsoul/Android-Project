package com.example.se_android.Models;

import java.io.Serializable;

public class Quiz implements Serializable {
    private String question,ans1,ans2,ans3,ans4,status;
    private int realans,id,time,point;

    public Quiz(int quizID, String question, String ans1, String ans2, String ans3, String ans4, int realans, int time,int point,String status) {
        this.id = quizID;
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.realans = realans;
        this.time = time;
        this.point = point;
        this.status = status;
    }
    public Quiz(String question, String ans1, String ans2, String ans3, String ans4, int realans,int time) {
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.realans = realans;
        this.time = time;
    }

    public Quiz(int id,String question, String ans1, String ans2, String ans3, String ans4, int realans) {
        this.id= id;
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.realans = realans;
    }

    public Quiz(String question, String ans1, String ans2, String ans3, String ans4, int realans) {
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.realans = realans;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAns1() {
        return ans1;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public void setAns3(String ans3) {
        this.ans3 = ans3;
    }

    public String getAns4() {
        return ans4;
    }

    public void setAns4(String ans4) {
        this.ans4 = ans4;
    }

    public int getRealans() {
        return realans;
    }

    public void setRealans(int realans) {
        this.realans = realans;
    }

    public String getAns(){
        String ans = "";
        switch (this.realans){
            case 1: ans = getAns1();
                    break;
            case 2: ans = getAns2();
                    break;
            case 3: ans =  getAns3();
                    break;
            case 4: ans = getAns4();
                    break;
        }
        return ans;
    }

    public int getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public int getPoint() {
        return point;
    }

    public String getStatus() {
        return status;
    }
}
