package com.example.se_android.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Models.Course;
import com.example.se_android.R;

public class TeacherShowPin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_teacher_show_pin);
        TextView textView = findViewById(R.id.classPIN);
        Course course = Course.getOurInstance();
        textView.setText(course.getPIN());
    }

    public void Back(View view){
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
