package com.example.se_android.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.MainActivity;
import com.example.se_android.R;

public class StudentMainClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_student_main_class);
    }

    public void onQuizClick(View view){
        Intent intent = new Intent(this,StudentDoQuiz.class);
        startActivityForResult(intent,10);
    }

    public void onInfoClick(View view){
        Intent intent = new Intent(this,StudentInfo.class);
        startActivityForResult(intent,11);
    }

    public void onLogoutClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void BackHome(View view){
        Intent intent = new Intent();
        finish();
    }

    public void onSkillClick(View view){
        Intent intent = new Intent(this,StudentSkill.class);
        startActivityForResult(intent,12);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
