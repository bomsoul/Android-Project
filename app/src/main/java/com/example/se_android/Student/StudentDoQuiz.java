package com.example.se_android.Student;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class StudentDoQuiz extends AppCompatActivity {
    ArrayList<Quiz> quizzes;
    private TextView score,question;
    private Button ans1,ans2,ans3,ans4;
    private String realans;
    private int scorecount = 0;
    private int questionNumber = 0;
    private ProgressBar bar;
    MycountdownTimer mycountdownTimer;
    public int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_student_do_quiz);
        score = findViewById(R.id.score);
        question = findViewById(R.id.qa);
        ans1 = findViewById(R.id.an1);
        ans2 = findViewById(R.id.an2);
        ans3 = findViewById(R.id.an3);
        ans4 = findViewById(R.id.an4);
        bar = findViewById(R.id.progressBar);
        try {
            quizzes = new getQuizData().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (quizzes.isEmpty()){
            Intent intent = new Intent(this,StudentMainClassActivity.class);
            startActivity(intent);
        }
        else{
            updateQuestion();
            Log.i("TIME", "updateQuestion: " + quizzes.get(questionNumber).getId() + ","+ quizzes.get(questionNumber).getTime());
            ans1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ans1.getText() == realans){
                        scorecount += quizzes.get(questionNumber).getPoint();
                        updateScore(scorecount);

                        Toast.makeText(StudentDoQuiz.this,"Correct",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(StudentDoQuiz.this,"Wrong",Toast.LENGTH_SHORT).show();
                        updateQuestion();
                    }
                }
            });
            ans2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ans2.getText() == realans){
                        scorecount += quizzes.get(questionNumber).getPoint();
                        updateScore(scorecount);

                        Toast.makeText(StudentDoQuiz.this,"Correct",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(StudentDoQuiz.this,"Wrong",Toast.LENGTH_SHORT).show();
                        updateQuestion();
                    }
                }
            });
            ans3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ans3.getText() == realans){
                        scorecount += quizzes.get(questionNumber).getPoint();
                        updateScore(scorecount);

                        Toast.makeText(StudentDoQuiz.this,"Correct",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(StudentDoQuiz.this,"Wrong",Toast.LENGTH_SHORT).show();
                        updateQuestion();
                    }
                }
            });
            ans4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ans4.getText() == realans){
                        scorecount += quizzes.get(questionNumber).getPoint();
                        updateScore(scorecount);

                        Toast.makeText(StudentDoQuiz.this,"Correct",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(StudentDoQuiz.this,"Wrong",Toast.LENGTH_SHORT).show();
                        updateQuestion();
                    }
                }
            });
        }
    }

    private void updateScore(int scorecount){
        score.setText(scorecount+"");
        updateQuestion();
    }

    private void updateQuestion(){
        if (questionNumber <quizzes.size()){
            question.setText(quizzes.get(questionNumber).getQuestion());
            ans1.setText(quizzes.get(questionNumber).getAns1());
            ans2.setText(quizzes.get(questionNumber).getAns2());
            ans3.setText(quizzes.get(questionNumber).getAns3());
            ans4.setText(quizzes.get(questionNumber).getAns4());
            realans = quizzes.get(questionNumber).getAns();
            mycountdownTimer = new MycountdownTimer(quizzes.get(questionNumber).getTime()*1000,1000);
            mycountdownTimer.start();
            questionNumber++;
        }
        else{
            Log.i("DD", "updateQuestion: " +" NONE");
            Intent intent = new Intent(this,StudentSubmitQuiz.class);
            intent.putExtra("quiz",quizzes);
            intent.putExtra("score",scorecount);
            startActivity(intent);
        }
    }

    class getQuizData extends AsyncTask<Void,Void, ArrayList<Quiz>>{

        @Override
        protected ArrayList<Quiz> doInBackground(Void... voids) {
            User user = User.getOurInstance();
            Course course = Course.getOurInstance();
            ArrayList<Quiz> quizzes = new ArrayList<>();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                String sql = "SELECT Quiz.point,Quiz.time,User_Quiz.quiz_status,Quiz.id,Quiz.user_id ,Quiz.question,Quiz.ans1," +
                        "Quiz.ans2,Quiz.ans3,Quiz.ans4,Quiz.realans,User_Quiz.PIN,User_Quiz.quiz_status " +
                        "FROM User_Quiz INNER JOIN Quiz ON Quiz.id = User_Quiz.quiz_id " +
                        "WHERE User_Quiz.user_id = " +user.getId() ;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                Log.i("QUIZ", "doInBackground: "+course.getPIN());
                Log.i("DATA", "doInBackground: " + sql);
                while ((resultSet.next())){
                    if (resultSet.getBoolean("quiz_status") == false && resultSet.getString("PIN").equals(course.getPIN()) ){
                        String question = resultSet.getString("question");
                        String ans1 = resultSet.getString("ans1");
                        String ans2 = resultSet.getString("ans2");
                        String ans3 = resultSet.getString("ans3");
                        String ans4 = resultSet.getString("ans4");
                        int realans = resultSet.getInt("realans");
                        int quizID = resultSet.getInt("id");
                        int time = resultSet.getInt(("time"));
                        int point = resultSet.getInt("point");
                        Log.d("DD",quizID + ".)"+question +" "+ ans1 +" "+ ans2+ " " +ans3 +" "+ ans4 +"-"+ realans+".."+time);
                        quizzes.add(new Quiz(quizID,question,ans1,ans2,ans3,ans4,realans,time,point));
                    }
                 }
            }catch (Exception e){
                e.printStackTrace();
            }
            return quizzes;
        }
    }

    class MycountdownTimer extends CountDownTimer{
        int start = 0;
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MycountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            start = (int)(millisInFuture/countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) ((millisUntilFinished)/1000);
            Log.i("Count", "CountDown: " + progress);
            i++;
            try{
                bar.setProgress((int)i*100/(start));
            }catch (Exception e){

            }
        }

        @Override
        public void onFinish() {
            i = 0;
            updateQuestion();
        }
    }
}
