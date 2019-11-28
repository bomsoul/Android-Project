package com.example.se_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.se_android.Models.Users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.se_android.MainActivity.dbpass;
import static com.example.se_android.MainActivity.dburl;
import static com.example.se_android.MainActivity.dbuser;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText name,email,username,password;
    String[] roles={"teacher","student"};
    Spinner spin;
    String role = "";
    ArrayList<Users> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        spin = (Spinner) findViewById(R.id.simpleSpinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,roles);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        username = findViewById(R.id.usernameRegis);
        password = findViewById(R.id.passWord);
        try {
            users = new QueryUser().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void onRegisClick(View view){
        if (name.getText().toString().trim().equalsIgnoreCase("")){
            name.setError("This field cannot be blank");
        }
        if (email.getText().toString().trim().equalsIgnoreCase("")){
            email.setError("This field cannot be blank");
        }
        if (username.getText().toString().trim().equalsIgnoreCase("")){
            username.setError("This field cannot be blank");
        }
        if (password.getText().toString().trim().equalsIgnoreCase("")){
            password.setError("This field cannot be blank");
        }
        else{
            boolean status = true;
            for (Users user: users) {
                if (user.getEmail().equals(email.getText().toString())){
                    email.setError("This Email is already exist.");
                    status = false;
                    break;
                }
                if (user.getUsername().equals(username.getText().toString())){
                    username.setError("This username is already exist");
                    status = false;
                    break;
                }
            }
            if(status){
                String text_status = "";
                try {
                    text_status = new Register().execute(name.getText().toString(),
                            email.getText().toString(),username.getText().toString(),
                            password.getText().toString(),"student").get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("DD", "onRegisClick: "+"Register Complete");
                if(text_status.equals("Register Success")){
                    Intent intent = new Intent();
                    intent.putExtra("status",text_status);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("status",text_status);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), roles[position], Toast.LENGTH_LONG).show();
        role = roles[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        ((TextView)spin.getSelectedView()).setError("Please Select role");
    }

    class QueryUser extends AsyncTask<Void,Void, ArrayList<Users>>{

        @Override
        protected ArrayList<Users> doInBackground(Void... voids) {
            ArrayList<Users> user = new ArrayList<>();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Users");
                while ((resultSet.next())){
                    Log.i("DD", "doInBackground: getData");
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");

                    user.add(new Users(id,name,username,password,email));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return user;
        }
    }

    public void BacktoHome(View view){
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    public class Register extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String email = strings[1];
            String username = strings[2];
            String password = strings[3];
            String role = strings[4];
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dburl,dbuser,dbpass);
                String sql = "INSERT INTO Users VALUES " +
                        "(null,'"+name+"','"+username+"','"+password+"','"+email+"','"+role+"')";
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
                return "Register Success";
            }catch (Exception e){
                e.printStackTrace();
            }
            return "Register Fail";
        }
    }
}
