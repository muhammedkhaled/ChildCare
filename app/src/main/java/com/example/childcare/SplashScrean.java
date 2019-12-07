package com.example.childcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScrean extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screan);

        Prefs.initPrefs(getApplicationContext());
        UserType type = Prefs.getUserType();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (type != null && user != null){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                    finish();
                }
            }
        }, 3000);


    }
}
