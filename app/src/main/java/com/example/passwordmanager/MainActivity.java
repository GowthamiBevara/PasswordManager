package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {
    EditText a_name, mail, password, d_name, ap_name;
    Button s, viewAllBtn, viewAppPassBtn, delBtn;
    SQLiteDatabase db;
    int color = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate: " + validateEmail("gow@gmail.com"));
        Log.d("MainActivity", "onCreate: " + validateEmail("gow@gmail.i"));
        Log.d("MainActivity", "onCreate: " + validateEmail("gow%@gmail.c1"));
        Log.d("MainActivity", "onCreate: " + validateEmail("gow123@gmail.com"));
        if (!getIntent().getBooleanExtra("loggedin", false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String username = getIntent().getStringExtra("username");


        a_name = (EditText) findViewById(R.id.app_name);
        mail = (EditText) findViewById(R.id.mail);

        password = (EditText) findViewById(R.id.password);
        d_name = (EditText) findViewById(R.id.d_name);
        ap_name = (EditText) findViewById(R.id.ap_name);
        s = (Button) findViewById(R.id.save);
        viewAllBtn = (Button) findViewById(R.id.view_all);
        viewAppPassBtn = (Button) findViewById(R.id.view_1);
        delBtn = (Button) findViewById(R.id.delete);


        //data base creation

        db = new SQLHelper(this).db;

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String app_name = a_name.getText().toString().trim();
                String app_email = mail.getText().toString().trim();
                String app_password = password.getText().toString().trim();

                if (app_name.length() == 0 || app_email.length() == 0 ||
                        app_password.length() == 0) {
                    showMessage("Error", "Please enter all values");
                    s.setBackgroundColor(Color.RED);
                    return;
                }
                if(!validateEmail(app_email)) {
                    showMessage("Invalid Email!", "Please enter valid email");
                    s.setBackgroundColor(Color.RED);
                    return;
                }
                try {

                    db.execSQL(
                            "INSERT INTO password_manager VALUES(?,?,?,?);",
                            new String[]{username, app_name, app_email, app_password});
                    showMessage("Success", "Record added");
                    s.setBackgroundColor(Color.GREEN);
                    clearText();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c = db.rawQuery("SELECT * FROM password_manager WHERE username = ?", new String[]{username});
                if (c.getCount() == 0) {
                    showMessage("Error", "No records found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (c.moveToNext()) {
//                    buffer.append("Username: " + c.getString(0) + "\n");
                    buffer.append("App Name: " + c.getString(1) + "\n");
                    buffer.append("Mail : " + c.getString(2) + "\n");
                    buffer.append("Password: " + c.getString(3) + "\n\n");
                }
                showMessage(username + " / Passwords", buffer.toString());
            }
        });
        viewAppPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String appName = ap_name.getText().toString();
                Cursor c = db.rawQuery(
                        "SELECT * FROM password_manager where username = ? AND app_name = ?;",
                        new String[]{username, appName}
                );
                if (c.getCount() == 0) {
                    showMessage("Error", "No records found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (c.moveToNext()) {
                    buffer.append("Mail : " + c.getString(2) + "\n");

                    buffer.append("Password: " + c.getString(3) + "\n\n");
                }
                showMessage(username + " / Password of / " + appName, buffer.toString());
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String appName = d_name.getText().toString();
                if (appName.trim().length() == 0) {
                    showMessage("Error", "Please enter value");
                    return;
                }
                int rowsDeleted = db.delete("password_manager", "username = ? AND app_name = ?", new String[]{username, appName});
                if (rowsDeleted > 0) {
                    showMessage("Delete", "Record Deleted Successfully");
                } else {
                    showMessage("Error", "No records found");
                }
                clearText();
            }
        });

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        a_name.setText("");
        mail.setText("");
        password.setText("");
        ap_name.setText("");
        d_name.setText("");
        a_name.requestFocus();//places the cursor where we want
    }

    public boolean validateEmail(String app_mail) {
        if (app_mail == null) {
            return false;
        }
        // Check if the email has a valid format
        return app_mail.matches("\\w+@\\w+\\.[A-Za-z]{2,3}");
    }


}