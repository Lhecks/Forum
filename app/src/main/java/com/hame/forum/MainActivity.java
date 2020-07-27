package com.hame.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hame.forum.controller.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(getApplicationContext());
    }

    public void startForum(View view) {
        startActivity(new Intent(MainActivity.this, Forum.class));
//        finish();
    }

    public void startOpinion(View view) {
        startActivity(new Intent(MainActivity.this, Opinion.class));
//        finish();
    }

    public void signOut(View view) {
        sessionManager.logOut();
        startActivity(new Intent(MainActivity.this, Login.class));
    }

}