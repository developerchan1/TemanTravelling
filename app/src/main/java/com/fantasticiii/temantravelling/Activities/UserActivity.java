package com.fantasticiii.temantravelling.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.fantasticiii.temantravelling.R;

public class UserActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        button = findViewById(R.id.id_btn_logout);

        button.setOnClickListener((view -> {
            startActivity(new Intent(UserActivity.this, SignInActivity.class));
        }));
    }
}