package com.example.se_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Adapters.ClassListAdapter;
import com.example.se_android.Adapters.ClassListSTAdapter;
import com.example.se_android.Models.Classroom;
import com.example.se_android.Models.Course;
import com.example.se_android.Models.User;
import com.example.se_android.Student.JoinClassActivity;
import com.example.se_android.Student.StudentMainClassActivity;
import com.example.se_android.Teacher.CreateClassActivity;
import com.example.se_android.Teacher.TeacherMainClassActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.se_android.MainActivity.dbpass;
import static com.example.se_android.MainActivity.dburl;
import static com.example.se_android.MainActivity.dbuser;

public class HomeActivity extends AppCompatActivity {

    List<Classroom> list = new ArrayList<>();
    ArrayAdapter classAdapter;
    GestureDetector gd;
    User user = User.getOurInstance();
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        listView = findViewById(R.id.listVliew);
        try {
            list = new LoadClass().execute(user.getId()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (("teacher").equals(user.getRole())){
            Button button= findViewById(R.id.button);
            button.setText("Create Class");
            classAdapter = new ClassListAdapter(this,R.layout.itemclassrow,list);
        }
        else{
            Button button= findViewById(R.id.button);
            button.setText("Join Class");
            classAdapter = new ClassListSTAdapter(this,R.layout.itemclassrow_st,list);
        }

        listView.setAdapter(classAdapter);

        gd = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                final int index =  listView.pointToPosition( 0,(int)e.getY());
                Log.i("dd", "onDown: " + index);
                Course course = Course.getOurInstance();
                Classroom  classroom = list.get(index);
                course.setId(classroom.getId());
                course.setName(classroom.getClassname());
                course.setPIN(classroom.getPIN());
                if (user.getRole().equals("teacher")){
                    Intent intent = new Intent(HomeActivity.this, TeacherMainClassActivity.class);
                    startActivityForResult(intent,1);
                }
                else{
                    Intent intent = new Intent(HomeActivity.this, StudentMainClassActivity.class);
                    startActivityForResult(intent,4);
                }
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gd.onTouchEvent(motionEvent);
                return false;
            }
        });
    }

    public void onClk(View view){
        if(user.getRole().equals("teacher")){
            Intent intent = new Intent(this, CreateClassActivity.class);
            startActivityForResult(intent,0);
        }
        else{
            Intent intent = new Intent(this, JoinClassActivity.class);
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode  == RESULT_OK){
            String i = data.getStringExtra("word");
            Toast.makeText(this, i+"", Toast.LENGTH_SHORT).show();
            List<Classroom> list = null;
            try {
                list = new LoadClass().execute(user.getId()).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (requestCode == 0){
                classAdapter = new ClassListAdapter(this,R.layout.itemclassrow,list);
                Log.i("DD", "onActivityResult: "+0);
            }
            else {
                classAdapter = new ClassListSTAdapter(this,R.layout.itemclassrow_st,list);
            }
            listView.setAdapter(classAdapter);
            classAdapter.notifyDataSetChanged();

        }
    }

    class LoadClass extends AsyncTask<Integer,Void,List<Classroom>>{

        @Override
        protected List<Classroom> doInBackground(Integer... integers) {
            Integer id = integers[0];
            List <Classroom> classrooms = new ArrayList();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                Log.i("dd", "Connect Success!!");
                String sql = "SELECT Courses.id AS course_id, Courses.name AS course_name, " +
                        "Courses.code AS course_code,Courses.PIN as PIN ,Users.id AS user_id, Users.name AS user_name," +
                        "Courses.t_name AS t_name " +
                        "FROM ( ( User_Course INNER JOIN Users ON User_Course.user_id = Users.id ) " +
                        "INNER JOIN Courses ON User_Course.course_id = Courses.id ) " +
                        "WHERE Users.id = "+id;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while ((resultSet.next())){
                    Log.i("DD", "doInBackground: getData");
                    int course_id = resultSet.getInt("course_id");
                    String course_name = resultSet.getString("course_name");
                    String t_name = resultSet.getString("t_name");
                    String course_code = resultSet.getString("course_code");
                    String PIN = resultSet.getString("PIN");
                    classrooms.add(new Classroom(course_id,course_name,t_name,course_code,PIN));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return classrooms;
        }
    }
}
