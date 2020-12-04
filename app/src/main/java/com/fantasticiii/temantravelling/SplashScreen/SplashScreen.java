package com.fantasticiii.temantravelling.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import com.fantasticiii.temantravelling.Activities.MainActivity;
import com.fantasticiii.temantravelling.Activities.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set theme to light
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, WelcomeActivity.class));
        }else{
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }
}