package com.example.se_android.Student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Models.Course;
import com.example.se_android.Models.User;
import com.example.se_android.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import static com.example.se_android.MainActivity.dbpass;
import static com.example.se_android.MainActivity.dburl;
import static com.example.se_android.MainActivity.dbuser;

public class StudentInfo extends AppCompatActivity {
    private TextView point,name,username,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_student_info);
        User user = User.getOurInstance();
        point = findViewById(R.id.Mypoint);
        name = findViewById(R.id.nameView);
        name.setText("Name : "+ user.getName());
        username = findViewById(R.id.usernameView);
        username.setText("Username : "+ user.getUsername());
        email = findViewById(R.id.emailView);
        email.setText("Email : "+ user.getEmail());
        try {
            String x = new Info().execute().get();
            point.setText("Class Point = " + x);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void backHome(View view){
        Intent intent = new Intent();
        finish();
    }

    public class Info extends AsyncTask<Void, Void, String> {

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Void... voids) {
            User user = User.getOurInstance();
            Course course = Course.getOurInstance();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                String sql ="SELECT User_Class_Point.point FROM User_Class_Point INNER JOIN Users ON User_Class_Point.user_id = Users.id " +
                        "INNER JOIN Courses ON User_Class_Point.course_id = Courses.id " +
                        "WHERE Users.id = "+user.getId()+" and Courses.id = "+ course.getId();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while ((resultSet.next())){
                    return resultSet.getInt("point")+"";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }

    }
}
