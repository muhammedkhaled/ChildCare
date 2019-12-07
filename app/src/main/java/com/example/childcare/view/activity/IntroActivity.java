package com.example.childcare.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.childcare.R;
import com.example.childcare.core.Prefs;
import com.example.childcare.model.UserType;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        FirebaseAuth.getInstance().signOut();
    }

    public void chParOnClick(View view) {
        switch (view.getId()) {
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
