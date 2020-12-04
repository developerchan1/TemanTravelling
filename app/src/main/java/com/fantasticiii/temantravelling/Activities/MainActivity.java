package com.fantasticiii.temantravelling.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.fantasticiii.temantravelling.R;

public class MainActivity extends AppCompatActivity {

    LinearLayout itemTourGuide, itemPlace, itemHistory, itemAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemTourGuide = findViewById(R.id.item_tour_guide);
        itemPlace = findViewById(R.id.item_place);
        itemHistory = findViewById(R.id.item_history);
        itemAccount = findViewById(R.id.item_account);

        itemTourGuide.setOnClickListener((view) -> {
            startActivity(new Intent(MainActivity.this, OrderTourGuideActivity.class));
        });
    }
}