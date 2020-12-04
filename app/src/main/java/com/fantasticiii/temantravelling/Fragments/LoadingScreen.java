package com.fantasticiii.temantravelling.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantasticiii.temantravelling.Activities.TourGuideActivity;
import com.fantasticiii.temantravelling.Manager.FragmentChangeListener;
import com.fantasticiii.temantravelling.R;

import java.util.Objects;

public class LoadingScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getActivity(), TourGuideActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        }, 1000);
    }
}