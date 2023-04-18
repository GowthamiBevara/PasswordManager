package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

//    final String USERNAME = "gow";
//    final String PASSWORD = "1234";

    SQLHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        dbHelper = new SQLHelper(this);

        EditText unameEt = findViewById(R.id.unameEt);
        EditText passEt = findViewById(R.id.passEt);

        Button loginBtn = findViewById(R.id.loginBtn);
        Button registerBtn = findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener((view) -> {
            String username = unameEt.getText().toString();
            String password = passEt.getText().toString();

            if (dbHelper.login(username, password)) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("loggedin", true);
                i.putExtra("username", username);
                startActivity(i);
                finish();
            } else {
                showMessage("Wrong credentials!", "Please check your username and password, try again!");
            }
        });

        registerBtn.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }



    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}