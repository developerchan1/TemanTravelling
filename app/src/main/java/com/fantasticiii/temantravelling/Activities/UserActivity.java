package com.fantasticiii.temantravelling.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fantasticiii.temantravelling.Adapter.ShowHistoryAdapter;
import com.fantasticiii.temantravelling.Fragments.ShowHistory;
import com.fantasticiii.temantravelling.Manager.PreferencesManager;
import com.fantasticiii.temantravelling.Model.History;
import com.fantasticiii.temantravelling.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    MaterialButton button;
    Button btnEditProfile;
    Toolbar toolbar;
    CircleImageView profileImage;
    TextView tvName, tvEmail;
    FirebaseFirestore db;
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        button = findViewById(R.id.id_btn_logout);
        toolbar = findViewById(R.id.toolbar2);
        profileImage = findViewById(R.id.profile_image);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        db = FirebaseFirestore.getInstance();
        preferencesManager = new PreferencesManager(this);

        addBackArrow();
        getUserData();

        //noinspection deprecation
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        button.setOnClickListener((view -> {
            //remove all data in sharedPreferences
            new PreferencesManager(this).resetAllUserData();
            
            //sign out from firebase auth
            FirebaseAuth.getInstance().signOut();

            Intent i = new Intent(UserActivity.this, WelcomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }));

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserActivity.this, EditProfileActivity.class);
                startActivity(i);
            }
        });
    }

    private void getUserData() {
        //profileImage.setImageURI(user.getPhotoUrl());
        tvName.setText(preferencesManager.getName());
        tvEmail.setText(preferencesManager.getEmail());
    }

    private void addBackArrow(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}