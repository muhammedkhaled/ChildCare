package com.example.childcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void chParOnClick(View view) {
        switch (view.getId()){
            case R.id.childBtn:
                Prefs.saveUserType(UserType.CHILD);
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
            case R.id.parentBtn:
                Prefs.saveUserType(UserType.PARENT);
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
        }
    }
}
