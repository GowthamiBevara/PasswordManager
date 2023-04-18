package com.example.passwordmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLHelper {

    Context context;

    SQLiteDatabase db;

    public SQLHelper(Context context) {
        this.context = context;
        db = context.openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS app_users(username VARCHAR UNIQUE, password VARCHAR);");

        db.execSQL("CREATE TABLE IF NOT EXISTS password_manager(username VARCHAR, app_name VARCHAR, mail VARCHAR, password VARCHAR, UNIQUE(username, app_name) ON CONFLICT REPLACE);");
    }
    public boolean login(String username, String pass) {
        Cursor c = db.rawQuery(
                "SELECT * FROM app_users WHERE username = ? AND password = ?",
                new String[]{username, pass}
        );
        return c.getCount() > 0;
        //  atleast one row is there with given credentials
    }

    public boolean register(String username, String pass) {
        Cursor c = db.rawQuery(
                "SELECT * FROM app_users WHERE username = ?",
                new String[]{username}
        );

        if (c.getCount() > 0) {
            // already registered what to do??
            return false;
        }

        Cursor c2 = db.rawQuery("INSERT INTO app_users VALUES (?,?);", new String[]{username, pass});
        Log.d("SQLHelper", "register: " + c2.getCount());
        Log.d("SQLHelper", "register: " + c2);
        return true ;

        // chichichichihci atleast one row is there with given credentials
    }

}
