package com.example.se_android.Student;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Adapters.StudentSkillAdapter;
import com.example.se_android.Models.Course;
import com.example.se_android.Models.Skill;
import com.example.se_android.Models.User;
import com.example.se_android.R;
import com.example.se_android.SkillShop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.se_android.MainActivity.dbpass;
import static com.example.se_android.MainActivity.dburl;
import static com.example.se_android.MainActivity.dbuser;

public class StudentSkill extends AppCompatActivity {
    ListView listView;
    List<Skill> list = new ArrayList<>();
    GestureDetector gd;
    StudentSkillAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_skill);
        listView = findViewById(R.id.student_listview);
        try {
            list = new StudentSkillTask().execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        adapter = new StudentSkillAdapter(this,R.layout.student_skill_row,list);
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
                final int index =  listView.pointToPosition( 0,(int)e.getY());
                new AlertDialog.Builder(StudentSkill.this).setTitle("Are you sure you want to use this" +list.get(index).getTitle()+"skill?")
                        .setMessage(list.get(index).getDescription())
                        .setCancelable(false)
                        .setPositiveButton("Use Skill", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                View v = listView
                                        .getChildAt(index-listView.getFirstVisiblePosition());
                                if(v == null)
                                    return;
                                if (list.get(index).getAmount() ==1){
                                    list.get(index).setAmount(0);
                                    ManageStudentSkill ms = new ManageStudentSkill();
                                    ms.execute(list.get(index));
                                    list.remove(list.get(index));
                                }
                                else{
                                    TextView amount1 = v.findViewById(R.id.amount);
                                    list.get(index).setAmount(list.get(index).getAmount() -1);
                                    ManageStudentSkill ms = new ManageStudentSkill();
                                    ms.execute(list.get(index));
                                    amount1.setText("Amount : "+list.get(index).getAmount());
                                }
                                adapter = new StudentSkillAdapter(StudentSkill.this,R.layout.student_skill_row,list);
                                listView.setAdapter(adapter);
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
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

    public void onShopClick(View view){
        Intent intent = new Intent(this, SkillShop.class);
        startActivity(intent);
    }

    public void onHomeClick(View view){
        finish();
    }

    class StudentSkillTask extends AsyncTask<Void,Void,ArrayList<Skill>>{

        @Override
        protected ArrayList<Skill> doInBackground(Void... voids) {
            ArrayList<Skill> arrayList = new ArrayList<>();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                Log.i("dd", "Query!!!");
                String sql = "SELECT User_Skill.amount,User_Skill.skill_id,Skill.title,Skill.description FROM User_Skill\n" +
                        "INNER JOIN Skill ON User_Skill.skill_id = Skill.id \n" +
                        "INNER JOIN Users ON Users.id = User_Skill.user_id\n" +
                        "INNER JOIN Courses ON Courses.id = User_Skill.course_id WHERE Courses.PIN = '"+Course.getOurInstance().getPIN()+"'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    int id = resultSet.getInt("skill_id");
                    int amount = resultSet.getInt("amount");
                    String title =resultSet.getString("title");
                    String description = resultSet.getString("description");
                    arrayList.add(new Skill(id,amount,title,description));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return arrayList;
        }
    }

    class ManageStudentSkill extends AsyncTask<Skill, Void, Void> {

        @Override
        protected Void doInBackground(Skill... skills) {
            Skill skill = skills[0];
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                String sql ="";
                if(skill.getAmount() == 0){
                    sql = "Delete From User_Skill WHERE user_id = " + User.getOurInstance().getId()
                            +" and course_id = " + Course.getOurInstance().getId();
                }
                else{
                    sql = "Update User_Skill SET amount ="+ skill.getAmount() +
                            " WHERE user_id = " + User.getOurInstance().getId()
                            +" and course_id = " + Course.getOurInstance().getId();
                }
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
