package com.example.se_android.Teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Adapters.StudentAdapter;
import com.example.se_android.Models.Course;
import com.example.se_android.Models.MyNumberPicker;
import com.example.se_android.Models.Student;
import com.example.se_android.Models.User;
import com.example.se_android.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.se_android.MainActivity.dbpass;
import static com.example.se_android.MainActivity.dburl;
import static com.example.se_android.MainActivity.dbuser;

public class TeacherManageStudent extends AppCompatActivity {
    ListView listView;
    GestureDetector gd;
    ArrayList<Student> students;
    StudentAdapter studentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_teacher_manage_student);
        try {
            students = new getStudetnData().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (students.isEmpty()){
            Intent intent = new Intent(this,TeacherMainClassActivity.class);
            startActivity(intent);
        }else{
            listView = findViewById(R.id.m_listView);
            studentAdapter = new StudentAdapter(this,R.layout.manage_list,students);
            listView.setAdapter(studentAdapter);
            gd = new GestureDetector(this, new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
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
                    final View mod = getLayoutInflater().inflate(R.layout.add_score,null);
                    final int index =  listView.pointToPosition( 0,(int)e.getY());
                    final MyNumberPicker numberPicker = mod.findViewById(R.id.t_picker);
                    final TextView textView = mod.findViewById(R.id.t_name);
                    textView.setText(students.get(index).getName());
                    new AlertDialog.Builder(TeacherManageStudent.this).setView(mod)
                            .setMessage("Add Score").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            View v = listView.getChildAt(index -
                                    listView.getFirstVisiblePosition());
                            if(v == null){
                                return;
                            }
                            students.get(index).addScore(numberPicker.getValue());
                            TextView textView1 = v.findViewById(R.id.std_point);
                            textView1.setText(students.get(index).getPoint()+"");
                        }
                    }).create()
                     .show();

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
    }

    public void onBackBtn(View view){
        Intent intent = new Intent(this,TeacherMainClassActivity.class);
        startActivity(intent);
    }

    public void onSubmit(View view) throws ExecutionException, InterruptedException {
        boolean status = new setData().execute(students).get();
        if(status){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            intent.putExtra("status","Update Score Complete!!");
        }else{
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            intent.putExtra("status","Update Fail !!");
        }
        finish();
    }

    class getStudetnData extends AsyncTask<Void,Void,ArrayList<Student>>{

        @Override
        protected ArrayList<Student> doInBackground(Void... voids) {
            Course course = Course.getOurInstance();
            ArrayList arrayList = new ArrayList();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                String sql = "SELECT User_Class_Point.point,Users.id,Users.name FROM Users " +
                        "INNER JOIN User_Class_Point ON Users.id = User_Class_Point.user_id " +
                        "Where User_Class_Point.course_id = "+ course.getId();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int point = resultSet.getInt("point");
                    arrayList.add(new Student(id,name,point));
                }
            }catch (Exception e ){
                e.printStackTrace();
            }

            return arrayList;
        }
    }

    class setData extends AsyncTask<ArrayList<Student>,Void,Boolean>{

        @Override
        protected Boolean doInBackground(ArrayList<Student>... arrayLists) {
            ArrayList<Student> students = arrayLists[0];
            User user = User.getOurInstance();
            Course course = Course.getOurInstance();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                for (Student x: students) {
                    String sql = "UPDATE User_Class_Point SET point ="+ x.getPoint()+
                            " Where user_id = "+ x.getId()+ " and course_id = "+ course.getId();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql);
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
    }
}
