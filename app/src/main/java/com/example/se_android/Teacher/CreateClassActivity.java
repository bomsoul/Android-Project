package com.example.se_android.Teacher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Models.SecureKeyProvider;
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

public class CreateClassActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_class);
    }

    public void onCreateClass(View view) throws ExecutionException, InterruptedException {
        Intent intent= new Intent();
        setResult(RESULT_OK,intent);
        EditText editText = findViewById(R.id.editText);
        EditText editText2 = findViewById(R.id.editText2);
        String word = new CreateClass().execute(editText.getText().toString(),editText2.getText().toString()).get();
        intent.putExtra("word",word);
        finish();
    }

    public class CreateClass extends AsyncTask<String,String,String>{
        private int id = 0;
        @Override
        protected String doInBackground(String... strings) {
            User user = User.getOurInstance();
            SecureKeyProvider secureKeyProvider = new SecureKeyProvider();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                Log.i("dd", "Connect Success!!");
                String PIN = secureKeyProvider.getAlphaNumericString();
                String sql = "INSERT INTO Courses VALUES (null,'"+strings[0]+"','"+strings[1]+
                        "','"+user.getName()+"','"+ PIN +"'"+")";
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
                sql = "Select id from Courses Where PIN = '"+PIN+"'";
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    int cid = resultSet.getInt("id");
                    this.id = cid;
                    break;
                }
                sql = "INSERT INTO User_Course VALUES ("+this.id+","+user.getId()+",'"+PIN+"')";
                statement.executeUpdate(sql);


            }catch (Exception e){
                e.printStackTrace();
            }
            return "Add Class Successful!!";
        }
    }
}
