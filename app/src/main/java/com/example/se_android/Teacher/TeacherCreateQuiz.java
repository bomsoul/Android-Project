package com.example.se_android.Teacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Adapters.QuizAdapter;
import com.example.se_android.Models.Course;
import com.example.se_android.Models.MyNumberPicker;
import com.example.se_android.Models.Quiz;
import com.example.se_android.Models.User;
import com.example.se_android.Models.UserCourse;
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

public class TeacherCreateQuiz extends AppCompatActivity {

    ArrayList<Quiz> quizs = new ArrayList<>();
    ListView listView;
    GestureDetector gd;
    QuizAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_teacher_create_quiz);
        listView = findViewById(R.id.listView1);
        adapter = new QuizAdapter(this,R.layout.quiz_row,quizs);
        listView.setAdapter(adapter);
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

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gd.onTouchEvent(event);
                return false;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void onClk(View view){
        final View diall =  getLayoutInflater().inflate(R.layout.quiz_template_row,null);
        new AlertDialog.Builder(TeacherCreateQuiz.this).setView(diall)
                .setMessage("Add your quiz").setCancelable(false).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = diall.findViewById(R.id.editText3);
                        EditText editText1 = diall.findViewById(R.id.editText4);
                        EditText editText2 = diall.findViewById(R.id.editText5);
                        EditText editText3 = diall.findViewById(R.id.editText6);
                        EditText editText4 = diall.findViewById(R.id.editText7);
                        MyNumberPicker numberPicker = diall.findViewById(R.id.nPicker);
//                        numberPicker.setMinValue(1);
//                        numberPicker.setMaxValue(4);
                        quizs.add(new Quiz(editText.getText().toString()
                                ,editText1.getText().toString(),editText2.getText().toString(),
                                editText3.getText().toString(),editText4.getText().toString(),
                                numberPicker.getValue()));
                        adapter.notifyDataSetChanged();
                    }
                }
        ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    public void onSubmitQuiz(View view){
        try {
            String show = new AddQuizToDB().execute(quizs).get();
            Intent intent= new Intent();
            setResult(RESULT_OK,intent);
            intent.putExtra("status",show);
            finish();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onBack(View view){
        Intent intent= new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
    class AddQuizToDB extends AsyncTask<ArrayList<Quiz>,Void,String>{

        @Override
        protected String doInBackground(ArrayList<Quiz>... arrayLists) {
            User user = User.getOurInstance();
            Course course = Course.getOurInstance();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                String sql = "Select * From User_Course Where course_id = "+ course.getId();
                Statement statement = connection.createStatement();
                System.out.println(sql);
                ResultSet resultSet = statement.executeQuery(sql);
                ArrayList<UserCourse> userCourses = new ArrayList<>();
                while (resultSet.next()){
                    userCourses.add(new UserCourse(resultSet.getInt("course_id"),resultSet.getInt("user_id"),resultSet.getString("class_PIN")));
                }
                sql = "SELECT MAX(data_set) AS dataset FROM Quiz Where pin ='"+ course.getPIN()+"'";
                resultSet = statement.executeQuery(sql);
                int dataset = -1;
                while (resultSet.next()){
                    dataset = resultSet.getInt("dataset");
                }
                System.out.println("Dataset =" + dataset);
                for (Quiz x: arrayLists[0]) {
                    sql = "INSERT INTO Quiz VALUES (null,'"+x.getQuestion()+"','"+x.getAns1()+
                            "','"+x.getAns2()+"','"+x.getAns3()+ "','"+x.getAns4()+
                            "','"+x.getRealans()+"','"+user.getId()+"','"+ course.getPIN() +"',"+(dataset+1)+")";
                    statement = connection.createStatement();
                    statement.executeUpdate(sql);
                }
                System.out.println("Dataset2 =" + dataset);
                sql = "Select * From Quiz Where pin = '"+ course.getPIN()+"' and data_set = " + (dataset+1);
                ArrayList<Integer> quiz_ids = new ArrayList<>();
                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                System.out.println(sql);
                while (resultSet.next()){
                    quiz_ids.add(resultSet.getInt("id"));
                }
                for (Integer x: quiz_ids) {
                    System.out.println(x);
                }
                if (dataset != -1){
                    for (UserCourse x: userCourses) {
                        for (Integer n: quiz_ids) {
                            sql = "INSERT INTO User_Quiz VALUES("+x.getUser_id()+",false,'"+x.getPIN()+"',"+n+")";
                            statement = connection.createStatement();
                            statement.executeUpdate(sql);
                        }
                    }
                }
                return "Create Quiz Success!!!";

            }catch (Exception e){
                e.printStackTrace();
            }
            return "FAIL To Add Data";
        }
    }
}
