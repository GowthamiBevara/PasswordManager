package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private SQLHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new SQLHelper(this);


        EditText unameEt = findViewById(R.id.unameEt);
        EditText passEt = findViewById(R.id.passEt);
        EditText confPassEt = findViewById(R.id.confPassEt);

        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener((view) -> finish());

        Button registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(view -> {
            String username = unameEt.getText().toString();
            String password = passEt.getText().toString();
            String confPassword = confPassEt.getText().toString();

            if(username.length() <3 || password.length() < 3) {
                showMessage("Invalid details", "Please enter valid details");
                return;
            }

            if (!confPassword.equals(password)) {
                showMessage("Password not matching", "Please double check passwords");
                return;
            }

            if(dbHelper.register(username, password)) {
                showMessage("User registered","Registration successfull");
            } else {
                showMessage("Registration Failed", "Something went wrong!");
            }
            finish();
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