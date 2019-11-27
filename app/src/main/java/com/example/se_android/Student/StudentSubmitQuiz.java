package com.example.se_android.Student;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Models.Course;
import com.example.se_android.Models.Quiz;
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

public class StudentSubmitQuiz extends AppCompatActivity {
    ArrayList<Quiz> quizzes;
    private int score ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_student_submit_quiz);
        quizzes = (ArrayList<Quiz>) getIntent().getSerializableExtra("quiz");
        score = getIntent().getExtras().getInt("score");
        TextView myScore = findViewById(R.id.myScoreq);
        myScore.setText(score+"");
    }

    public void onClick(View view) throws ExecutionException, InterruptedException {
        SubmitQuiz submitQuiz = new SubmitQuiz(score);
        boolean isSubmit = submitQuiz.execute(quizzes).get();
        if(isSubmit){
            Intent intent = new Intent(this,StudentMainClassActivity.class);
            startActivity(intent);
        }
    }

    class SubmitQuiz extends AsyncTask<ArrayList<Quiz>,Void,Boolean>{
        private int Score;
        public SubmitQuiz(int score){
            this.Score = score;
        }
        @Override
        protected Boolean doInBackground(ArrayList<Quiz>... arrayLists) {
            ArrayList<Quiz> quizz = arrayLists[0];
            User user = User.getOurInstance();
            Course course = Course.getOurInstance();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                for (Quiz x: quizz) {
                    String sql = "UPDATE User_Quiz " +
                            "SET quiz_status = true" +
                            " WHERE user_id = "+ user.getId()+" and quiz_id = "+x.getId();
                    Log.i("DDD", "doInBackground: "+ sql);
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql);
                }
                int initscore = 0;
                String sql = "SELECT * FROM User_Class_Point WHERE user_id = "+ user.getId()+" and course_id = "+course.getId();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    initscore = resultSet.getInt("point");
                }
                sql = "UPDATE User_Class_Point SET point = "+ (initscore+this.Score) +
                        " WHERE user_id = "+ user.getId()+" and course_id = "+course.getId();
                statement = connection.createStatement();
                statement.executeUpdate(sql);

                return true;

            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
    }
}
