package com.fantasticiii.temantravelling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        toolbar = findViewById(R.id.toolbar);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        addBackArrow();
    }

    private void addBackArrow(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}