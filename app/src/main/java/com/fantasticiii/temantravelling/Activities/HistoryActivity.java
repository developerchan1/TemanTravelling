package com.fantasticiii.temantravelling.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.fantasticiii.temantravelling.Fragments.ShowHistory;
import com.fantasticiii.temantravelling.Manager.FragmentChangeListener;
import com.fantasticiii.temantravelling.Model.History;
import com.fantasticiii.temantravelling.R;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements FragmentChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ShowHistory showHistory = new ShowHistory();

        getSupportFragmentManager().beginTransaction().replace(R.id.id_frame_layout_activity_history, showHistory).commit();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_out_right, R.anim.slide_in_right);
        fragmentTransaction.replace(R.id.frame_layout,fragment,fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
}