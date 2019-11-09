package com.example.se_android.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.HomeActivity;
import com.example.se_android.MainActivity;
import com.example.se_android.R;

public class TeacherMainClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_teacher_main_class);
    }

    public void clk(View view){
        Intent intent = new Intent(this,TeacherCreateQuiz.class);
        startActivityForResult(intent,1);
    }

    public void clkPIN(View view){
        Intent intent = new Intent(this,TeacherShowPin.class);
        startActivityForResult(intent,2);
    }

    public void BackHome(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void ClickLogout(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onManageBtn(View view){
        Intent intent = new Intent(this,TeacherManageStudent.class);
        startActivityForResult(intent,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, data.getStringExtra("status"), Toast.LENGTH_SHORT).show();
    }
}
