package com.example.se_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static final String dburl = "jdbc:mysql://remotemysql.com:3306/ytzEjdpCNf?characterEncoding=latin1";
    public static final String dbuser = "ytzEjdpCNf";
    public static final String dbpass = "L9LcYDD81I";

    EditText editText,editText2;
    User user = User.getOurInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.username);
        editText2 = findViewById(R.id.password);

    }

    public void onLoginClick(View view) throws ExecutionException, InterruptedException {
        if (editText.getText().toString().trim().equalsIgnoreCase("")){
            editText.setError("This field cannot be blank");
        }
        if (editText2.getText().toString().trim().equalsIgnoreCase("")){
            editText2.setError("This field cannot be blank");
        }
        else{
            User user = new LoginConection().execute(editText.getText().toString(),editText2.getText().toString()).get();
            if (user.getId() == -99){
                new AlertDialog.Builder(this)
                        .setTitle("Wrong Username or Password")
                        .setMessage("Please fill the right username and password")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
                editText2.setText("");
            }
            else{
                Intent intent = new Intent(this,HomeActivity.class);
                startActivity(intent);
            }
        }
    }

    public void onRegisClick(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==  RESULT_OK){
            Toast.makeText(this, data.getStringExtra("status"), Toast.LENGTH_SHORT).show();
        }
    }
    class LoginConection extends AsyncTask<String,Void, User> {
        @Override
        protected User doInBackground(String... strings) {
            User user = User.getOurInstance();
            String userss= strings[0];
            String pass = strings[1];
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                Statement statement = connection.createStatement();
                user.setId(-99);
                ResultSet resultSet = statement.executeQuery("Select * From Users where username = '"+ userss + "' and password='"+ pass+"'");
                while ((resultSet.next())){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    String role = resultSet.getString("role");
                    user.setId(id);
                    user.setRole(role);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setName(name);
                    user.setUsername(username);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return user;
        }

    }

}
