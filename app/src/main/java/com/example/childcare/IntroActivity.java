package com.example.childcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    private final static String SHAREDPEREFENCENAME = "com.example.childcare.userType";
    private final static String USERTYPEKEY = "userType";
    private String type ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Prefs.initPrefs(getApplicationContext(),SHAREDPEREFENCENAME,MODE_PRIVATE);
    }

    public void chParOnClick(View view) {
        switch (view.getId()){
            case R.id.childBtn:
                type = "Child";
                Prefs.putString(USERTYPEKEY,type);
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
            case R.id.parentBtn:
                type = "Parent";
                Prefs.putString(USERTYPEKEY,type);
                startActivity(new Intent(this, SignUpActivity.class).
                        putExtra("parent",1));
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
