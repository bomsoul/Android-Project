package com.example.se_android;

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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Adapters.SkillShopAdapter;
import com.example.se_android.Models.Course;
import com.example.se_android.Models.Skill;
import com.example.se_android.Models.User;
import com.example.se_android.Student.StudentMainClassActivity;
import com.example.se_android.Student.StudentSkill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.example.se_android.MainActivity.dbpass;
import static com.example.se_android.MainActivity.dburl;
import static com.example.se_android.MainActivity.dbuser;

public class SkillShop extends AppCompatActivity {
    ListView listView;
    List<Skill> skillshop,itemmall;
    TextView point,bucket;
    private int datapoint = 0;
    SkillShopAdapter adapter ;
    GestureDetector gd;
    Map<String,Skill> myitem ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_shop);
        skillshop = (ArrayList<Skill>) getIntent().getExtras().getSerializable("item");
        myitem = new HashMap<>();
        for (Skill x: skillshop) {
            myitem.put("edit",x);
        }
        point = findViewById(R.id.your_point);
        bucket = findViewById(R.id.bucket);
        try {
            datapoint = new StudentPoint().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        point.setText("Point : " + datapoint);
        bucket.setText("Item in bucket : " + 0);
        try {
            itemmall = new getSkillData().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listView = findViewById(R.id.skillshoplistview);
        adapter = new SkillShopAdapter(SkillShop.this,R.layout.skill_row,itemmall);
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
                new AlertDialog.Builder(SkillShop.this)
                        .setTitle("Do you want to buy this Skill?")
                .setCancelable(false).setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(datapoint >= itemmall.get(index).getPoint()){
                            boolean found = false;
                            for (Map.Entry<String, Skill> entry : myitem.entrySet()) {
                                String key = entry.getKey();
                                Skill value = entry.getValue();
                                if(value.getId() == itemmall.get(index).getId()){
                                    value.setAmount(value.getAmount() + 1);
                                    bucket.setText("Item in bucket : " + skillshop.size());
                                    datapoint -= itemmall.get(index).getPoint();
                                    found = true;
                                    break;
                                }
                            }
                            if(!found){
                                myitem.put("create",new Skill(itemmall.get(index).getId(),1,itemmall.get(index).getTitle(),itemmall.get(index).getDescription()));
                                datapoint -= itemmall.get(index).getPoint();
                            }
                            point.setText("Point : " + datapoint);
                            bucket.setText("Item in Bucket "+ (myitem.size()-skillshop.size()));

                        }
                        else {
                            Toast.makeText(SkillShop.this, "Don't have enough point", Toast.LENGTH_LONG).show();
                        }
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

    public void onSubmitSkill(View view){
        SubmitAdd submitAdd = new SubmitAdd();
        submitAdd.execute(myitem);
        Intent intent = new Intent(this, StudentSkill.class);
        startActivity(intent);
    }

    public void onBackToMyHome(View view){
        Intent intent = new Intent(this, StudentMainClassActivity.class);
        startActivity(intent);
    }

    class StudentPoint extends AsyncTask<Void,Void,Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                String sql ="SELECT User_Class_Point.point FROM User_Class_Point INNER JOIN Users ON User_Class_Point.user_id = Users.id " +
                    "INNER JOIN Courses ON User_Class_Point.course_id = Courses.id " +
                    "WHERE Users.id = "+ User.getOurInstance().getId()+" and Courses.id = "+ Course.getOurInstance().getId();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    return resultSet.getInt("point");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return 0;
        }
    }

    class getSkillData extends AsyncTask<Void,Void,ArrayList<Skill>>{

        @Override
        protected ArrayList<Skill> doInBackground(Void... voids) {
            ArrayList<Skill> skills = new ArrayList<>();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                String sql = "Select * From Skill";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String desc = resultSet.getString("description");
                    int point = resultSet.getInt("point");
                    skills.add(new Skill(id,0,title,desc));
                    skills.get(skills.size()-1).setPoint(point);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return skills;
        }


    }
    class SubmitAdd extends AsyncTask<Map<String,Skill>,Void,Void>{

        @Override
        protected Void doInBackground(Map<String, Skill>... maps) {
            Map <String,Skill> skillMap = maps[0];
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                String sql = "";
                Log.i("SHOW", "doInBackground: "+ "TEST");
                for (Map.Entry<String, Skill> entry : skillMap.entrySet()) {
                    String key = entry.getKey();
                    Skill value = entry.getValue();
                    System.out.println(key + value);
                    if(key.equals("edit")){
                        sql ="UPDATE User_Skill SET amount = " + value.getAmount()
                                + " WHERE user_id = " + User.getOurInstance().getId() +" and "+
                                " course_id = " + Course.getOurInstance().getId()+
                                " and skill_id = " + value.getId();
                        System.out.println(sql);
                    }
                    else{
                        sql ="INSERT INTO User_Skill VALUES ("+User.getOurInstance().getId()
                                +","+Course.getOurInstance().getId()+
                                ","+ value.getId() +","+value.getAmount()+")";
                        System.out.println(sql);
                    }
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql);
                    statement.executeUpdate("UPDATE User_Class_Point SET point = " + datapoint+
                            " WHERE user_id =" + User.getOurInstance().getId() +
                            " and course_id = " + Course.getOurInstance().getId());
                }

            }catch (Exception e){

            }
            return null;
        }
    }
}
