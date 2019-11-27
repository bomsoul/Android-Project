package com.example.se_android.Student;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Models.User;
import com.example.se_android.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import static com.example.se_android.MainActivity.dbpass;
import static com.example.se_android.MainActivity.dburl;
import static com.example.se_android.MainActivity.dbuser;

public class JoinClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_join_class);
    }

    public void onJoinClick(View view) throws ExecutionException, InterruptedException {
        EditText editText = findViewById(R.id.editText);
        String word = new JoinClass().execute(editText.getText().toString()).get();
        if(word.equals("You are joined class.")){
            Intent intent = new Intent();
            intent.putExtra("word",word);
            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Wrong PIN")
                    .setMessage(word)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();
            editText.setText("");
        }
    }

    class JoinClass extends AsyncTask<String,Void,String>{
        int id ;
        @Override
        protected String doInBackground(String... strings) {
            User user = User.getOurInstance();
            String PIN = strings[0];
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                String sql = "SELECT id from Courses WHERE PIN = '"+PIN+"'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    this.id = resultSet.getInt("id");
                    break;
                }
                if (this.id > 0){
                    sql = "INSERT INTO User_Course VALUE ("+this.id+","+user.getId()+",'"+PIN+"')";
                    statement.executeUpdate(sql);
                    sql ="SELECT * FROM Courses Where PIN = '"+PIN+"'";
                    resultSet = statement.executeQuery(sql);
                    int id = 0;
                    while (resultSet.next()){
                        id = resultSet.getInt("id");
                    }
                    sql = "INSERT INTO User_Class_Point VALUES (null,0,"+user.getId()+","+id+")";
                    statement.executeUpdate(sql);
                }
                else {
                    return "No course with this PIN";
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "You are joined class.";
        }
    }
}
